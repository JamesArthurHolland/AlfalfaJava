package com.github.jamesarthurholland.alfalfa;

import java.util.Objects;

public class Variable
{
    protected boolean primary;
    protected String visibility;
    protected String type;
    protected String name;

    public Variable(){
    }

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
        return Objects.equals(primary, variable.primary)
                && Objects.equals(visibility, variable.visibility)
                && Objects.equals(type, variable.type)
                && Objects.equals(name, variable.name);
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
    public String toString()
    {
//            if(String.IsNullOrEmpty(type)) {
        return visibility + " $" + name;
//            }
//            return visibility + " " + type + " $" + name;
    }

    public boolean isPrimary()
    {
        return primary;
    }

    public void setPrimary(boolean givenPrimary)
    {
        primary = givenPrimary;
    }

    public boolean isEqualTo(Variable otherVar)
    {
        if (primary != otherVar.isPrimary()) {
            return false;
        }
        if (visibility != otherVar.getVisibility ()) {
            return false;
        }
        if (type != otherVar.getType ()) {
            if (type == null && otherVar.getType () == null) {
                return false;
            }
        }
        if (name != otherVar.getName ()) {
            return false;
        }
        return true;
    }

}
