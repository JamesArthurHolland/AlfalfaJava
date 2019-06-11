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
            PatternBuilder patternBuilder = new PatternBuilder();
            return scanPatternFile(workingDirectory, patternBuilder);
        }
        return null;
    }

    private Pattern scanPatternFile(Path workingDirectory, PatternBuilder patternBuilder) {
        try {
            Path alfalfaFile = workingDirectory.resolve(ALFALFA_FILE);
            BufferedReader reader = Files.newBufferedReader(alfalfaFile);
            //            Yaml object = new Yaml(new Constructor(Pattern.class));
//                Pattern pattern = (Pattern) new Yaml(new Constructor(Pattern.class)).load(reader);

            Pattern currentPattern = null;

            Map<String, Object> patternYamlObject = (Map<String, Object>) new Yaml().load(reader);
            String currentPatternName = (String) patternYamlObject.get(NAME_KEY);
            String currentPatternVersion = (String) patternYamlObject.get(VERSION_KEY);
            // TODO scan vars and (pass them down the tree)???
            LinkedHashMap<String, String> vars = new LinkedHashMap<>();

            if(patternBuilder == null) {
                patternBuilder = new PatternBuilder();
            }

            patternBuilder.setName(currentPatternName)
                    .setVersion(currentPatternVersion)
                    .setVars(vars);

            currentPattern = patternBuilder.build();


            ArrayList<LinkedHashMap<String, Object>> imports = (ArrayList<LinkedHashMap<String, Object>>) patternYamlObject.get(IMPORTS_KEY);
            for(LinkedHashMap<String, Object> importHashMap : imports) {
                String importName = (String) importHashMap.get(NAME_KEY);
                System.out.println("DEBUG PATTERN SCAN");

                LinkedHashMap<String, Object> swapHashMap = (LinkedHashMap<String, Object>) importHashMap.get(SWAP_NAME_KEY);

                PatternBuilder importPatternBuilder = swapHashMap == null ? new PatternBuilder() : new PatternBuilder(swapHashMap);
                Pattern swapPattern = getPattern(currentPatternName, currentPatternVersion, swapHashMap, importPatternBuilder);

                importPatternBuilder = swapPattern == null ? new PatternBuilder() : new PatternBuilder(swapPattern);
                importHashMap.putIfAbsent(VERSION_KEY, currentPatternVersion);
                if(importHashMap.get(VERSION_KEY).equals(null)) {
                    importPatternBuilder.setVersion(currentPatternVersion);
                }
                importPatternBuilder.addImportHashMap(importHashMap);
                Pattern importedPattern = getPattern(currentPatternName, currentPatternVersion, importHashMap, importPatternBuilder);


                // TODO if no alfalfa in module, still add the files to new pattern
                if(importedPattern == null) {
                    importedPattern = importPatternBuilder.build();
                }
                currentPattern.imports.add(importedPattern);



//                PatternBuilder importPatternBuilder = new PatternBuilder()
//                        .setName(importName)
//                        .setLocation(location)
//                        .setVersion((String) importHashMap.get(VERSION_KEY))
//                        .setVars((LinkedHashMap<String, String>) importHashMap.get(VARS_KEY));


//                Pattern importedPattern = scanPatternFile(FileUtils.patternPath(importName, version), importPatternBuilder);
//                Path patternPath = importDetail.getPatternPath();

                //                this.imports.put()
            }
            ////            pattern = yaml.load(reader);
            //            pattern = new Pattern(name, version, imports);
            System.out.println("AlfalfaFile scan");
            return currentPattern;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Pattern getPattern(String currentPatternName, String currentPatternVersion, LinkedHashMap<String, Object> importHashMap, PatternBuilder patternBuilder) {
        // TODO get vars etc from this level

        if(importHashMap != null) {

            Path patternPath = getPathForPatternName(currentPatternName, currentPatternVersion, importHashMap);
            patternBuilder.addFiles(FileUtils.getFilePathsRecursive(patternPath));

            if(Files.exists(patternPath.resolve(ALFALFA_FILE))) {
                return scanPatternFile(
                    patternPath,
                    patternBuilder
                );
            }
            else {
                return patternBuilder.build();
            }
        }
        return null;
    }

    private Path getPathForPatternName(String currentPatternName, String currentPatternVersion, LinkedHashMap<String, Object> importHashMap) {
        String patternName = (String) importHashMap.get(NAME_KEY);
        String patternVersion = (String) importHashMap.get(VERSION_KEY);

        Path patternPath;
        if (patternIsASubModuleOfCurrentPattern(patternName)) {
            patternPath = FileUtils.modulePath(
                currentPatternName,
                currentPatternVersion, // TODO different version maybe allowed? Pass swapVersion if exists
                patternName
            );
        } else {
            patternPath = FileUtils.modulePath(patternName, patternVersion);
        }
        if(patternPath == null) {
            throw new RuntimeException("Swap pattern path can't be null for " + this.workingDirectory + ", " + importHashMap.get(NAME_KEY));
        }
        return patternPath;
    }

    private boolean patternIsASubModuleOfCurrentPattern(String patternName) {
        return patternName.contains(".") == false;
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
            addImportHashMap(importHashMap);
        }

        public PatternBuilder addImportHashMap(LinkedHashMap<String, Object> importHashMap) {
            String locationString = (String) importHashMap.getOrDefault(LOCATION_KEY, "");
            if( ! locationString.isEmpty() ) {
                this.setLocation(Paths.get(locationString));
            }

            this.setName((String) importHashMap.get(NAME_KEY))
                    .setVersion((String) importHashMap.get(VERSION_KEY))
                    .setVars((LinkedHashMap<String, String>) importHashMap.getOrDefault(VARS_KEY, new LinkedHashMap<String, String>()));

            return this;
        }

//        public PatternBuilder addSwapHashMap(LinkedHashMap<String, Object> swapHashMap, String importName) {
//            String locationString = (String) swapHashMap.get(LOCATION_KEY);
//
//            this.setName(importName)
//                    .setVersion((String) swapHashMap.get(VERSION_KEY))
//                    .setLocation(Paths.get(locationString))
//                    .setVars((LinkedHashMap<String, String>) swapHashMap.get(VARS_KEY));
//
//            return this;
//        }

        public Pattern build() {
            return patternTmp;
        }

        public PatternBuilder setName(String name) {
            patternTmp.name = Optional.ofNullable(patternTmp.name).orElse(name);
            return this;
        }

//        public PatternBuilder setSwapName(String swapName) {
//            patternTmp.name = Optional.ofNullable(patternTmp.name).orElse(swapName);
//            return this;
//        }

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

        public PatternBuilder setLocation(Path location) {
            patternTmp.outputLocation = location;
            return this;
        }


    }
}
