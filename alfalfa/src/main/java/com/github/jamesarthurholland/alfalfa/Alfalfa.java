package com.github.jamesarthurholland.alfalfa;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import com.github.jamesarthurholland.alfalfa.model.EntityInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Alfalfa {
    public static void alfalfaSimpleRun(Path workingDirectory, Path templateDirectory, Schema config){
//            ArrayList<Path> patterns = Files.list(Paths.get("patternFolderPathString"))

        config.getEntityInfo().forEach(entityInfo -> {
            try {

                Files.list(templateDirectory)
                        .filter(StringUtils::fileIsTemplateFile)
                        .forEach(patternPath -> {
                            evaluateTemplateFileForConfig(patternPath, entityInfo, workingDirectory);
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

//                    .collect(Collectors.toCollection(ArrayList::new));


    }

    public static void evaluateTemplateFileForConfig(Path patternFilePath, EntityInfo entityInfo, Path workingDirectory) {
        try {
            ArrayList<String> lines = Files.lines(patternFilePath).collect(Collectors.toCollection(ArrayList::new));
            CompilerResult compilerResult = Compiler.runAlfalfa(entityInfo, lines);
            Compiler.writeCompilerResultToFile(workingDirectory.toString(), compilerResult);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
