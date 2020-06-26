package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.github.jamesarthurholland.alfalfa.transpiler.FoldableEvaluator;
import com.github.jamesarthurholland.alfalfa.transpiler.SentenceEvaluator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public abstract class Foldable extends Node implements FoldableEvaluator
{
    public abstract ArrayList<Node> evaluate(LinkedHashMap<String, Object> container);

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

    protected boolean rightNodeFixed;
    public final Types type;

    public Foldable(Types type)
    {
        this.type = type;
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

    public ArrayList<Node> addSentenceEvaluatorToLoopChildNodes(Foldable copy, SentenceEvaluator evaluator) {
        ArrayList<Node> nodes = new ArrayList<>();

        Sentence childNode = (Sentence) copy.right;

        while(childNode != null) {
            nodes.add(childNode);
            childNode.setEvaluator(evaluator);
            if(childNode.left != null) {
                childNode = (Sentence) childNode.left;
            }
            else {
                childNode = null;
            }
        }

        return nodes;
    }



}
