package com.github.jamesarthurholland.alfalfa.configurationBuilder;

import com.github.jamesarthurholland.alfalfa.StringUtils;
import com.github.jamesarthurholland.alfalfa.model.EntityInfo;
import com.github.jamesarthurholland.alfalfa.model.Mapping;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Config {
    private ArrayList<EntityInfo> entityInfoList;
    private HashMap<String, ArrayList<Mapping>> mappingsForEntityName;

    public static final String ALFALFA_DOT_FOLDER = ".alfalfa";

    public Config(Path workingDirectory) throws NoDotAlfalfaDirectoryException {
        // getMappingStrings

        if(!hasDotAlfalfaDirectory(workingDirectory)) {
            throw new NoDotAlfalfaDirectoryException();
        }

        Path dotAlfalfaPath = workingDirectory.resolve(ALFALFA_DOT_FOLDER);

        ArrayList<Config.Model> models = streamFilterModelFiles(dotAlfalfaPath)
            .map(EntityScanner::readConfigFromFile)
            .collect(Collectors.toCollection(ArrayList::new));

        // getModelFiles
        // map models to models
    }

    protected Stream<Path> streamFilterModelFiles(Path path)
    {
        try {
            Files.list(path)
                    .filter(StringUtils::fileIsModelFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Config(ArrayList<EntityInfo> info, ArrayList<Mapping> mappings) {
        this.entityInfoList = info;
        this.mappingsForEntityName = new HashMap<>();
    }

//    public void addElement(ConfigElement element) {
//        this.entityInfoList.add(element.getEntityInfo());
//        for(Mapping mapping : element.getMappings()) {
//
//            ArrayList<Mapping> fromEntityMappings = this.mappingsForEntityName.getOrDefault(mapping.getParentEntityName(), new ArrayList<>());
//            fromEntityMappings.add(mapping);
//
//            ArrayList<Mapping> toEntityMappings = this.mappingsForEntityName.getOrDefault(mapping.getChildEntityName(), new ArrayList<>());
//            toEntityMappings.add(mapping);
//
//            this.mappingsForEntityName.put( // TODO this is mad wrong. inserting into self
//                mapping.getParentEntityName(),
//                fromEntityMappings
//            );
//            this.mappingsForEntityName.put(
//                mapping.getChildEntityName(),
//                toEntityMappings
//            );
//        }
//    }

    public ArrayList<EntityInfo> getEntityInfo() {
        return entityInfoList;
    }

    public static boolean hasDotAlfalfaDirectory(Path path)
    {
        Path pathWithAlfalfa = path.resolve(ALFALFA_DOT_FOLDER);

        if( Files.exists(pathWithAlfalfa) && Files.isDirectory(pathWithAlfalfa)) {
            return true;
        }
        return false;
    }

    public static class Model
    {
        private EntityInfo entityInfo;
        private ArrayList <Mapping> mappings;


        public Model(EntityInfo entityInfo, ArrayList<Mapping> mappings) {
            this.entityInfo = entityInfo;
            this.mappings = mappings;
        }

        public EntityInfo getEntityInfo() {
            return entityInfo;
        }

    }
}
