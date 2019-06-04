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
    public String name;
    public String version;
    public ArrayList<Pattern> imports = new ArrayList<>();
//    public HashMap<String, Pattern> imports;
    public LinkedHashMap<String, String> vars = new LinkedHashMap<>();
    public Path outputLocation;


    public Pattern() {}

    public Pattern(String name, String version, LinkedHashMap<String, String> vars) {
        this.name = requireNonNull(name, "The argument 'name' must not be null.");
        this.version = requireNonNull(version, "The argument 'version' must not be null.");
        this.vars = vars;
    }

    public Pattern(String name, String version, ArrayList<Pattern> imports, LinkedHashMap<String, String> vars, Pattern swap) {
        this.name = requireNonNull(name, "The argument 'name' must not be null.");
        this.version = requireNonNull(version, "The argument 'version' must not be null.");
        this.imports = imports;
        this.vars = vars;
    }

    public Pattern(Path workingDirectory)
    {

    }



//    private static class ImportDetail {
//        public Pattern original;
//        public Pattern swap;
//        public String outputLocation;
//
//        public ImportDetail(LinkedHashMap<String, String> importHashMap)
//        {
//
//        }
//
//        public ImportDetail(Pattern original, Pattern swap, ArrayList<ImportDetail> imports, HashMap<String, String> vars) {
//            this.swap = swap;
//        }
//
//        public Path getPatternPath() {
//            if(swap == null) {
//                return FileUtils.patternPath(original.name, original.version);
//            }
//            return FileUtils.patternPath(swap.name, swap.version);
//        }
//    }
}
