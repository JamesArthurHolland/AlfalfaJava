package com.github.jamesarthurholland.alfalfa.configurationBuilder;

import com.github.jamesarthurholland.alfalfa.model.Variable;

public class KeyVariableConditional extends VariableConditional {
    public static final String TOKEN = "KEY";

    public KeyVariableConditional() {
        super(TOKEN);
    }

    @Override
    boolean isVariableOfConditional(Variable variable) {
        return variable.isPrimary();
    }
}
