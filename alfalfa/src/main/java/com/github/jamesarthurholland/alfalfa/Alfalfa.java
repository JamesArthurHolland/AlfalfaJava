package com.github.jamesarthurholland.alfalfa;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import com.github.jamesarthurholland.alfalfa.model.EntityInfo;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.github.jamesarthurholland.alfalfa.StringUtils.fileIsTemplateFile;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Alfalfa {

    private final static Logger Log = Logger.getLogger(Alfalfa.class.getName());

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
                .sorted()
                .map(Paths::get)
                .filter(path -> FileUtils.isAlfalfaFile(path) == false )// TODO this doesnt work
                .forEach(fullPath -> {
                    Log.info("File is " + fullPath);
                    Path filePathRelativeToModule = pattern.getPatternRepoPath().relativize(fullPath);
                    // TODO folderSwap here if file is folder and folderfilePathRelativeToModule is listed in folder swaps
                    Path fileAbsoluteOutputPath = workingDirectory.resolve(pattern.getOutputPath()).resolve(filePathRelativeToModule);

                    if(fileIsTemplateFile(fullPath)) {
                        // TODO
                        if(pattern.mode == Pattern.VariableMode.FOR_EACH) {
                            config.getEntityInfo().forEach(entityInfo -> {
                                Path fileOutputDirectoryPath = workingDirectory.resolve(pattern.getOutputPath()).resolve(filePathRelativeToModule).getParent();
                                evaluateTemplateFileForConfig(fullPath, entityInfo, fileOutputDirectoryPath);
                            });
                        }
                    }
//                    else if ( )
                    else if( FileUtils.isEmptyDir(fullPath) || ! Files.isDirectory(fullPath) && !FileUtils.isAlfalfaFile(fullPath)) {
                        try {
                            // TODO add file if doesn't exist, otherwise if exists, find the delta between this one and the last. Then add the diff. Don't delete any code that was added manually.




                            Files.lines(fullPath, Charset.defaultCharset()) // TODO this breaks for binary files
                                .map(pattern::injectVarsToLine)
                                .collect(Collectors.toCollection(ArrayList::new))
                                .forEach(list -> {
                                    try {
                                        org.apache.commons.io.FileUtils.writeLines(fileAbsoluteOutputPath.toFile(), Collections.singleton(list));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });



//                            Files.copy(fullPath, fileAbsoluteOutputPath, REPLACE_EXISTING);
                        } catch (UncheckedIOException e) {
                            Log.warning("File " + fullPath.toString() + " is a binary, copying without touching. Are you sure you need this binary?");
                            try {
                                Files.copy(fullPath, fileAbsoluteOutputPath, REPLACE_EXISTING);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                        catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        return;
                    }

                    System.out.println(filePathRelativeToModule);
                    System.out.println(fileAbsoluteOutputPath);
                    System.out.println("\n");



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
