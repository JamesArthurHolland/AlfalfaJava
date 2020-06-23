package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class Foldable extends Node
{
    public static enum Types {
        VAR_LOOP,
        ENTITY_LOOP,
        VAR_CONDITIONAL
    }

    public static final LinkedHashMap<Types, Pattern> FOLDABLE_OPENERS = new LinkedHashMap<>();
    public static final LinkedHashMap<Types, Pattern> FOLDABLE_CLOSERS = new LinkedHashMap<>();

    static {
        FOLDABLE_OPENERS.put(Types.VAR_LOOP, keyToLoopOpener("VARS"));
        FOLDABLE_OPENERS.put(Types.ENTITY_LOOP, keyToLoopOpener("ENTITIES"));
        FOLDABLE_OPENERS.put(Types.VAR_CONDITIONAL, keyToLoopOpener("ENTITIES"));

        FOLDABLE_CLOSERS.put(Types.VAR_LOOP, keyToLoopCloser("VARS"));
        FOLDABLE_CLOSERS.put(Types.ENTITY_LOOP, keyToLoopCloser("ENTITIES"));
        FOLDABLE_CLOSERS.put(Types.VAR_CONDITIONAL, keyToLoopCloser("ENTITIES"));


    }

    private boolean leftTreeFixed;
    public static Types type;

    public Foldable(Types type)
    {
        this.type = type;
        leftTreeFixed = false;
    }

    public static Pattern keyToLoopOpener(String word) {
        return Pattern.compile("^\\s*\\{\\{" + word + "\\}\\}\\s*$");
    }

    public static Pattern keyToLoopCloser(String word) {
        return Pattern.compile("^\\s*\\{\\{/" + word + "\\}\\}\\s*$");
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

}
