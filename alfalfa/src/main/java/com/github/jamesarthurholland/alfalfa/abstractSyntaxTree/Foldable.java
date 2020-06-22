package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import java.util.LinkedHashMap;

public class Foldable extends Node
{
    public static enum Types {
        VAR_LOOP,
        ENTITY_LOOP,
        VAR_CONDITIONAL
    }

    public static final LinkedHashMap<Types, String> FOLDABLE_KEYS = new LinkedHashMap<>();

    static {
        FOLDABLE_KEYS.put(Types.VAR_LOOP, keyToLoopOpener("VARS"));
        FOLDABLE_KEYS.put(Types.VAR_LOOP, keyToLoopOpener("ENTITIES"));
    }

    private boolean leftTreeFixed;
    public static Types type;

    public Foldable(Types type)
    {
        this.type = type;
        leftTreeFixed = false;
    }

    public static String keyToLoopOpener(String word) {
        return "^\\s*\\{\\{" + word + "\\}\\}\\s*$";
    }

    public boolean isLeftTreeFixed()
    {
        return leftTreeFixed;
    }

    public void fixLeftTree() {
        leftTreeFixed = true;
    }

    @Override
    public void print() {

    }

    @Override
    public Types getType() {
        return null;
    }

}
