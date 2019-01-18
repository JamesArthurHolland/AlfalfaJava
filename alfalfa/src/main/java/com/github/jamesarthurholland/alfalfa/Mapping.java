package com.github.jamesarthurholland.alfalfa;

public class Mapping {
    public enum Type {
        ONE_TO_ONE,
        ONE_TO_MANY,
        MANY_TO_ONE,
        MANY_TO_MANY
    }

    protected String fromEntityName;
    protected String toEntityName;

    protected String fromVarName;
    protected String toVarName;

    public Mapping(String fromEntityName, String toEntityName, String fromVarName, String toVarName) {
        this.fromEntityName = fromEntityName;
        this.toEntityName = toEntityName;
        this.fromVarName = fromVarName;
        this.toVarName = toVarName;
    }
}
