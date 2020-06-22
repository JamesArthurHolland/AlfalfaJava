package com.github.jamesarthurholland.alfalfa.fileHandlers;

import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.TemplateParser;
import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.TemplateParseResult;
import com.github.jamesarthurholland.alfalfa.SentenceEvaluator;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import com.github.jamesarthurholland.alfalfa.model.EntityInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class TemplateFileHandler
{
    public static void handleTemplateFile(Pattern pattern, Path workingDirectory, Schema config, Path fullInputPath) {
        Path filePathRelativeToModule = pattern.getPatternRepoPath().relativize(fullInputPath);

        if(pattern.mode == Pattern.VariableMode.FOR_EACH) {
            config.getEntityInfo().forEach(entityInfo -> {
                Path fileOutputDirectoryPath = workingDirectory.resolve(pattern.getOutputPath()).resolve(filePathRelativeToModule).getParent();

                if (doesPathNeedFolderSwap(filePathRelativeToModule, pattern)) {
                    fileOutputDirectoryPath = outputPathForPatternFolderSwap(filePathRelativeToModule, fileOutputDirectoryPath, pattern, workingDirectory, entityInfo);
                }

                evaluateTemplateFileForConfig(fullInputPath, entityInfo, fileOutputDirectoryPath);
            });
        }
        // TODO not for each?
    }

    public static void evaluateTemplateFileForConfig(Path patternFilePath, EntityInfo entityInfo, Path workingDirectory) {
        try {
            ArrayList<String> lines = Files.lines(patternFilePath).collect(Collectors.toCollection(ArrayList::new));
            TemplateParseResult templateParseResult = TemplateParser.runAlfalfa(entityInfo, lines); // TODO pass pattern here and do conditional based on variable mode
            TemplateParser.writeCompilerResultToFile(workingDirectory.toString(), templateParseResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean doesPathNeedFolderSwap(Path relativePath, Pattern pattern) {
        for (Map.Entry<String, String> entry : pattern.folderSwaps.entrySet()) {
            String folderName = entry.getKey();

            if (isChildPathInParentPath(relativePath, Paths.get(folderName))) {
                return true;
            }
        }
        return false;
    }

    public static Path outputPathForPatternFolderSwap(Path relativePath, Path unswapped, Pattern pattern, Path workingDirectory, EntityInfo entityInfo) {
        Path outputPath = Paths.get(unswapped.toUri());

        for (Map.Entry<String, String> entry : pattern.folderSwaps.entrySet()) {
            String folderName = entry.getKey();
            String swapValue = entry.getValue();

            if(isChildPathInParentPath(relativePath, Paths.get(folderName))) {
                String relativeFolderSwapped = unswapped.toString().replace(folderName, swapValue);
                relativeFolderSwapped = SentenceEvaluator.evaluateForEntityReplacements(relativeFolderSwapped, entityInfo);
                outputPath = workingDirectory.resolve(pattern.getOutputPath()).resolve(relativeFolderSwapped);
            }
        }
        return outputPath;
    }

    public static boolean isChildPathInParentPath(Path childFolder, Path parentFolder) {
        return childFolder.startsWith(parentFolder);
    }
}
