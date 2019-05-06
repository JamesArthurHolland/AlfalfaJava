package com.github.jamesarthurholland.alfalfa.configurationBuilder;

import com.github.jamesarthurholland.alfalfa.StringUtils;
import com.github.jamesarthurholland.alfalfa.model.EntityInfo;
import com.github.jamesarthurholland.alfalfa.model.Mapping;
import com.google.common.io.Files;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Config {
    private ArrayList<EntityInfo> entityInfoList;
    private HashMap<String, ArrayList<Mapping>> mappingsForEntityName;

    public Config(File[] modelFiles) {
        // getMappingStrings

        ArrayList<Config.Model> models = streamFilterModelFiles(modelFiles)
            .map(ModelScanner::readConfigFromFile)
            .collect(Collectors.toCollection(ArrayList::new));

        // getModelFiles
        // map models to models
    }

    protected Stream<File> streamFilterModelFiles(File [] allFiles)
    {
        return Arrays.stream(allFiles)
                .filter(StringUtils::fileIsModelFile);
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

    protected static class Model
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
