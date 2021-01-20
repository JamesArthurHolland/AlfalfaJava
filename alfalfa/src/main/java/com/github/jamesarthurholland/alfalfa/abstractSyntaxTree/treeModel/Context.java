package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel;

import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.Container;
import com.github.jamesarthurholland.alfalfa.transpiler.SentenceEvaluator;

import java.util.Map;

public class Context implements Cloneable
{
    protected Container container;
    protected SentenceEvaluator evaluator;

    public Context(Container container, SentenceEvaluator evaluator) {
        this.container = container;
        this.evaluator = evaluator;
    }

    @Override
    protected Object clone() {
        Container newContainer = new Container();
        for (Map.Entry<String, Cloneable> entry : container.entrySet()) {
            String key = entry.getKey();
            Cloneable value = entry.getValue();

            newContainer.put(key, value);
        }
        return new Context(newContainer, evaluator);
    }

}
