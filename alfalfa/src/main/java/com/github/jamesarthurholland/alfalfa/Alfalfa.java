package com.github.jamesarthurholland.alfalfa;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import com.github.jamesarthurholland.alfalfa.fileHandlers.DirectoryFileHandler;
import com.github.jamesarthurholland.alfalfa.fileHandlers.TemplateFileHandler;
import com.github.jamesarthurholland.alfalfa.typeSystem.TypeSystemConverter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.github.jamesarthurholland.alfalfa.StringUtils.fileIsTemplateFile;

public class Alfalfa {

    private final static Logger Log = Logger.getLogger(Alfalfa.class.getName());

    public static boolean pathIsNotTheModuleRootFolder(Path path, Pattern pattern) {
        return ! Files.isDirectory(path) || (Files.isDirectory(path) && ! pattern.getPatternRepoPath().relativize(path).toString().isEmpty());
    }

    private static void createFoldersAndStaticFiles(Path workingDirectory, Schema config, Pattern rootPattern) {
        Stack<Pattern> patternStack = new Stack();
        patternStack.add(rootPattern);

        while( ! patternStack.empty() ) {
            Pattern pattern = patternStack.pop();
            Log.info("pattern is " + pattern.name);
            pattern.files.stream()
                .sorted()
                .map(Paths::get)
                .filter(path -> FileUtils.isAlfalfaFile(path) == false)// TODO this doesnt work
                .forEach(fullPath -> {
                    Path filePathRelativeToModule = pattern.getPatternRepoPath().relativize(fullPath);

                    Path filePathWithVars = workingDirectory.resolve(pattern.getOutputPath()).resolve(filePathRelativeToModule);

                    if (Files.isDirectory(fullPath)) {
                        if (pattern.folderSwaps.containsKey(filePathRelativeToModule.toString()) == false) {
                            DirectoryFileHandler.handle(workingDirectory, config, pattern, fullPath, filePathWithVars);
                        }
                    }
                });
            pattern.imports.forEach(patternStack::add);
        }
    }

    public static void alfalfaRun(Path workingDirectory, Schema config, Pattern rootPattern, TypeSystemConverter converter) {
//        createFoldersAndStaticFiles(workingDirectory, config, rootPattern);
        handleFiles(workingDirectory, config, rootPattern, converter);
    }

    private static void handleFiles(Path workingDirectory, Schema config, Pattern rootPattern, TypeSystemConverter converter) {
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
                    Path filePathRelativeToModule = pattern.getPatternRepoPath().relativize(fullPath);

                    Path fileAbsoluteOutputPath = workingDirectory.resolve(pattern.getOutputPath()).resolve(filePathRelativeToModule);

                    if(fileIsTemplateFile(fullPath)) {
                        Log.info("File1 is " + fullPath);
                        TemplateFileHandler.handleTemplateFile(pattern, workingDirectory, config, fullPath, converter);
                    }
                    else if( !Files.isDirectory(fullPath) && !FileUtils.isAlfalfaFile(fullPath)) {
                        try {
                            Log.info("File2 is " + fullPath);
                            // TODO add file if doesn't exist, otherwise if exists, find the delta between this one and the last. Then add the diff. Don't delete any code that was added manually.

                            // TODO folderSwap here if file is folder and folderfilePathRelativeToModule is listed in folder swaps

                            Log.info("Here not alfalfafile " + fullPath);
                            ArrayList<String> lines = Files.lines(fullPath, Charset.defaultCharset()) // TODO this breaks for binary files
                                    .map(pattern::injectVarsToLine)
                                    .collect(Collectors.toCollection(ArrayList::new));

                            try {
                                Path relativeRoot = filePathRelativeToModule.subpath(0, 1);
                                if (pattern.folderSwaps.containsKey(relativeRoot.toString())) {
                                    Path folderToSwap = Paths.get(pattern.patternRepoPath).resolve(relativeRoot);
                                    Path swappedFolder = DirectoryFileHandler.applyFolderSwapIfNeeded(folderToSwap, relativeRoot, pattern, workingDirectory, null);
                                    Path fileRelativeToSwapFolder = filePathRelativeToModule.subpath(1, filePathRelativeToModule.getNameCount());

                                    fileAbsoluteOutputPath = swappedFolder.resolve(fileRelativeToSwapFolder);
                                    fileAbsoluteOutputPath = StringUtils.injectVarsToPath(pattern, fileAbsoluteOutputPath);
                                }
                                org.apache.commons.io.FileUtils.writeLines(fileAbsoluteOutputPath.toFile(), lines);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //                            Files.copy(fullPath, fileAbsoluteOutputPath, REPLACE_EXISTING);
                        } catch (Exception e1) {
                            Log.warning("File " + fullPath.toString() + " is a binary, copying without touching. Are you sure you need this binary?");
                            Log.warning(e1.getMessage());
                            e1.printStackTrace();
                        }
                        return;
                    }
                });

            pattern.imports.forEach(patternStack::add);
        }
    }


}
