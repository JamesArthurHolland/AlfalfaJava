package com.github.jamesarthurholland.alfalfa.configurationBuilder.schema;

import java.util.Objects;

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

    protected Type type;

    public Mapping(String parentEntityName, String childEntityName, String fromVarName, String toVarName, Type type) {
        this.parentEntityName = parentEntityName;
        this.childEntityName = childEntityName;
        this.fromVarName = fromVarName;
        this.toVarName = toVarName;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mapping mapping = (Mapping) o;
        return Objects.equals(parentEntityName, mapping.parentEntityName) &&
                Objects.equals(childEntityName, mapping.childEntityName) &&
                Objects.equals(fromVarName, mapping.fromVarName) &&
                Objects.equals(toVarName, mapping.toVarName) &&
                type == mapping.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(parentEntityName, childEntityName, fromVarName, toVarName, type);
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
