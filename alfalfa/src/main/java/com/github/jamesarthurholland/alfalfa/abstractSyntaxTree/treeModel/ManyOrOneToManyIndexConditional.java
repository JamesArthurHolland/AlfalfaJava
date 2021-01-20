package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel;

import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.Container;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Mapping;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Variable;
import com.github.jamesarthurholland.alfalfa.transpiler.FoldableEvaluator;
import com.github.jamesarthurholland.alfalfa.transpiler.SentenceIndexEvaluator;

import java.util.ArrayList;

public class ManyOrOneToManyIndexConditional extends Foldable implements FoldableEvaluator
{
    public ManyOrOneToManyIndexConditional() {
        super(Type.MANY_OR_ONE_TO_MANY_CONDITIONAL);
    }

    public ManyOrOneToManyIndexConditional(Node left, Node right, boolean isRightTrueFixed) {
        super(Type.MANY_OR_ONE_TO_MANY_CONDITIONAL);
        this.left = left;
        this.right = right;
        this.rightNodeFixed = isRightTrueFixed;
    }

    public ManyOrOneToManyIndexConditional(ManyOrOneToManyIndexConditional other) {
        super(Type.MANY_OR_ONE_TO_MANY_CONDITIONAL);

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

        Mapping mapping = (Mapping) this.context.container.get(Container.MAPPING_KEY);
        EntityInfo info = (EntityInfo) this.context.container.get(Container.ENTITY_INFO_KEY);
        Variable var = (Variable) this.context.container.get(Container.VAR_KEY);

        if(mapping.getType() == Mapping.Type.MANY_TO_MANY || mapping.getType() == Mapping.Type.ONE_TO_MANY) {
            SentenceIndexEvaluator evaluator = new SentenceIndexEvaluator(var, info, mapping);
            Container newContainer = (Container) this.context.container.clone();
            Context context = new Context(newContainer, evaluator);
            ManyOrOneToManyIndexConditional copy = new ManyOrOneToManyIndexConditional(this);
            ArrayList<Node> sentenceEvaluatorNodes = addContextToChildNodes(this, context);
            nodes.addAll(sentenceEvaluatorNodes);
        }

        return nodes;
    }
}
