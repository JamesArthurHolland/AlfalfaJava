package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

public class FoldableFactory
{
    public static Foldable newFoldable(Foldable.Types type) {
        switch (type) {
            case VAR_LOOP:
                return new VarLoop();
            case ENTITY_LOOP:
                return new EntityLoop();
            case INDICES_LOOP:
                return new IndicesLoop();
            case VAR_KEY_CONDITIONAL:
                // TODO
                break;
        }
        return null;
    }


}