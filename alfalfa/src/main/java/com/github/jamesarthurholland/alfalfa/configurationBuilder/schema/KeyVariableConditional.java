package com.github.jamesarthurholland.alfalfa.configurationBuilder.schema;

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
