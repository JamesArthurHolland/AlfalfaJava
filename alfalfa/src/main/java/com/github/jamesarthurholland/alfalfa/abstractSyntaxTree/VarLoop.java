package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;
import com.github.jamesarthurholland.alfalfa.transpiler.FoldableEvaluator;
import com.github.jamesarthurholland.alfalfa.transpiler.SentenceVarEvaluator;

import java.util.ArrayList;

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
    public ArrayList<Node> evaluate(Container container) {
        ArrayList<Node> nodes = new ArrayList<>();

        if(context != null) {
            container = context.container;
        }
        EntityInfo info = (EntityInfo) container.get(Container.ENTITY_INFO_KEY);

        Container finalContainer = container;
        info.getVariables()
            .forEach(var -> {
                SentenceVarEvaluator evaluator = new SentenceVarEvaluator(var, info);
                VarLoop copy = new VarLoop(this);
                Context context = new Context(finalContainer, evaluator);
                nodes.addAll(addContextToChildNodes(copy, context));
            });

        return nodes;
    }


}