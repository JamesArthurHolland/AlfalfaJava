package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import java.util.LinkedHashMap;

public class Container extends LinkedHashMap<String, Cloneable> {
    public final static String ENTITY_INFO_KEY = "ENTITY_INFO";
    public final static String SCHEMA_KEY = "SCHEMA";
    public final static String MAPPING_KEY = "MAPPING";
    public final static String VAR_KEY = "VAR";
}
