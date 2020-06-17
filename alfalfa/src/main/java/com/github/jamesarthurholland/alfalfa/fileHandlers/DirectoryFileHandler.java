package com.github.jamesarthurholland.alfalfa.fileHandlers;

import com.github.jamesarthurholland.alfalfa.SentenceEvaluator;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import com.github.jamesarthurholland.alfalfa.model.EntityInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class DirectoryFileHandler
{
    public static void handle(Path workingDirectory, Schema config, Pattern pattern, Path filePathRelativeToModule, AtomicReference<Path> fileAbsoluteOutputPath) {
        if(pattern.mode == Pattern.VariableMode.FOR_EACH && doesDirectoryNeedFolderSwap(filePathRelativeToModule, pattern)) {
            config.getEntityInfo().forEach(entityInfo -> {
                Path fileAbsoluteOutputPathNew = outputPathForPatternFolderSwap(filePathRelativeToModule, fileAbsoluteOutputPath, pattern, workingDirectory, entityInfo);
                try {
                    Files.createDirectories(fileAbsoluteOutputPathNew);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static boolean doesDirectoryNeedFolderSwap(Path relativePath, Pattern pattern) {
        for (Map.Entry<String, String> entry : pattern.folderSwaps.entrySet()) {
            String folderName = entry.getKey();

            if (relativePath.compareTo(Paths.get(folderName)) == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isChildFolderInParentFolder(Path childFolder, Path parentFolder) {
        return childFolder.startsWith(parentFolder);
    }

    public static Path outputPathForPatternFolderSwap(Path relativePath, AtomicReference<Path> unswapped, Pattern pattern, Path workingDirectory, EntityInfo entityInfo) {
        Path outputPath = Paths.get(unswapped.get().toUri());

        for (Map.Entry<String, String> entry : pattern.folderSwaps.entrySet()) {
            String folderName = entry.getKey();
            String swapValue = entry.getValue();

            if(isChildFolderInParentFolder(relativePath, Paths.get(folderName))) {
                String relativeFolderSwapped = unswapped.toString().replace(folderName, swapValue);
                relativeFolderSwapped = SentenceEvaluator.evaluateForEntityReplacements(relativeFolderSwapped, entityInfo);
                outputPath = workingDirectory.resolve(pattern.getOutputPath()).resolve(relativeFolderSwapped);
            }
        }
        return outputPath;
    }
}
