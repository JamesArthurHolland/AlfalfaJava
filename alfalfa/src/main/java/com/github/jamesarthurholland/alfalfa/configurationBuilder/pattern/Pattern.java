package com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern;

import com.hubspot.jinjava.Jinjava;
import org.apache.commons.lang3.SerializationUtils;

import javax.inject.Inject;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static java.util.Objects.requireNonNull;

public class Pattern implements Serializable
{
    public String name;
    public String version;
    public ArrayList<Pattern> imports = new ArrayList<>();
//    public HashMap<String, Pattern> imports = new HashMap<>();
    public LinkedHashMap<String, String> folderSwaps = new LinkedHashMap<>();
    public LinkedHashMap<String, String> vars = new LinkedHashMap<>();
    public ArrayList<String> files = new ArrayList<>();
    public String outputPath;
    public String patternRepoPath;
    public ImportMode mode = ImportMode.FOR_EACH_ENTITY;

    public enum ImportMode {
        FOR_EACH_ENTITY,
        ONCE_FOR_ENTITY
    }


    public Pattern() {}

    public Pattern(
        String name,
        String version,
        LinkedHashMap<String, String> folderSwaps,
        LinkedHashMap<String, String> vars
    ) {
        this.name = requireNonNull(name, "The argument 'name' must not be null.");
        this.version = requireNonNull(version, "The argument 'version' must not be null.");
        this.folderSwaps = folderSwaps;
        this.vars = vars;
    }

    public Pattern(
        String name,
        String version,
        ArrayList<Pattern> imports,
        LinkedHashMap<String, String> folderSwaps,
        LinkedHashMap<String, String> vars
    ) {
        this.name = requireNonNull(name, "The argument 'name' must not be null.");
        this.version = requireNonNull(version, "The argument 'version' must not be null.");
        this.imports = imports;
        this.folderSwaps = folderSwaps;
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

}
