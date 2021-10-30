package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel;

import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.Container;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;
import com.github.jamesarthurholland.alfalfa.transpiler.FoldableEvaluator;
import com.github.jamesarthurholland.alfalfa.transpiler.SentenceVarEvaluator;

import java.util.ArrayList;

public class VarLoop extends Foldable implements FoldableEvaluator
{
    public VarLoop() {
        super(Type.VAR_LOOP);
    }

    public VarLoop(VarLoop other) {
        super(Type.VAR_LOOP);

        if(other.left != null) {
            this.left = copy(other.left);
        }
        if(other.right != null) {
            this.right = copy(other.right);
        }
    }

    @Override
    public ArrayList<Node> evaluate(Container baseContainer) {
        ArrayList<Node> nodes = new ArrayList<>();


        Container finalContainer;
        if(this.context != null){
            finalContainer = this.context.container;
        }
        else {
            finalContainer = baseContainer;
        }
        EntityInfo info = (EntityInfo) finalContainer.get(Container.ENTITY_INFO_KEY);

        info.getVariables()
            .forEach(var -> {
                SentenceVarEvaluator evaluator = new SentenceVarEvaluator(var, info);
                VarLoop copy = new VarLoop(this);
                Container newContainer = (Container) finalContainer.clone();
                newContainer.put(Container.IS_IN_VAR_LOOP, new Container.VarLoopStatus(true));
                Context context = new Context(newContainer, evaluator);
                nodes.addAll(addContextToChildNodes(copy, context));
            });

        return nodes;
    }


}