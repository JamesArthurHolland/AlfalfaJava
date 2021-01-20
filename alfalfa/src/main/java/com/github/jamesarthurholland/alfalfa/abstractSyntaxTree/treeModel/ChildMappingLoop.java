package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel;

import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.Container;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Mapping;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Variable;
import com.github.jamesarthurholland.alfalfa.transpiler.FoldableEvaluator;
import com.github.jamesarthurholland.alfalfa.transpiler.SentenceIndexEvaluator;

import java.util.ArrayList;
import java.util.Optional;

public class ChildMappingLoop extends Foldable implements FoldableEvaluator
{
    public ChildMappingLoop() {
        super(Type.CHILD_MAPPING_LOOP);
    }

    public ChildMappingLoop(ChildMappingLoop other) {
        super(Type.CHILD_MAPPING_LOOP);

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
        Schema schema = (Schema) finalContainer.get(Container.SCHEMA_KEY);

        info.getVariables()
            .forEach(var -> {
                Optional<Mapping> mappingOptional = schema.getMappingsWhenIsChildForEntityVar(info, var);
                if(mappingOptional.isPresent()) {
                    SentenceIndexEvaluator evaluator = new SentenceIndexEvaluator(var, info);
                    ChildMappingLoop copy = new ChildMappingLoop(this);
                    Container newContainer = (Container) finalContainer.clone();
                    newContainer.put(Container.VAR_KEY, var);
                    newContainer.put(Container.MAPPING_KEY, mappingOptional.get());
                    Context context = new Context(newContainer, evaluator);
                    nodes.addAll(addContextToChildNodes(copy, context));
                }
            });

        return nodes;
    }

}