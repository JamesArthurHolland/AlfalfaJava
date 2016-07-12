import java.util.ArrayList;
import java.util.List;

public class EntityInfo
{
    protected String entity;
    protected String nameSpace;
    protected String dbName;
    protected List<Variable> variables = new ArrayList<Variable>();

    public String getPrimaryKey(EntityInfo info)
    {
        for(Variable var : info.getVariables()) {
        if(var.isPrimary()) {
            return var.getName();
        }
    }
        return "";
    }

    public boolean isVarLast(Variable var)
    {
        int sizeOfVariableList = variables.size();
        if (variables.get(sizeOfVariableList - 1).isEqualTo (var)) {
            return true;
        }
        return false;
    }

    public List<Variable> getVariables()
    {
        return variables;
    }

    public void setEntity(String givenEntity)
    {
        entity = givenEntity;
    }

    public String getEntity()
    {
        return entity;
    }

    public void setDbName(String db)
    {
        dbName = db;
    }

    public String getDbName()
    {
        return dbName;
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