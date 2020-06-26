package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;
import com.github.jamesarthurholland.alfalfa.transpiler.FoldableEvaluator;
import com.github.jamesarthurholland.alfalfa.transpiler.SentenceVarEvaluator;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class VarLoop extends Foldable implements FoldableEvaluator
{
    public VarLoop() {
        super(Types.VAR_LOOP);
    }

    public VarLoop(VarLoop other) {
        super(Types.VAR_LOOP);

        if(other.left != null) {
            this.left = copy(other.left);
        }
        if(other.right != null) {
            this.right = copy(other.right);
        }
    }

    @Override
    public ArrayList<Node> evaluate(LinkedHashMap<String, Object> container) {
        ArrayList<Node> nodes = new ArrayList<>();

        EntityInfo info = (EntityInfo) container.get(TemplateParser.ENTITY_INFO_KEY);

        info.getVariables()
            .forEach(var -> {
                SentenceVarEvaluator evaluator = new SentenceVarEvaluator(var, info);
                VarLoop copy = new VarLoop(this);
                nodes.addAll(addSentenceEvaluatorToLoopChildNodes(copy, evaluator));
            });

        return nodes;
    }


}