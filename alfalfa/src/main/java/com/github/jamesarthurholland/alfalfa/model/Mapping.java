package com.github.jamesarthurholland.alfalfa.model;

public class Mapping {
    public enum Type {
        ONE_TO_ONE,
        ONE_TO_MANY,
        MANY_TO_ONE,
        MANY_TO_MANY
    }

    protected String parentEntityName;
    protected String childEntityName;

    protected String fromVarName;
    protected String toVarName;

    public Mapping(String parentEntityName, String childEntityName, String fromVarName, String toVarName) {
        this.parentEntityName = parentEntityName;
        this.childEntityName = childEntityName;
        this.fromVarName = fromVarName;
        this.toVarName = toVarName;
    }

    public String getParentEntityName() {
        return parentEntityName;
    }

    public String getChildEntityName() {
        return childEntityName;
    }

    public String getFromVarName() {
        return fromVarName;
    }

    public String getToVarName() {
        return toVarName;
    }
}
