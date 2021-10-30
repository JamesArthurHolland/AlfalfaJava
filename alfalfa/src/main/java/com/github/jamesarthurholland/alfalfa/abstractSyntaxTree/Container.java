package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import java.util.LinkedHashMap;

public class Container extends LinkedHashMap<String, Cloneable> {
    public final static String ENTITY_INFO_KEY = "ENTITY_INFO";
    public final static String SCHEMA_KEY = "SCHEMA";
    public final static String MAPPING_KEY = "MAPPING";
    public final static String VAR_KEY = "VAR";
    public final static String IS_IN_VAR_LOOP = "IS_IN_VAR_LOOP";

    public static class VarLoopStatus implements Cloneable {
        private boolean isInVarLoop;

        public VarLoopStatus(boolean isInVarLoop) {
            this.isInVarLoop = isInVarLoop;
        }

        public boolean isInVarLoop() {
            return isInVarLoop;
        }
    }

//    public static Container clone(Container other) {
//        Container newContainer = new Container();
//        for (String key : other.keySet())
//        {
//            switch (key) {
//                case ENTITY_INFO_KEY:
//
//                    newContainer.put(key, object.clone());
//            }
//            Cloneable object = other.get(key);
//            newContainer.put(key, object.clone());
//        }
//
//        return newContainer;
//    }
}
