package com.github.jamesarthurholland.alfalfa.configurationBuilder.schema;

import java.util.Objects;

public class Variable
{
    public static final String PUBLIC = "public";
    public static final String PROTECTED = "protected";
    public static final String PRIVATE = "private";

    public static final String[] VariableConditionals = { // TODO should be enum
      "KEY",
      "121",
      ""
    };

    protected boolean primary;
    protected String visibility;
    protected String type;
    protected String name;

    public Variable(boolean primary, String visibility, String type, String name)
    {
        this.primary = primary;
        this.visibility = visibility;
        this.type = type;
        this.name = name;
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
        Variable variable = (Variable) o;
        // field comparison
        return primary == variable.primary
                && visibility.equals(variable.visibility)
                && type.equals(variable.type)
                && name.equals(variable.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primary, visibility, type, name);
    }

    public void setVisibility(String givenVisibility)
    {
        visibility = givenVisibility;
    }

    public String getVisibility()
    {
        return visibility;
    }

    public void setType(String givenType)
    {
        type = givenType;
    }

    public String getType()
    {
        return type;
    }

    public void setName(String givenName)
    {
        name = givenName;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String toString() {
        return visibility;
    }

    public boolean isPrimary()
    {
        return primary;
    }

    public void setPrimary(boolean givenPrimary)
    {
        primary = givenPrimary;
    }
}
