package com.github.jamesarthurholland.alfalfa.configurationBuilder;

import com.github.jamesarthurholland.alfalfa.StringUtils;
import com.github.jamesarthurholland.alfalfa.model.EntityInfo;
import com.github.jamesarthurholland.alfalfa.model.Mapping;
import one.util.streamex.EntryStream;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class Config {
    private HashSet<EntityInfo> entityInfoList = new HashSet<>();
    private Map<String, HashSet<Mapping>> mappingsForEntityName = new HashMap<>();

    public static final String ALFALFA_DOT_FOLDER = ".alfalfa";

    public Config(Path workingDirectory) throws NoDotAlfalfaDirectoryException {
        // getMappingStrings

        if(!hasDotAlfalfaDirectory(workingDirectory)) {
            throw new NoDotAlfalfaDirectoryException();
        }

        Path dotAlfalfaPath = workingDirectory.resolve(ALFALFA_DOT_FOLDER);


        streamFilterModelFiles(dotAlfalfaPath)
            .map(ModelFileScanner::readModelFile)
            .forEach(modelFileScan -> {
                mappingsForEntityName = EntryStream.of(modelFileScan.getMappings())
                        .append(EntryStream.of(mappingsForEntityName))
                        .toMap((e1, e2) -> e1);

                entityInfoList.add(modelFileScan.getEntityInfo());
            });

        System.out.println("Config constructor DBG");
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


//    public void addElement(ConfigElement element) {
//        this.entityInfoList.add(element.getEntityInfo());
//        for(Mapping mapping : element.getMappings()) {
//
//            ArrayList<Mapping> fromEntityMappings = this.mappingsForEntityName.getOrDefault(mapping.getParentEntityName(), new ArrayList<>());
//            fromEntityMappings.add(mapping);
//
//            ArrayList<Mapping> toEntityMappings = this.mappingsForEntityName.getOrDefault(mapping.getChildEntityName(), new ArrayList<>());
//            toEntityMappings.add(mapping);
//
//            this.mappingsForEntityName.put( // TODO this is mad wrong. inserting into self
//                mapping.getParentEntityName(),
//                fromEntityMappings
//            );
//            this.mappingsForEntityName.put(
//                mapping.getChildEntityName(),
//                toEntityMappings
//            );
//        }
//    }

    public HashSet<EntityInfo> getEntityInfo() {
        return entityInfoList;
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
