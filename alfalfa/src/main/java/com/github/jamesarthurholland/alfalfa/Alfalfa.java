package com.github.jamesarthurholland.alfalfa;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import com.github.jamesarthurholland.alfalfa.model.EntityInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Stack;
import java.util.stream.Collectors;

import static com.github.jamesarthurholland.alfalfa.StringUtils.fileIsTemplateFile;

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

    public static void alfalfaRun(Path workingDirectory, Schema config, Pattern rootPattern) {
        Stack<Pattern> patternStack = new Stack();
        patternStack.add(rootPattern);

        while( ! patternStack.empty() ) {
            Pattern pattern = patternStack.pop();

            pattern.files.stream()
                .forEach(fullPath -> {
                    Path filePathRelativeToModule = pattern.patternRepoPath.relativize(fullPath);
                    Path fileAbsoluteOutputPath = workingDirectory.resolve(pattern.outputPath).resolve(filePathRelativeToModule);

                    if( ! fileIsTemplateFile(fullPath) ) {
                        try {
                            Files.copy(fullPath, fileAbsoluteOutputPath);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }

                    System.out.println(filePathRelativeToModule);
                    System.out.println(fileAbsoluteOutputPath);
                    System.out.println("\n");

                    if(pattern.mode == Pattern.VariableMode.FOR_EACH) {
                        config.getEntityInfo().forEach(entityInfo -> {
                            Path fileOutputDirectoryPath = workingDirectory.resolve(pattern.outputPath).resolve(filePathRelativeToModule).getParent();
                            evaluateTemplateFileForConfig(fullPath, entityInfo, fileOutputDirectoryPath);
                        });
                    }
                });

            pattern.imports.forEach(patternStack::add);
        }
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
