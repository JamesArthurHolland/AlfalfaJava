package com.github.jamesarthurholland.alfalfa.configurationBuilder.schema;

import com.github.jamesarthurholland.alfalfa.StringUtils;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.NoDotAlfalfaDirectoryException;
import com.github.jamesarthurholland.alfalfa.typeSystem.TypeSystemConverter;
import one.util.streamex.EntryStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.ModelFileScanner.readModelFile;

public class Schema implements Cloneable
{
    private HashSet<EntityInfo> entityInfoList = new HashSet<>();
    private Map<String, HashSet<Mapping>> mappingsForEntityName = new HashMap<>();

    public static final String ALFALFA_DOT_FOLDER = "alfalfa";

    public Schema(HashSet<EntityInfo> entityInfoList, Map<String, HashSet<Mapping>> mappingsForEntityName) {
        this.entityInfoList = entityInfoList;
        this.mappingsForEntityName = mappingsForEntityName;
    }

    public Schema(Path workingDirectory) throws NoDotAlfalfaDirectoryException {
        // getMappingStrings

        if(!hasDotAlfalfaDirectory(workingDirectory)) {
            throw new NoDotAlfalfaDirectoryException();
        }

        Path dotAlfalfaPath = workingDirectory.resolve(ALFALFA_DOT_FOLDER);


        streamFilterModelFiles(dotAlfalfaPath)
            .map(path -> readModelFile(path))
            .forEach(modelFileScan -> {
                mappingsForEntityName = EntryStream.of(modelFileScan.getMappings())
                        .append(EntryStream.of(mappingsForEntityName))
                        .toMap((e1, e2) -> e1);

                entityInfoList.add(modelFileScan.getEntityInfo());
            });

        System.out.println("Schema constructor DBG");
        // getModelFiles
        // map models to models
    }

    protected Stream<Path> streamFilterModelFiles(Path path)
    {
        try {
            return Files.list(path)
                    .filter(StringUtils::fileIsModelFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // TODO wrong I think
    }

    public Optional<Mapping> getMappingsWhenIsChildForEntityVar(EntityInfo info, Variable var) {
        Optional<Mapping> mapping = this.mappingsForEntityName.get(info.getFullyQualifedName())
                .stream()
                .filter(current -> current.childVarName.equals(var.getName()))
                .findFirst();

        if (mapping.isPresent() == false) {
            mapping = this.mappingsForEntityName.get("_")// TODO this is a bit hacky. For doing 121 indexes without parent
                    .stream()
                    .filter(current -> current.childVarName.equals(var.getName()))
                    .findFirst();
        }

        return mapping;
    }

//    public Optional<Mapping> getMappingsWhenIsParentForEntityVar(EntityInfo info, Variable var) {
//        Optional<Mapping> mapping = this.mappingsForEntityName.get(info.getFullyQualifedName())
//                .stream()
//                .filter(current -> current.fromVarName.equals(var.getName()))
//                .findFirst();
//
//        return mapping;
//    }

    public HashSet<EntityInfo> getEntityInfo() {

        return entityInfoList;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {

        Map<String, HashSet<Mapping>> newMappingsMap = new HashMap<>();

        this.mappingsForEntityName.entrySet()
            .stream()
            .forEach(entry -> {
                String key = entry.getKey();
                HashSet<Mapping> mappings = entry.getValue();

                HashSet<Mapping> clonedMappings = new HashSet<>();

                for(Mapping mapping : mappings) {
                    clonedMappings.add( (Mapping)mapping.clone() );
                }

                newMappingsMap.put(key, clonedMappings);
            });

        HashSet<EntityInfo> clonedInfoList = new HashSet<>();

        this.entityInfoList.stream()
                .forEach(entityInfo -> clonedInfoList.add( ((EntityInfo)entityInfo.clone()) ));

        return new Schema(clonedInfoList, newMappingsMap);
    }

    public static boolean hasDotAlfalfaDirectory(Path path)
    {
        Path pathWithAlfalfa = path.resolve(ALFALFA_DOT_FOLDER);

        if( Files.exists(pathWithAlfalfa) && Files.isDirectory(pathWithAlfalfa)) {
            return true;
        }
        return false;
    }


}
