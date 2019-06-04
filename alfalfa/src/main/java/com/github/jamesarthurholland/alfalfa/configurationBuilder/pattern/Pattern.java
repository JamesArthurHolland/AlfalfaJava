package com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern;

import com.github.jamesarthurholland.alfalfa.FileUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.util.Objects.requireNonNull;

public class Pattern {
    public static String ALFALFA_FILE = "AlfalfaFile";
    public static String NAME_KEY = "name";
    public static String SWAP_NAME_KEY = "swap";
    public static String VERSION_KEY = "version";
    public static String IMPORTS_KEY = "imports";
    public static String VARS_KEY = "vars";

    public String name;
    public String version;
    public ArrayList<ImportDetail> imports;
//    public HashMap<String, Pattern> imports;
    public HashMap<String, String> vars = new HashMap<>();



    public Pattern() {}

    public Pattern(String name, String version, ArrayList<ImportDetail> imports, HashMap<String, String> vars) {
        this.name = requireNonNull(name, "The argument 'name' must not be null.");
        this.version = requireNonNull(version, "The argument 'version' must not be null.");
        this.imports = imports;
        this.vars = vars;
    }

    public Pattern(Path workingDirectory)
    {
        Path alfalfaFile = workingDirectory.resolve(Pattern.ALFALFA_FILE);
//        Pattern pattern = null;
        if(Files.exists(alfalfaFile)) {
            try {
                BufferedReader reader = Files.newBufferedReader(alfalfaFile);
    //            Yaml object = new Yaml(new Constructor(Pattern.class));
//                Pattern pattern = (Pattern) new Yaml(new Constructor(Pattern.class)).load(reader);



                Map<String, Object> patternYamlObject = (Map<String, Object>) new Yaml().load(reader);
                String name = (String) patternYamlObject.get(NAME_KEY);
                String version = (String) patternYamlObject.get(VERSION_KEY);
                ArrayList<LinkedHashMap<String, String>> imports = (ArrayList<LinkedHashMap<String, String>>) patternYamlObject.get(IMPORTS_KEY);
                for(LinkedHashMap<String, String> importHashMap : imports) {
    //                // TODO get pattern from file
                    ImportDetail importDetail = new ImportDetail(importHashMap);
                    Path patternPath = importDetail.getPatternPath();

    //                this.imports.put()
                }
    ////            pattern = yaml.load(reader);
    //            pattern = new Pattern(name, version, imports);
                System.out.println("AlfalfaFile scan");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private static class ImportDetail {
        public Pattern original;
        public Pattern swap;
        public String outputLocation;

        public ImportDetail(LinkedHashMap<String, String> importHashMap)
        {
            String version = importHashMap.get(VERSION_KEY);
            swap = new Pattern(FileUtils.patternPath(importHashMap.get(SWAP_NAME_KEY), version)); // TODO add vars for swap here. Pass original Object to get its defaults
            if(swap == null) {

            }
            original = new Pattern(FileUtils.patternPath(importHashMap.get(NAME_KEY), importHashMap.get(VERSION_KEY)));


            Yaml yaml = new Yaml();
            StringWriter writer = new StringWriter();
            yaml.dump(importHashMap, writer);
            ImportDetail load = new Yaml(new Constructor(ImportDetail.class)).load(writer.toString());
        }

        public ImportDetail(Pattern original, Pattern swap, ArrayList<ImportDetail> imports, HashMap<String, String> vars) {
            this.swap = swap;
        }

        public Path getPatternPath() {
            if(swap == null) {
                return FileUtils.patternPath(original.name, original.version);
            }
            return FileUtils.patternPath(swap.name, swap.version);
        }
    }
}
