package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import com.github.jamesarthurholland.alfalfa.transpiler.FoldableEvaluator;
import com.github.jamesarthurholland.alfalfa.transpiler.SentenceSingleEvaluator;

import java.util.ArrayList;

public class EntityLoop extends Foldable implements FoldableEvaluator
{
    public EntityLoop() {
        super(Types.ENTITY_LOOP);
    }

    public EntityLoop(Node left, Node right, boolean isRightTrueFixed) {
        super(Types.ENTITY_LOOP);
        this.left = left;
        this.right = right;
        this.rightNodeFixed = isRightTrueFixed;
    }

    public EntityLoop(EntityLoop other) {
        super(Types.ENTITY_LOOP);

        if(other.left != null) {
            this.left = copy(other.left);
        }
        if(other.right != null) {
            this.right = copy(other.right);
        }
    }

//    @Override
//    protected Object clone() {
//
//        return new EntityLoop(
//            this.left.clone(),
//            this.right.clone(),
//            this.rightNodeFixed
//        );
//    }

    @Override
    public ArrayList<Node> evaluate(Container container) {
        ArrayList<Node> nodes = new ArrayList<>();

        Schema schema = (Schema) container.get(TemplateParser.SCHEMA_KEY);

        schema.getEntityInfo()
                .forEach(entityInfo -> {
                    SentenceSingleEvaluator evaluator = new SentenceSingleEvaluator(entityInfo);
                    Container newContainer = (Container) container.clone();
                    newContainer.put(TemplateParser.ENTITY_INFO_KEY, entityInfo);
                    Context context = new Context(newContainer, evaluator);
                    EntityLoop copy = new EntityLoop(this);
                    ArrayList<Node> sentenceEvaluatorNodes = addContextToChildNodes(copy, context);
                    nodes.addAll(sentenceEvaluatorNodes);
                });

        return nodes;
    }
}
