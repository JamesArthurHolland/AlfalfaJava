package com.github.jamesarthurholland.alfalfa.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntityInfo {
    protected String name;
    protected String nameSpace;
    protected List<Variable> variables = new ArrayList<>();
    protected List<Mapping> mappings = new ArrayList<>();

    public EntityInfo() {
    }

    public EntityInfo(String entity, List<Variable> variables) {
        this.name = entity;
        this.variables = variables;
    }

    public EntityInfo(String entity, String nameSpace, List<Variable> variables) { // TODO remove
        this.name = entity;
        this.nameSpace = nameSpace;
        this.variables = variables;
    }

    public String getPrimaryKey(EntityInfo info) {
        for (Variable var : info.getVariables()) {
            if (var.isPrimary()) {
                return var.getName();
            }
        }
        return "";
    }

    public boolean isVarLast(Variable var) {
        int sizeOfVariableList = variables.size();
        if (variables.get(sizeOfVariableList - 1).equals(var)) {
            return true;
        }
        return false;
    }

    public String getFullyQualifedName()
    {
        return nameSpace + "." + name;
    }

    public void addMapping(Mapping mapping) {
        mappings.add(mapping);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (getClass() != o.getClass()) {
            return false;
        }
        EntityInfo entityInfo = (EntityInfo) o;
        // field comparison
        return name.equals(entityInfo.name)
                && nameSpace.equals(entityInfo.nameSpace)
                && Objects.equals(variables, entityInfo.variables);
    }

    public List<Variable> getVariables()
    {
        return variables;
    }

    public void setName(String givenEntity)
    {
        name = givenEntity;
    }

    public String getName()
    {
        return name;
    }

    public void setNameSpace(String givenNameSpace)
    {
        nameSpace = givenNameSpace;
    }

    public String getNameSpace()
    {
        return nameSpace;
    }


}
