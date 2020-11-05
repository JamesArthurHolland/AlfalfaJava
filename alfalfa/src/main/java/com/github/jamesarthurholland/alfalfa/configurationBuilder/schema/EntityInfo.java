package com.github.jamesarthurholland.alfalfa.configurationBuilder.schema;

import java.util.*;

public class EntityInfo implements Cloneable
{
    protected String name;
    protected String nameSpace;
    protected Optional<String> tableName;
    protected LinkedHashSet<Variable> variables = new LinkedHashSet<>();

    public EntityInfo(String entity, String tableName, LinkedHashSet<Variable> variables) {
        this.name = entity;
        this.tableName = Optional.ofNullable(tableName);
        this.variables = variables;
    }

    public EntityInfo(String entity, String nameSpace, String tableName, LinkedHashSet<Variable> variables) { // TODO remove
        this.name = entity;
        this.nameSpace = nameSpace;
        this.tableName = Optional.ofNullable(tableName);
        this.variables = variables;
    }

    @Override
    public Object clone() {

        variables = new LinkedHashSet<>();

        this.variables.forEach(variable -> {
            this.variables.add(new Variable(variable));
        });

        return new EntityInfo(this.name, this.nameSpace, this.tableName.get(), variables);
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
        if (Arrays.asList(variables.toArray()).get(sizeOfVariableList - 1).equals(var)) {
            return true;
        }
        return false;
    }

    public String getFullyQualifedName()
    {
        return nameSpace + "." + name;
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
                && tableName.equals(entityInfo.tableName)
                && nameSpace.equals(entityInfo.nameSpace)
                && variables.equals(entityInfo.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, nameSpace, tableName, variables);
    }

    public Set<Variable> getVariables()
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

    public Optional<String> getTableName() {
        return tableName;
    }
}
