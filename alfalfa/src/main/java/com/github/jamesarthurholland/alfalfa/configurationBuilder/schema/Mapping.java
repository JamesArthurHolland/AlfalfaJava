package com.github.jamesarthurholland.alfalfa.configurationBuilder.schema;

import java.util.Objects;

public class Mapping implements Cloneable {
    public enum Type {
        ONE_TO_ONE,
        ONE_TO_MANY,
        MANY_TO_MANY
    }

    protected String parentEntityName;
    protected String childEntityName;

    protected String parentVarName;
    protected String childVarName;

    protected Type type;

    public Mapping(String parentEntityName, String childEntityName, String parentVarName, String childVarName, Type type) {
        this.parentEntityName = parentEntityName;
        this.childEntityName = childEntityName;
        this.parentVarName = parentVarName;
        this.childVarName = childVarName;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mapping mapping = (Mapping) o;
        return Objects.equals(parentEntityName, mapping.parentEntityName) &&
                Objects.equals(childEntityName, mapping.childEntityName) &&
                Objects.equals(parentVarName, mapping.parentVarName) &&
                Objects.equals(childVarName, mapping.childVarName) &&
                type == mapping.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentEntityName, childEntityName, parentVarName, childVarName, type);
    }

    @Override
    protected Object clone() {

        return new Mapping(
            this.parentEntityName,
            this.childEntityName,
            this.parentVarName,
            this.childVarName,
            this.type
        );
    }

    public String getParentEntityName() {
        return parentEntityName;
    }

    public String getChildEntityName() {
        return childEntityName;
    }

    public String getParentVarName() {
        return parentVarName;
    }

    public String getChildVarName() {
        return childVarName;
    }

    public Type getType() {
        return type;
    }
}
