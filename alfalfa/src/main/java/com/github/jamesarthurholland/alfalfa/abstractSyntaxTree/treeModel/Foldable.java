package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel;

import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.Container;
import com.github.jamesarthurholland.alfalfa.transpiler.FoldableEvaluator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public abstract class Foldable extends Node implements FoldableEvaluator
{
    public abstract ArrayList<Node> evaluate(Container baseContainer);

    public static final LinkedHashMap<Node.Type, Pattern> FOLDABLE_OPENERS = new LinkedHashMap<>();
    public static final LinkedHashMap<Node.Type, Pattern> FOLDABLE_CLOSERS = new LinkedHashMap<>();

    static {
        FOLDABLE_OPENERS.put(Node.Type.VAR_LOOP, keyToLoopOpener("VARS"));
        FOLDABLE_OPENERS.put(Node.Type.ENTITY_LOOP, keyToLoopOpener("ENTITIES"));
        FOLDABLE_OPENERS.put(Node.Type.VAR_KEY_CONDITIONAL, keyToLoopOpener("KEY"));

        FOLDABLE_OPENERS.put(Node.Type.CHILD_MAPPING_LOOP, keyToLoopOpener("MAPPINGS"));
        FOLDABLE_OPENERS.put(Node.Type.ONE_TO_ONE_CONDITIONAL, keyToLoopOpener("121"));
        FOLDABLE_OPENERS.put(Node.Type.ONE_TO_MANY_CONDITIONAL, keyToLoopOpener("12M"));
        FOLDABLE_OPENERS.put(Node.Type.MANY_OR_ONE_TO_MANY_CONDITIONAL, keyToLoopOpener("X2M"));
        FOLDABLE_OPENERS.put(Node.Type.MANY_TO_MANY_CONDITIONAL, keyToLoopOpener("M2M"));

        FOLDABLE_CLOSERS.put(Node.Type.VAR_LOOP, keyToLoopCloser("VARS"));
        FOLDABLE_CLOSERS.put(Node.Type.ENTITY_LOOP, keyToLoopCloser("ENTITIES"));
        FOLDABLE_CLOSERS.put(Node.Type.VAR_KEY_CONDITIONAL, keyToLoopCloser("KEY"));

        FOLDABLE_CLOSERS.put(Node.Type.CHILD_MAPPING_LOOP, keyToLoopCloser("MAPPINGS"));
        FOLDABLE_CLOSERS.put(Node.Type.ONE_TO_ONE_CONDITIONAL, keyToLoopCloser("121"));
        FOLDABLE_CLOSERS.put(Node.Type.ONE_TO_MANY_CONDITIONAL, keyToLoopCloser("12M"));
        FOLDABLE_CLOSERS.put(Node.Type.MANY_OR_ONE_TO_MANY_CONDITIONAL, keyToLoopCloser("X2M"));
        FOLDABLE_CLOSERS.put(Node.Type.MANY_TO_MANY_CONDITIONAL, keyToLoopCloser("M2M"));
    }

    protected boolean rightNodeFixed;

    public Foldable(Node.Type type)
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
