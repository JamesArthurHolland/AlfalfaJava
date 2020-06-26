package com.github.jamesarthurholland.alfalfa.transpiler;

import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public interface FoldableEvaluator {
    ArrayList<Node> evaluate(LinkedHashMap<String, Object> container);
}
