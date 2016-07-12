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