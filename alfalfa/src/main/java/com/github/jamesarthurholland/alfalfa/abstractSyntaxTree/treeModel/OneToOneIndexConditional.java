package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel;

import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.Container;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Mapping;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Variable;
import com.github.jamesarthurholland.alfalfa.transpiler.FoldableEvaluator;
import com.github.jamesarthurholland.alfalfa.transpiler.SentenceIndexEvaluator;
import com.github.jamesarthurholland.alfalfa.transpiler.SentenceSingleEvaluator;

import java.util.ArrayList;

public class OneToOneIndexConditional extends Foldable implements FoldableEvaluator
{
    public OneToOneIndexConditional() {
        super(Type.ONE_TO_ONE_CONDITIONAL);
    }

    public OneToOneIndexConditional(Node left, Node right, boolean isRightTrueFixed) {
        super(Type.ONE_TO_ONE_CONDITIONAL);
        this.left = left;
        this.right = right;
        this.rightNodeFixed = isRightTrueFixed;
    }

    public OneToOneIndexConditional(OneToOneIndexConditional other) {
        super(Type.ONE_TO_ONE_CONDITIONAL);

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

        if(mapping.getType() == Mapping.Type.ONE_TO_ONE) {
            SentenceIndexEvaluator evaluator = new SentenceIndexEvaluator(var, info, mapping);
            Container newContainer = (Container) this.context.container.clone();
            Context context = new Context(newContainer, evaluator);
            OneToOneIndexConditional copy = new OneToOneIndexConditional(this);
            ArrayList<Node> sentenceEvaluatorNodes = addContextToChildNodes(copy, context);
            nodes.addAll(sentenceEvaluatorNodes);
        }

        return nodes;
    }
}
