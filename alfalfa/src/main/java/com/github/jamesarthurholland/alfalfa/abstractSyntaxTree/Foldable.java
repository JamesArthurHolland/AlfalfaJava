package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.github.jamesarthurholland.alfalfa.transpiler.FoldableEvaluator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public abstract class Foldable extends Node implements FoldableEvaluator
{
    public abstract ArrayList<Node> evaluate(Container container);

    public static final LinkedHashMap<Types, Pattern> FOLDABLE_OPENERS = new LinkedHashMap<>();
    public static final LinkedHashMap<Types, Pattern> FOLDABLE_CLOSERS = new LinkedHashMap<>();

    static {
        FOLDABLE_OPENERS.put(Types.VAR_LOOP, keyToLoopOpener("VARS"));
        FOLDABLE_OPENERS.put(Types.ENTITY_LOOP, keyToLoopOpener("ENTITIES"));
        FOLDABLE_OPENERS.put(Types.VAR_CONDITIONAL, keyToLoopOpener("ENTITIES"));
        FOLDABLE_OPENERS.put(Types.INDICES_LOOP, keyToLoopOpener("INDICES"));

        FOLDABLE_CLOSERS.put(Types.VAR_LOOP, keyToLoopCloser("VARS"));
        FOLDABLE_CLOSERS.put(Types.ENTITY_LOOP, keyToLoopCloser("ENTITIES"));
        FOLDABLE_CLOSERS.put(Types.VAR_CONDITIONAL, keyToLoopCloser("ENTITIES"));
        FOLDABLE_CLOSERS.put(Types.INDICES_LOOP, keyToLoopCloser("INDICES"));
    }

    protected boolean rightNodeFixed;

    public Foldable(Types type)
    {
        super(type);
        rightNodeFixed = false;
    }

    public static Pattern keyToLoopOpener(String word) {
        return Pattern.compile("^\\s*\\{\\{" + word + "\\}\\}\\s*$");
    }

    public static Pattern keyToLoopCloser(String word) {
        return Pattern.compile("^\\s*\\{\\{/" + word + "\\}\\}\\s*$");
    }

    public boolean isRightNodeFixed()
    {
        return rightNodeFixed;
    }

    public void setRightNodeFixed() {
        rightNodeFixed = true;
    }

    @Override
    public void print() {}

    public ArrayList<Node> addContextToChildNodes(Foldable copy, Context context) {
        ArrayList<Node> nodes = new ArrayList<>();

        Node childNode = copy.right;

        while(childNode != null) {
            nodes.add(childNode);
            childNode.setContext(context);
            if(childNode.left != null) {
                childNode = childNode.left;
            }
            else {
                childNode = null;
            }
        }

        return nodes;
    }



}
