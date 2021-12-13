package com.github.jamesarthurholland.alfalfa.fileHandlers;

import com.esotericsoftware.minlog.Log;
import com.github.jamesarthurholland.alfalfa.FileUtils;
import com.github.jamesarthurholland.alfalfa.StringUtils;
import com.github.jamesarthurholland.alfalfa.transpiler.SentenceVarEvaluator;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class DirectoryFileHandler
{
    public static void handle(Path workingDirectory, Schema config, Pattern pattern, Path fullInputPath, Path pathUnswapped) {
        Log.info("In folder is " + fullInputPath);
        if(pattern.mode == Pattern.ImportMode.ONCE_FOR_ENTITY ) {
            createDirectory(workingDirectory, pattern, fullInputPath, pathUnswapped, null);
        }
        if(pattern.mode == Pattern.ImportMode.FOR_EACH_ENTITY ) {
            for (EntityInfo entityInfo : config.getEntityInfo()) {
                createDirectory(workingDirectory, pattern, fullInputPath, pathUnswapped, entityInfo);
//                Path filePathRelativeToModule = pattern.getPatternRepoPath().relativize(filePath);
//                Path fullPathUnswapped = pathUnswapped.resolve(filePathRelativeToModule);
//                boolean created =
//                if (created == false) {
//                    continue;
//                }
            }
        }
//        else if(FileUtils.isEmptyDir(fullInputPath)) {
//            try {
//                Files.createDirectories(pathUnswapped);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    private static boolean createDirectory(Path workingDirectory, Pattern pattern, Path fullInputPath, Path pathUnswapped, EntityInfo info) {
        try {
            Path resolvedPath = applyFolderSwapIfNeeded(
                    fullInputPath,
                    pathUnswapped,
                    pattern,
                    workingDirectory,
                    info
            );
            if (resolvedPath == null) {
                return false;
            }
            Files.createDirectories(resolvedPath);
            Log.info("Out folder is " + resolvedPath.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean doesDirectoryNeedFolderSwap(Path relativePath, Pattern pattern) {
        for (Map.Entry<String, String> entry : pattern.folderSwaps.entrySet()) {
            String folderName = entry.getKey();

            if (isChildFolderInParentFolder(relativePath, Paths.get(folderName))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChildFolderInParentFolder(Path childFolder, Path parentFolder) {
        return childFolder.startsWith(parentFolder);
    }

    public static Path applyFolderSwapIfNeeded(Path fullInputPath, Path unswapped, Pattern pattern, Path workingDirectory, EntityInfo entityInfo) {
        Path filePathRelativeToModule = pattern.getPatternRepoPath().relativize(fullInputPath);
        if (filePathRelativeToModule.toString().isEmpty()) {
            return null;
        }
        if (!doesDirectoryNeedFolderSwap(filePathRelativeToModule, pattern)) {
            return unswapped;
        }
        Path resolvedPath = unswapped;

        for (Map.Entry<String, String> entry : pattern.folderSwaps.entrySet()) {
            String folderName = entry.getKey();
            String swapValue = entry.getValue();

            if(isChildFolderInParentFolder(filePathRelativeToModule, Paths.get(folderName))) {
                String relativeFolderSwapped = unswapped.toString().replace(folderName, swapValue);
                if (entityInfo != null) {
                    relativeFolderSwapped = SentenceVarEvaluator.evaluateForEntityReplacements(relativeFolderSwapped, entityInfo);
                }
                resolvedPath = workingDirectory.resolve(pattern.getOutputPath()).resolve(relativeFolderSwapped);
                resolvedPath = StringUtils.injectVarsToPath(pattern, resolvedPath);
            }
        }
        return resolvedPath;
    }
}
