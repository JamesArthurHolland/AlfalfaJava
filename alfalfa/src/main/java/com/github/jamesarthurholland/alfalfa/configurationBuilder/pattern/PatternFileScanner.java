package com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern;

import com.github.jamesarthurholland.alfalfa.FileUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class PatternFileScanner {
    public static String ALFALFA_FILE = "AlfalfaFile";
    public static String IMPORTS_KEY = "imports";
    public static String LOCATION_KEY = "location";
    public static String NAME_KEY = "name";
    public static String SWAP_NAME_KEY = "swap";
    public static String VERSION_KEY = "version";
    public static String VARS_KEY = "vars";

    private Path workingDirectory = null;

    public PatternFileScanner(Path workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public Pattern scan() {
        if(Files.exists(workingDirectory.resolve(ALFALFA_FILE))) {
//            PatternBuilder patternBuilder = new PatternBuilder();
            PatternBuilder patternBuilder = new PatternBuilder();
            return scanPatternFile(patternBuilder, workingDirectory);
        }
        return null;
    }

    private Pattern scanPatternFile(PatternBuilder patternBuilder, Path workingDirectory) {
        try {
            if( ! Files.exists(workingDirectory.resolve(ALFALFA_FILE))) {
                return patternBuilder.build(); // TODO this needs to work for files in the directory
            }
            Path alfalfaFile = workingDirectory.resolve(ALFALFA_FILE);
            BufferedReader reader = Files.newBufferedReader(alfalfaFile);

            Map<String, Object> patternYamlObject = (Map<String, Object>) new Yaml().load(reader);
            String currentPatternName = (String) patternYamlObject.get(NAME_KEY); // TODO builder.addHashmap for this as well
            String currentPatternVersion = (String) patternYamlObject.get(VERSION_KEY);
            String currentPatternLocationString = (String) patternYamlObject.getOrDefault(LOCATION_KEY,"./");
            Path currentPatternPath = Paths.get(currentPatternLocationString);
            // TODO scan vars and (pass them down the tree)???
            LinkedHashMap<String, String> vars = new LinkedHashMap<>();

            if(patternBuilder == null) {
                patternBuilder = new PatternBuilder();
            }

            patternBuilder.setName(currentPatternName)
                .setVersion(currentPatternVersion)
                .setVars(vars)
                .setOutputLocation(currentPatternPath);

            Pattern currentPattern = patternBuilder.build();


            ArrayList<LinkedHashMap<String, Object>> imports = (ArrayList<LinkedHashMap<String, Object>>) patternYamlObject.get(IMPORTS_KEY);

            for(LinkedHashMap<String, Object> importHashMap : imports) {
                Pattern importedPattern = getImportPattern(currentPattern, importHashMap);
                currentPattern.imports.add(importedPattern);
            }

            return currentPattern;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Pattern getImportPattern(Pattern currentPattern, LinkedHashMap<String, Object> importHashMap) {
        PatternBuilder importPatternBuilder = new PatternBuilder();

        LinkedHashMap<String, Object> swapHashMap = (LinkedHashMap<String, Object>) importHashMap.get(SWAP_NAME_KEY);
        if(swapHashMap != null) {
            importPatternBuilder = new PatternBuilder(swapHashMap);
            Pattern swapPattern = scanPatternFile(importPatternBuilder, getPathForHashMap(currentPattern, swapHashMap));

            if(swapPattern != null) {
                importPatternBuilder = new PatternBuilder(swapPattern);
            }
        }

        importPatternBuilder.addImportHashMap(importHashMap, currentPattern);

        Pattern importedPattern = scanPatternFile(importPatternBuilder, getPathForHashMap(currentPattern, importHashMap));

        if(importedPattern == null) {
            importedPattern = importPatternBuilder.build();
        }

        return importedPattern;
    }

    public static Path getPathForHashMap(Pattern parentPattern, LinkedHashMap<String, Object> importHashMap) {
        String patternName = (String) importHashMap.get(NAME_KEY);
        String patternVersion = (String) importHashMap.get(VERSION_KEY);


        Path patternPath;
        if (FileUtils.patternIsASubModuleOfCurrentPattern(patternName)) {
            patternPath = FileUtils.modulePathFromParentAndName(
                parentPattern.name,
                parentPattern.version, // TODO different version maybe allowed? Pass swapVersion if exists
                patternName
            );
        } else {
            patternPath = FileUtils.modulePath(patternName, patternVersion);
        }
        if(patternPath == null) {
            throw new RuntimeException("Swap pattern path can't be null for " + importHashMap.get(NAME_KEY));
        }
        return patternPath;
    }

    protected static class PatternBuilder {
        private Pattern patternTmp;

        public PatternBuilder(Pattern pattern) {
            this.patternTmp = pattern;
        }

        public PatternBuilder() {
            patternTmp = new Pattern();
        }

        public PatternBuilder(LinkedHashMap<String, Object> importHashMap)
        {
            patternTmp = new Pattern();
            addImportHashMap(importHashMap, null);
        }

        public PatternBuilder addImportHashMap(LinkedHashMap<String, Object> importHashMap, Pattern parentPattern) {
            if(importHashMap != null) {
                String importName = (String) importHashMap.get(NAME_KEY);
                String importVersion = (String) importHashMap.getOrDefault(VERSION_KEY, "");
                if(FileUtils.patternIsASubModuleOfCurrentPattern(importName) && importVersion.isEmpty()) {
                    this.setVersion(parentPattern.version);
                }

                String outputLocation = (String) importHashMap.getOrDefault(LOCATION_KEY, "./");
                Path outputPath = parentPattern.outputPath.resolve(outputLocation);

                Path patternFilesPath = getPathForHashMap(parentPattern, importHashMap);

                this.setName((String) importHashMap.get(NAME_KEY))
                    .setVersion((String) importHashMap.get(VERSION_KEY))
                    .setVars((LinkedHashMap<String, String>) importHashMap.getOrDefault(VARS_KEY, new LinkedHashMap<String, String>()))
                    .setOutputLocation(outputPath)
                    .addFiles(FileUtils.getFilePathsRecursive(patternFilesPath));
            }

            return this;
        }

        public Pattern build() {



            return patternTmp;
        }

        public PatternBuilder setName(String name) {
            patternTmp.name = Optional.ofNullable(patternTmp.name).orElse(name);
            return this;
        }

        public PatternBuilder addFiles(ArrayList<Path> paths) {
            patternTmp.files.addAll(paths);
            return this;
        }

        public PatternBuilder setVersion(String version) {
            patternTmp.version = Optional.ofNullable(patternTmp.version).orElse(version);
            return this;
        }

        public PatternBuilder setVars(LinkedHashMap<String, String> vars) {
            vars.forEach(patternTmp.vars::putIfAbsent);
            return this;
        }

        public PatternBuilder setOutputLocation(Path location) {
            patternTmp.outputPath = location;
            return this;
        }


    }
}
