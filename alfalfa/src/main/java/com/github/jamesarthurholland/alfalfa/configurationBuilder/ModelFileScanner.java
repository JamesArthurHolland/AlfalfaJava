package com.github.jamesarthurholland.alfalfa.configurationBuilder;

import com.github.jamesarthurholland.alfalfa.model.EntityInfo;
import com.github.jamesarthurholland.alfalfa.model.Mapping;
import com.github.jamesarthurholland.alfalfa.model.Variable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class ModelFileScanner
{
    public static final HashMap<String, String> visibilityForLetterMap = new HashMap<>();

    static {
        visibilityForLetterMap.put("i", Variable.PRIVATE);
        visibilityForLetterMap.put("o", Variable.PROTECTED);
        visibilityForLetterMap.put("u", Variable.PUBLIC);
    }


    private static boolean isVariableDefinition(String[] lineArray) throws MalformedVariableLineException
    {
        if(! (lineArray[0].equals("k") || lineArray[0].equals("v")) ) {
            throw new MalformedVariableLineException();
        }
        if( ! (lineArray[1].equals("i") || lineArray[1].equals("o") || lineArray[1].equals("u")) ) {
            throw new MalformedVariableLineException();
        }

        return true;
    }

    public static boolean isMappingLine(String[] lineArray) {
        return lineArray.length > 4;
    }


    public static ModelFileScan readModelFile(Path path) {
        EntityInfo entityInfo = readEntityInfoFromFile(path);
        HashMap<String, HashSet<Mapping>> mappingsForName = readMappingsFromFile(path);

        return new ModelFileScan(entityInfo, mappingsForName);
    }

    public static HashMap<String, HashSet<Mapping>> readMappingsFromFile(Path path)
    {
        try {
            Optional<String> qualifiedNameOptional = Files.lines(path).findFirst();
            if(qualifiedNameOptional.isPresent()) {
                final String qualifiedName = qualifiedNameOptional.get();

                HashMap<String, HashSet<Mapping>> mappingsForName = new HashMap<>();

                HashSet<Mapping> mappings = Files.lines(path)
                    .skip(1)
                    .map(line -> line.split("[ ]+"))
                    .filter(ModelFileScanner::isVariableDefinition)
                    .filter(ModelFileScanner::isMappingLine)
                    .map(lineArray -> {
                        String varName = lineArray[3];

                        String mappingDefinition = lineArray[4];
                        String[] mappingKeyArray = mappingDefinition.split("<-");
                        Mapping.Type mappingType = Mapping.Type.ONE_TO_ONE;

                        if(mappingKeyArray[0].equals("121")) { // TODO error for unknown mapping code
                            mappingType = Mapping.Type.ONE_TO_ONE;
                        }

                        String mappingValue = mappingKeyArray[1]; // TODO error check mapping to

                        int mappingValueArrayLastDotIndex = mappingValue.lastIndexOf(".");
                        String mappedFromEntity = mappingValue.substring(0, mappingValueArrayLastDotIndex);
                        String mappedFromVariable = mappingValue.substring(mappingValueArrayLastDotIndex + 1);


                        Mapping mapping = new Mapping(mappedFromEntity, qualifiedName, mappedFromVariable, varName, mappingType); // TODO create constructor that takes linearray

                        HashSet<Mapping> otherQualifiedNameMappings = mappingsForName.getOrDefault(mappedFromEntity, new HashSet<>()); // TODO can the following 2 lines be 1 line? yes if getOrDefault creates key
                        otherQualifiedNameMappings.add(mapping);
                        mappingsForName.put(mappedFromEntity, otherQualifiedNameMappings);

                        return mapping;
                    })
                    .collect(Collectors.toCollection(HashSet::new)); // TODO add to hashset same as the "other entity"


                mappingsForName.put(qualifiedName, mappings);


                return mappingsForName;
            }
        } catch (MalformedVariableLineException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static EntityInfo readEntityInfoFromFile(Path path)
    {
        try {
            String entityName = ""; // TODO consider name object to embed in entityinfo
            String namespace = "";

            Optional<String> qualifiedNameOptional = Files.lines(path).findFirst();

            if(qualifiedNameOptional.isPresent()) {
                String qualifiedName = qualifiedNameOptional.get();
                int lastDotIndex = qualifiedName.lastIndexOf(".");
                entityName = qualifiedName.substring( lastDotIndex + 1);
                if( lastDotIndex == -1 || ! entityName.matches("\\w+") ){
                    entityName = qualifiedName;
                }
                else{
                    namespace = qualifiedName.substring(0, lastDotIndex);
                }
            }

            ArrayList<Variable> variables = Files.lines(path)
                .skip(1)
                .map(line -> line.split("[ ]+"))
                .filter(ModelFileScanner::isVariableDefinition)
                .map(lineArray -> {
                    boolean varIsPrimary = lineArray[0].equals("k"); // TODO check for 2 keys.. composite key?

                    String visibilityCode = lineArray[1];
                    String visibility = visibilityForLetterMap.get(visibilityCode);

                    String varType = lineArray[2];
                    String varName = lineArray[3];

                    return new Variable(varIsPrimary, visibility, varType, varName);
                })
                .collect(Collectors.toCollection(ArrayList::new)); // TODO create entity at return statement, encapsulation


            return new EntityInfo(entityName, namespace, variables);
        } catch (MalformedVariableLineException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class ModelFileScan
    {
        private EntityInfo entityInfo;
        private Map<String, HashSet<Mapping>> mappingsForName;


        public ModelFileScan(EntityInfo entityInfo, HashMap<String, HashSet<Mapping>> mappings) {
            this.entityInfo = entityInfo;
            this.mappingsForName = mappings;
        }

        public EntityInfo getEntityInfo() {
            return entityInfo;
        }

        public Map<String, HashSet<Mapping>> getMappings() {
            return mappingsForName;
        }
    }
}
