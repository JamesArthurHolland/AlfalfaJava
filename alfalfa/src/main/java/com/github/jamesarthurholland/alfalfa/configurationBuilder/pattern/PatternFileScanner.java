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

    public static Pattern scanRootPatternFile(Path workingDirectory) {
        Path alfalfaFile = workingDirectory.resolve(ALFALFA_FILE);
//        Pattern pattern = null;
        if(Files.exists(alfalfaFile)) {
            PatternBuilder patternBuilder = new PatternBuilder();
            return scanPatternFile(alfalfaFile, null);
        }
        return null;
    }

    private static Pattern scanPatternFile(Path alfalfaFile, PatternBuilder patternBuilder) {
        try {
            BufferedReader reader = Files.newBufferedReader(alfalfaFile);
            //            Yaml object = new Yaml(new Constructor(Pattern.class));
//                Pattern pattern = (Pattern) new Yaml(new Constructor(Pattern.class)).load(reader);

            Pattern currentPattern = null;

            Map<String, Object> patternYamlObject = (Map<String, Object>) new Yaml().load(reader);
            if(patternBuilder == null) {
                patternBuilder = new PatternBuilder();
            }
            String name = (String) patternYamlObject.get(NAME_KEY);
            String version = (String) patternYamlObject.get(VERSION_KEY);
            // TODO scan vars and (pass them down the tree)???
            LinkedHashMap<String, String> vars = new LinkedHashMap<>();
            patternBuilder.setName(name)
                    .setVersion(version)
                    .setVars(vars);

            currentPattern = patternBuilder.build();


            ArrayList<LinkedHashMap<String, Object>> imports = (ArrayList<LinkedHashMap<String, Object>>) patternYamlObject.get(IMPORTS_KEY);
            for(LinkedHashMap<String, Object> importHashMap : imports) {
                //                // TODO get pattern from file

                System.out.println("DEBUG PATTERN SCAN");

                LinkedHashMap<String, Object> swap = (LinkedHashMap<String, Object>) importHashMap.get(SWAP_NAME_KEY);

                PatternBuilder swapPatternBuilder = new PatternBuilder(swap);

                Pattern importedPattern = scanPatternFile(
                        FileUtils.patternPath(
                                (String) importHashMap.get(NAME_KEY),
                                (String) importHashMap.get(VERSION_KEY)),
                        swapPatternBuilder
                );

                String locationString = (String) importHashMap.get(LOCATION_KEY);
                Path location = Paths.get(locationString);

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


    protected static class PatternBuilder {
        private Pattern patternTmp;

        public PatternBuilder() {
            patternTmp = new Pattern();
        }

        public PatternBuilder(LinkedHashMap<String, Object> importHashMap)
        {
            patternTmp = new Pattern();
            addImportHashMap(importHashMap);
        }

        public PatternBuilder addImportHashMap(LinkedHashMap<String, Object> importHashMap) {
            String locationString = (String) importHashMap.get(LOCATION_KEY);

            this.setName((String) importHashMap.get(NAME_KEY))
                    .setVersion((String) importHashMap.get(VERSION_KEY))
                    .setLocation(Paths.get(locationString))
                    .setVars((LinkedHashMap<String, String>) importHashMap.get(VARS_KEY));

            return this;
        }

        public Pattern build() {
            return new Pattern(patternTmp.name, patternTmp.version, patternTmp.vars);
        }

        public PatternBuilder setName(String name) {
            patternTmp.name = Optional.ofNullable(patternTmp.name).orElse(name);
            return this;
        }

//        public PatternBuilder setSwapName(String swapName) {
//            patternTmp.name = Optional.ofNullable(patternTmp.name).orElse(swapName);
//            return this;
//        }

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
