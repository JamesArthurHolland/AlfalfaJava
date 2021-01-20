package com.github.jamesarthurholland.alfalfa.transpiler;

import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.Container;
import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel.Context;
import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel.Node;

import java.util.ArrayList;

public interface FoldableEvaluator {
    ArrayList<Node> evaluate(Container baseContainer);
}
