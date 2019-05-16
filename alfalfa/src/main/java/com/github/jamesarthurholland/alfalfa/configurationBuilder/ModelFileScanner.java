package com.github.jamesarthurholland.alfalfa.configurationBuilder;

import com.github.jamesarthurholland.alfalfa.model.EntityInfo;
import com.github.jamesarthurholland.alfalfa.model.Mapping;
import com.github.jamesarthurholland.alfalfa.model.Variable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
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

    public static HashMap<String, HashSet<Mapping>> readMappingsFromFile(Path path)
    {
        try {
            Optional<String> qualifiedNameOptional = Files.lines(path).findFirst();
            if(qualifiedNameOptional.isPresent()) {
                final String qualifiedName = qualifiedNameOptional.get();

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

                        return new Mapping(mappedFromEntity, qualifiedName, mappedFromVariable, varName, mappingType);
                    })
                    .collect(Collectors.toCollection(HashSet::new)); // TODO create entity at return statement, encapsulation

                HashMap<String, HashSet<Mapping>> mappingsForName = new HashMap<>();
                mappingsForName.put(qualifiedName, mappings);

                mappings.forEach(mapping -> {
                    String otherQualifedName = mapping.getParentEntityName();
                    if(mapping.getParentEntityName() == qualifiedName) {
                        otherQualifedName = mapping.getChildEntityName();
                    }
                    HashSet otherQualifiedNameMappings = mappingsForName.getOrDefault(otherQualifedName, new HashSet<>());
                    otherQualifiedNameMappings.add(mapping);

                    mappingsForName.put(otherQualifedName, otherQualifiedNameMappings);
                });

                return mappingsForName;
            }
        } catch (MalformedVariableLineException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static EntityInfo readConfigFromFile(Path path)
    {
        try {
            EntityInfo entityInfo = new EntityInfo(); // TODO bad
            String entityName = "";
            String namespace = "";
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
}
