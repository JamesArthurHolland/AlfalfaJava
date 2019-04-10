package com.github.jamesarthurholland.alfalfa;

import java.util.ArrayList;

class ConfigScanner
{



    private static boolean isVariableDefinitionSemanticallyCorrect() // TODO
    {
        return true;
//        return lineArray.length != 4
    }

    private static boolean isVariableDefinition(String[] lineArray) throws Exception
    {
        if(lineArray[0].equals("k") || lineArray[0].equals("v")) {
            if(isVariableDefinitionSemanticallyCorrect() == false) {
                throw new Exception("Variable definitions must be of the form: <k|v> <i|o|u> <var type> <var name>");
            }

            return true;
        }
        return false;
    }

    public Config.ConfigElement readConfigFromLines(ArrayList<String> entity)
    {
        EntityInfo entityInfo = new EntityInfo(); // TODO bad
//        ArrayList<String> name = fileToArrayList(fileName);
        boolean entityParsed = false;
        String entityName = "";
        ArrayList<Mapping> mappings = new ArrayList<>();

        for(String line : entity)
        {
            String[] lineArray = line.split("[ ]+");

            // Get entity name from given config file, if there is one.
            if(entityParsed == false)  {
                int lastDotIndex = line.lastIndexOf(".");
                entityName = line.substring( lastDotIndex + 1);
                if( lastDotIndex == -1 || ! entityName.matches("\\w+") ){
                    entityName = line;
                }
                else{
                    entityInfo.setNameSpace(line.substring(0, lastDotIndex));
                }

                entityInfo.setName(entityName);
                entityParsed = true;
                continue;
            }
            else {

                Variable var = new Variable();
    //            String chop = line;

                // Get the name's key variable ( id in most cases)
                if(line.startsWith("n"))  {
                    entityInfo.setNameSpace(lineArray[1]);
                    continue;
                }

                if(line.startsWith("-db"))  {
    //                entityInfo.setDbName(line.substring(4));
                    continue;
                }

                try {
                    if(ConfigScanner.isVariableDefinition(lineArray))  {
                        if(lineArray[0].equals("k")) { // TODO check for 2 keys.. composite key?
                            var.setPrimary(true);
                        }
                        else {
                            var.setPrimary(false);
                        }

                        if( ! (lineArray[1].equals("i") || lineArray[1].equals("o") || lineArray[1].equals("u")) ) {
                            throw new Exception("Second token on variable definition line must be < i (private) | o (protected)| u (public) >");
                        }
                        if(lineArray[1].equals("i")) {
                            var.setVisibility(Variable.PRIVATE);
                        }
                        if(lineArray[1].equals("o")) {
                            var.setVisibility(Variable.PROTECTED);
                        }
                        if(lineArray[1].equals("u")) {
                            var.setVisibility(Variable.PUBLIC);
                        }

                        var.setType(lineArray[2]);
                        String varName = lineArray[3];
                        var.setName(varName);

                        // Mapping
                        if( lineArray.length > 4 ) {
                            String mappingDefinition = lineArray[4];
                            String[] mappingKeyArray = mappingDefinition.split("->");
                            Mapping.Type mappingType;

                            if(mappingKeyArray[0].equals("121")) { // TODO error for unknown mapping code
                                mappingType = Mapping.Type.ONE_TO_ONE;
                            }

                            String mappingValue = mappingKeyArray[1]; // TODO error check mapping to

                            int mappingValueArrayLastDotIndex = mappingValue.lastIndexOf(".");
                            String mappedToEntity = mappingValue.substring(0, mappingValueArrayLastDotIndex);
                            String mappedToVariable = mappingValue.substring(mappingValueArrayLastDotIndex + 1);

                            Mapping mapping = new Mapping(entityInfo.getFullyQualifedName(), mappedToEntity, varName, mappedToVariable);

                            mappings.add(mapping);

                            System.out.println();
                        }

                        entityInfo.getVariables().add(var);
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return new Config.ConfigElement(entityInfo, mappings);
    }
}
