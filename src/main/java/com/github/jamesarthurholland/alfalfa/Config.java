package com.github.jamesarthurholland.alfalfa;

import java.util.ArrayList;
import java.util.HashMap;

public class Config {
    private ArrayList<EntityInfo> entityInfo;
    private HashMap<String, ArrayList<Mapping>> mappingsForEntityName;

    public Config() {
        this.entityInfo = new ArrayList<>();
        this.mappingsForEntityName = new HashMap<>();
    }

    public Config(ArrayList<EntityInfo> info, ArrayList<Mapping> mappings) {
        this.entityInfo = info;
        this.mappingsForEntityName = new HashMap<>();
    }

    public void addElement(ConfigElement element) {
        this.entityInfo.add(element.getEntityInfo());
        for(Mapping mapping : element.getMappings()) {

            ArrayList<Mapping> fromEntityMappings = this.mappingsForEntityName.getOrDefault(mapping.fromEntityName, new ArrayList<>());
            fromEntityMappings.add(mapping);

            ArrayList<Mapping> toEntityMappings = this.mappingsForEntityName.getOrDefault(mapping.toEntityName, new ArrayList<>());
            toEntityMappings.add(mapping);

            this.mappingsForEntityName.put(
                mapping.fromEntityName,
                fromEntityMappings
            );
            this.mappingsForEntityName.put(
                mapping.toEntityName,
                toEntityMappings
            );
        }
    }

    public ArrayList<EntityInfo> getEntityInfo() {
        return entityInfo;
    }

    protected static class ConfigElement
    {
        private EntityInfo entityInfo;
        private ArrayList<Mapping> mappings;

        public ConfigElement(EntityInfo entityInfo, ArrayList<Mapping> mappings) {
            this.entityInfo = entityInfo;
            this.mappings = mappings;
        }

        public EntityInfo getEntityInfo() {
            return entityInfo;
        }

        public ArrayList<Mapping> getMappings() {
            return mappings;
        }
    }
}
