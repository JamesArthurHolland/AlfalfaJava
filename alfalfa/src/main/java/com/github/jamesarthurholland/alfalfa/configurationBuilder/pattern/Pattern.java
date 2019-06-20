package com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.FieldSerializer;
import com.github.jamesarthurholland.alfalfa.FileUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.name.Named;
import com.hubspot.jinjava.Jinjava;
import org.apache.commons.lang3.SerializationUtils;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.util.Objects.requireNonNull;

public class Pattern implements Serializable
{
    public String name;
    public String version;
    public ArrayList<Pattern> imports = new ArrayList<>();
//    public HashMap<String, Pattern> imports = new HashMap<>();
    public LinkedHashMap<String, String> vars = new LinkedHashMap<>();
    public ArrayList<String> files = new ArrayList<>();
    public String outputPath;
    public String patternRepoPath;
    public VariableMode mode = VariableMode.FOR_EACH;

    public enum VariableMode {
        FOR_EACH,
        SINGLE
    }


    public Pattern() {}

    public Pattern(String name, String version, LinkedHashMap<String, String> vars) {
        this.name = requireNonNull(name, "The argument 'name' must not be null.");
        this.version = requireNonNull(version, "The argument 'version' must not be null.");
        this.vars = vars;
    }

    public Pattern(String name, String version, ArrayList<Pattern> imports, LinkedHashMap<String, String> vars) {
        this.name = requireNonNull(name, "The argument 'name' must not be null.");
        this.version = requireNonNull(version, "The argument 'version' must not be null.");
        this.imports = imports;
        this.vars = vars;
    }


//    public Pattern(String name, String version, ArrayList<Pattern> imports, LinkedHashMap<String, String> vars, ArrayList<Path> files, Path outputPath, Path patternRepoPath, VariableMode mode) {
//        this.name = name;
//        this.version = version;
//        this.imports = imports;
//        this.vars = vars;
//        this.files = files;
//        this.outputPath = outputPath;
//        this.patternRepoPath = patternRepoPath;
//        this.mode = mode;
//    }


    public Path getOutputPath() {
        return Paths.get(outputPath);
    }

    public void setOutputPath(Path path) {
        this.outputPath = path.toString();
    }

    public Path getPatternRepoPath() {
        return Paths.get(patternRepoPath);
    }

    public void setPatternRepoPath(Path patternRepoPath) {
        this.patternRepoPath = patternRepoPath.toString();
    }

    @Inject
    public Pattern copy() {
//        Gson gson = new GsonBuilder()
//                .serializeNulls()
//                .create();
//        String jsonString = gson.toJson(this);


        return SerializationUtils.clone(this);
//        return gson.fromJson(gson.toJson(this), Pattern.class);
    }

    public String injectVarsToLine(String line) {
        Jinjava jinjava = new Jinjava();
        return jinjava.render(line, vars);
    }

//    private static class ImportDetail {
//        public Pattern original;
//        public Pattern swap;
//        public String outputPath;
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
