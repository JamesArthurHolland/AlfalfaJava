package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel.*;

public class FoldableFactory
{
    public static Foldable newFoldable(Node.Type type) {
        switch (type) {
            case VAR_LOOP:
                return new VarLoop();
            case ENTITY_LOOP:
                return new EntityLoop();
            case CHILD_MAPPING_LOOP:
                return new ChildMappingLoop();
            case VAR_KEY_CONDITIONAL:
                // TODO
                break;
            case ONE_TO_ONE_CONDITIONAL:
                return new OneToOneIndexConditional();
            case ONE_TO_MANY_CONDITIONAL:
                return new OneToManyIndexConditional();
            case MANY_OR_ONE_TO_MANY_CONDITIONAL:
                return new ManyOrOneToManyIndexConditional();
        }
        return null;
    }


}