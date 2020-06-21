package com.github.jamesarthurholland.alfalfa;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import com.github.jamesarthurholland.alfalfa.fileHandlers.DirectoryFileHandler;
import com.github.jamesarthurholland.alfalfa.fileHandlers.TemplateFileHandler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.github.jamesarthurholland.alfalfa.StringUtils.fileIsTemplateFile;

public class Alfalfa {

    private final static Logger Log = Logger.getLogger(Alfalfa.class.getName());

    public static void alfalfaSimpleRun(Path workingDirectory, Path templateDirectory, Schema config){
//            ArrayList<Path> patterns = Files.list(Paths.get("patternFolderPathString"))

        config.getEntityInfo().forEach(entityInfo -> {
            try {

                Files.list(templateDirectory)
                    .filter(StringUtils::fileIsTemplateFile)
                    .forEach(patternPath -> {
                        TemplateFileHandler.evaluateTemplateFileForConfig(patternPath, entityInfo, workingDirectory);
                    });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

//                    .collect(Collectors.toCollection(ArrayList::new));
    }

    public static boolean pathIsNotTheModuleRootFolder(Path path, Pattern pattern) {
        return ! Files.isDirectory(path) || (Files.isDirectory(path) && ! pattern.getPatternRepoPath().relativize(path).toString().isEmpty());
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
                .filter(path -> pathIsNotTheModuleRootFolder(path, pattern))
                .forEach(fullPath -> {
                    Log.info("File is " + fullPath);
                    Path filePathRelativeToModule = pattern.getPatternRepoPath().relativize(fullPath);

                    AtomicReference<Path> fileAbsoluteOutputPath = new AtomicReference<>(workingDirectory.resolve(pattern.getOutputPath()).resolve(filePathRelativeToModule));



                    if(fileIsTemplateFile(fullPath)) {
                        // TODO
                        TemplateFileHandler.handleTemplateFile(pattern, workingDirectory, config, fullPath);
                    }
                    else if (Files.isDirectory(fullPath)) {
                        DirectoryFileHandler.handle(workingDirectory, config, pattern, fullPath, fileAbsoluteOutputPath.get());
//                        else {
//                            try {
//                                Files.createDirectories(fileAbsoluteOutputPath.get());
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
                    }
                    else if( FileUtils.isEmptyDir(fullPath) || ! Files.isDirectory(fullPath) && !FileUtils.isAlfalfaFile(fullPath)) {
                        try {
                            // TODO add file if doesn't exist, otherwise if exists, find the delta between this one and the last. Then add the diff. Don't delete any code that was added manually.

// TODO folderSwap here if file is folder and folderfilePathRelativeToModule is listed in folder swaps


                            Log.info("Here not alfalfafile " + fullPath);
                            Files.lines(fullPath, Charset.defaultCharset()) // TODO this breaks for binary files
                                    .map(pattern::injectVarsToLine)
                                    .collect(Collectors.toCollection(ArrayList::new))
                                    .forEach(list -> {
                                        try {
                                            org.apache.commons.io.FileUtils.writeLines(fileAbsoluteOutputPath.get().toFile(), Collections.singleton(list));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    });


//                            Files.copy(fullPath, fileAbsoluteOutputPath, REPLACE_EXISTING);
                        } catch (Exception e1) {
                            Log.warning("File " + fullPath.toString() + " is a binary, copying without touching. Are you sure you need this binary?");
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




}
