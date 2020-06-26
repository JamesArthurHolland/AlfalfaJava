package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.google.gson.Gson;

public class FoldableFactory
{
    public static Foldable newFoldable(Foldable.Types type) {
        switch (type) {
            case VAR_LOOP:
                return new VarLoop();
            case ENTITY_LOOP:
                return new EntityLoop();
            case VAR_CONDITIONAL:
                // TODO
                break;
        }
        return null;
    }


}