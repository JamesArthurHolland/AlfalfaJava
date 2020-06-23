package com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern;

import com.github.jamesarthurholland.alfalfa.FileUtils;
import com.hubspot.jinjava.Jinjava;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.stream.Collectors;

public class PatternFileScanner {
    public static String ALFALFA_FILE = "AlfalfaFile";
    public static String IMPORTS_KEY = "imports";
    public static String LOCATION_KEY = "location";
    public static String MODE_KEY = "mode";
    public static String NAME_KEY = "name";
    public static String SWAP_NAME_KEY = "swap";
    public static String VERSION_KEY = "version";
    public static String VARS_KEY = "vars";
    public static String FOLDER_SWAPS_KEY = "folder_swaps";

    public static LinkedHashMap<String, Pattern.ImportMode> IMPORT_LOOKUP = new LinkedHashMap<>();

    static {
        IMPORT_LOOKUP.put("once", Pattern.ImportMode.ONCE_FOR_ENTITY);
        IMPORT_LOOKUP.put("for_each", Pattern.ImportMode.FOR_EACH_ENTITY);
    }

    private Path workingDirectory = null;

    public PatternFileScanner(Path workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public Pattern scan() {
        if(Files.exists(workingDirectory.resolve(ALFALFA_FILE))) {
//            PatternBuilder patternBuilder = new PatternBuilder();
            Pattern pattern = scanPatternFile(workingDirectory, null);
            return pattern;
        }
        return null;
    }

    private Pattern scanPatternFile(Path workingDirectory, PatternBuilder parentPatternBuilder) {
        try {
            if( ! Files.exists(workingDirectory.resolve(ALFALFA_FILE))) {
                return parentPatternBuilder.build(); // TODO this needs to work for files in the directory
            }
            Path alfalfaFile = workingDirectory.resolve(ALFALFA_FILE);
            BufferedReader reader = Files.newBufferedReader(alfalfaFile);

            LinkedHashMap<String, Object> patternHashMap = (LinkedHashMap<String, Object>) new Yaml().load(reader);
            PatternBuilder currentPatternBuilder = new PatternBuilder();
            if(parentPatternBuilder != null) {
                currentPatternBuilder = parentPatternBuilder.clone();
            }
            currentPatternBuilder.addPatternHashMap(patternHashMap);

            ArrayList<LinkedHashMap<String, Object>> imports = (ArrayList<LinkedHashMap<String, Object>>) patternHashMap.get(IMPORTS_KEY);

            for(LinkedHashMap<String, Object> importHashMap : imports) {
                Pattern importedPattern = getImportPattern(importHashMap, currentPatternBuilder);
                currentPatternBuilder.build().imports.add(importedPattern);
            }

            return currentPatternBuilder.build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Pattern getImportPattern(LinkedHashMap<String, Object> importHashMap, PatternBuilder currentPatternBuilder) {
        PatternBuilder importPatternBuilder = new PatternBuilder();

        LinkedHashMap<String, Object> swapHashMap = (LinkedHashMap<String, Object>) importHashMap.get(SWAP_NAME_KEY);
        if(swapHashMap != null) {
            importPatternBuilder = new PatternBuilder(swapHashMap);
            Pattern swapPattern = scanPatternFile(getPathForHashMap(swapHashMap, currentPatternBuilder), importPatternBuilder);

            if(swapPattern != null) {
                importPatternBuilder = new PatternBuilder(swapPattern);
            }
        }

        importPatternBuilder.addImportHashMap(importHashMap, currentPatternBuilder);

        Pattern importedPattern = scanPatternFile(getPathForHashMap(importHashMap, currentPatternBuilder), importPatternBuilder);

        if(importedPattern == null) {
            importedPattern = importPatternBuilder.build();
        }

        return importedPattern;
    }

    public static Path getPathForHashMap(LinkedHashMap<String, Object> importHashMap, PatternBuilder parentPatternBuilder) {
        String patternName = (String) importHashMap.get(NAME_KEY);
        String patternVersion = (String) importHashMap.get(VERSION_KEY);
        if(parentPatternBuilder == null) {
            return FileUtils.modulePath(patternName, patternVersion);
        }

        Path patternPath;
        if (FileUtils.patternIsASubModuleOfCurrentPattern(patternName)) {
            patternPath = FileUtils.modulePathFromParentAndName(
                parentPatternBuilder.patternTmp.name,
                parentPatternBuilder.patternTmp.version, // TODO different version maybe allowed? Pass swapVersion if exists
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

        public PatternBuilder clone() {
            return new PatternBuilder(patternTmp.copy());
        }

        public PatternBuilder addPatternHashMap(LinkedHashMap<String, Object> patternHashMap) {
            String currentPatternName = (String) patternHashMap.get(NAME_KEY); // TODO builder.addHashmap for this as well
            String currentPatternVersion = (String) patternHashMap.get(VERSION_KEY);
            String currentPatternLocationString = (String) patternHashMap.getOrDefault(LOCATION_KEY,"./");

            Path currentPatternPath = Paths.get(currentPatternLocationString);
//             TODO scan vars and (pass them down the tree)???
            LinkedHashMap<String, String> vars = parseVarsFromHashMap(patternHashMap, patternTmp.vars);

            this.setName(currentPatternName)
                    .setVersion(currentPatternVersion)
                    .setVars(vars)
                    .setOutputLocation(currentPatternPath);

            return this;
        }

        private LinkedHashMap<String, String> parseVarsFromHashMap(LinkedHashMap<String, Object> patternHashMap, LinkedHashMap<String, String> parentVars) {
            return parseList(VARS_KEY, patternHashMap, parentVars);
        }


        private LinkedHashMap<String, String> parseFolderSwapsFromHashMap(LinkedHashMap<String, Object> patternHashMap, LinkedHashMap<String, String> parentFolderSwaps) {
            return parseList(FOLDER_SWAPS_KEY, patternHashMap, parentFolderSwaps);
        }

        private static LinkedHashMap<String, String> parseList(String key, LinkedHashMap<String, Object> patternHashMap, LinkedHashMap<String, String> parentItems) {
            LinkedHashMap<String, String> items = (LinkedHashMap<String, String>) patternHashMap.getOrDefault(key, new LinkedHashMap<>());
            if(parentItems.size() == 0) {
                return items;
            }

            items.entrySet().stream().forEach(entry -> {
                String renderedVariable = new Jinjava().render(entry.getValue(), parentItems);
                items.put(entry.getKey(), renderedVariable);
            });
            parentItems.entrySet().stream().forEach(entry -> {
                items.putIfAbsent(entry.getKey(), entry.getValue());
            });
            return items;
        }


        public PatternBuilder addImportHashMap(LinkedHashMap<String, Object> importHashMap, PatternBuilder parentPatternBuilder) {
            if(importHashMap != null) {
                String importName = (String) importHashMap.get(NAME_KEY);
                String importVersion = (String) importHashMap.getOrDefault(VERSION_KEY, "");
                String outputLocation = (String) importHashMap.getOrDefault(LOCATION_KEY, "./");

                String currentModeString = (String) importHashMap.getOrDefault(MODE_KEY, "once");
                Pattern.ImportMode mode = IMPORT_LOOKUP.get(currentModeString);


                if(FileUtils.patternIsASubModuleOfCurrentPattern(importName) && importVersion.isEmpty()) {
                    this.setVersion(parentPatternBuilder.patternTmp.version); // TODO confirm
                }

                Path outputPath = parentPatternBuilder.patternTmp.getOutputPath() == null ? Paths.get(outputLocation) : parentPatternBuilder.patternTmp.getOutputPath().resolve(outputLocation);

                Path patternRepoPath = getPathForHashMap( importHashMap, parentPatternBuilder);


                LinkedHashMap<String, String> vars = parseVarsFromHashMap(importHashMap, parentPatternBuilder.patternTmp.vars);

                ArrayList<Path> filesAndDirectoriesNoAlfalfaFiles = FileUtils.getFilePathsRecursive(patternRepoPath)
                        .stream()
                        .filter(path -> ! FileUtils.isAlfalfaFile(path) )
                        .collect(Collectors.toCollection(ArrayList::new));

                LinkedHashMap<String, String> folderSwaps = parseFolderSwapsFromHashMap(importHashMap, patternTmp.folderSwaps);

                this.setName((String) importHashMap.get(NAME_KEY))
                    .setVersion((String) importHashMap.get(VERSION_KEY))
                    .setVars(vars)
                    .setFolderSwaps(folderSwaps)
                    .setOutputLocation(outputPath)
                    .setMode(mode)
                    .addFiles(filesAndDirectoriesNoAlfalfaFiles) // TODO don't copy alfalfafile
                    .setPatternRepoPath(patternRepoPath);
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
            paths.stream()
                .map(Path::toString)
                .forEach(path -> patternTmp.files.add(path));
            return this;
        }

        public PatternBuilder setMode(Pattern.ImportMode mode) {
            patternTmp.mode = mode;
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
            patternTmp.setOutputPath(location);
            return this;
        }

        public PatternBuilder setPatternRepoPath(Path patternRepoPath) {
            patternTmp.setPatternRepoPath(patternRepoPath);
            return this;
        }

        public PatternBuilder setFolderSwaps(LinkedHashMap<String, String> swaps) {
            swaps.forEach(patternTmp.folderSwaps::putIfAbsent);
            return this;
        }
    }


}
