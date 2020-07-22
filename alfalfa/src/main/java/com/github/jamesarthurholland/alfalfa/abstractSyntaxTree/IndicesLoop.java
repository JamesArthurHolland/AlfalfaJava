package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Variable;
import com.github.jamesarthurholland.alfalfa.transpiler.FoldableEvaluator;
import com.github.jamesarthurholland.alfalfa.transpiler.SentenceIndexEvaluator;

import java.util.ArrayList;

public class IndicesLoop extends Foldable implements FoldableEvaluator
{
    public IndicesLoop() {
        super(Types.INDICES_LOOP);
    }

    public IndicesLoop(IndicesLoop other) {
        super(Types.INDICES_LOOP);

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
        EntityInfo info = (EntityInfo) container.get(TemplateParser.ENTITY_INFO_KEY);
        Schema schema = (Schema) container.get(TemplateParser.SCHEMA_KEY);

        Container finalContainer = container;
        info.getVariables()
            .forEach(var -> {
                if(isIndex(info, var, schema)) {
                    SentenceIndexEvaluator evaluator = new SentenceIndexEvaluator(var, info);
                    IndicesLoop copy = new IndicesLoop(this);
                    Context context = new Context(finalContainer, evaluator);
                    nodes.addAll(addContextToChildNodes(copy, context));
                }
            });

        return nodes;
    }

    protected boolean isIndex(EntityInfo info, Variable var, Schema schema) {
        return schema.getMappingForEntityVar(info, var).isPresent();
    }
}