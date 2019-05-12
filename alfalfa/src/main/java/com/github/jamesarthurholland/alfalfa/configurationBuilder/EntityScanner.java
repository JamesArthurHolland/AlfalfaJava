package com.github.jamesarthurholland.alfalfa.configurationBuilder;

import com.github.jamesarthurholland.alfalfa.model.EntityInfo;
import com.github.jamesarthurholland.alfalfa.model.Mapping;
import com.github.jamesarthurholland.alfalfa.model.Variable;
import com.google.common.base.Charsets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;

public class EntityScanner
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



    public static EntityInfo readConfigFromFile(Path path)
    {
        try {
//            ArrayList<String> entity = (ArrayList) Files.readLines(path, Charsets.UTF_8);
            EntityInfo entityInfo = new EntityInfo(); // TODO bad
//        ArrayList<String> name = fileToArrayList(fileName);
            boolean entityParsed = false;
            String entityName = "";
            ArrayList<Mapping> mappings = new ArrayList<>();

            Optional<String> qualifiedNameOptional = Files.lines(path).findFirst();
            if(qualifiedNameOptional.isPresent()) {
                String qualifiedName = qualifiedNameOptional.get();
                int lastDotIndex = qualifiedName.lastIndexOf(".");
                entityName = qualifiedName.substring( lastDotIndex + 1);
                if( lastDotIndex == -1 || ! entityName.matches("\\w+") ){
                    entityName = qualifiedName;
                }
                else{
                    entityInfo.setNameSpace(qualifiedName.substring(0, lastDotIndex));
                }
                entityInfo.setName(entityName);
            }



            Files.lines(path)
                .skip(1)
                .map(line -> line.split("[ ]+"))
                .forEach(lineArray -> {
                    Variable var = new Variable();

                    try {
                        if(EntityScanner.isVariableDefinition(lineArray))  {
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
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }); // TODO create entity at return statement, encapsulation

            return entityInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
