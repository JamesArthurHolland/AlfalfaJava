package com.github.jamesarthurholland.alfalfa.fileHandlers;

import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.TemplateParser;
import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.TranspileResult;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import com.github.jamesarthurholland.alfalfa.transpiler.TreeEvaluator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;

public class TemplateFileHandler
{
    public static void handleTemplateFile(Pattern pattern, Path workingDirectory, Schema schema, Path fullInputPath) {

        if(pattern.mode == Pattern.ImportMode.FOR_EACH_ENTITY) {
            evaluateTemplateFileForEntityInfo(fullInputPath, schema, fullInputPath, workingDirectory, pattern);
        }
        else if (pattern.mode == Pattern.ImportMode.ONCE_FOR_ENTITY) {
            evaluateTemplateFileForSchema(fullInputPath, schema, fullInputPath, workingDirectory, pattern);
        }

        // TODO not for each?
    }

    public static void evaluateTemplateFileForEntityInfo(Path patternFilePath, Schema schema, Path fullInputPath, Path workingDirectory, Pattern pattern) {
        schema.getEntityInfo().forEach(entityInfo -> {
            Path filePathRelativeToModule = pattern.getPatternRepoPath().relativize(fullInputPath);
            Path fileOutputDirectoryPath = workingDirectory.resolve(pattern.getOutputPath()).resolve(filePathRelativeToModule).getParent();

            if(pattern.mode == Pattern.ImportMode.FOR_EACH_ENTITY && DirectoryFileHandler.doesDirectoryNeedFolderSwap(filePathRelativeToModule, pattern)) {
                fileOutputDirectoryPath = DirectoryFileHandler.outputPathForPatternFolderSwap(
                        filePathRelativeToModule, fileOutputDirectoryPath, pattern, workingDirectory, entityInfo
                );
            }

            try {
                ArrayList<String> lines = Files.lines(patternFilePath).collect(Collectors.toCollection(ArrayList::new));

                LinkedHashMap<String, Object> container = new LinkedHashMap<>();
                container.put(TemplateParser.ENTITY_INFO_KEY, entityInfo);

                TranspileResult transpileResult = TreeEvaluator.runAlfalfa(lines, container, pattern); // TODO pass pattern here and do conditional based on variable mode
                TemplateParser.writeCompilerResultToFile(fileOutputDirectoryPath.toString(), transpileResult);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }



    public static void evaluateTemplateFileForSchema(Path patternFilePath, Schema schema, Path fullInputPath, Path workingDirectory, Pattern pattern) {
        Path filePathRelativeToModule = pattern.getPatternRepoPath().relativize(fullInputPath);
        Path fileOutputDirectoryPath = workingDirectory.resolve(pattern.getOutputPath()).resolve(filePathRelativeToModule).getParent();

        try {
            ArrayList<String> lines = Files.lines(patternFilePath).collect(Collectors.toCollection(ArrayList::new));

            LinkedHashMap<String, Object> container = new LinkedHashMap<>();
            container.put(TemplateParser.SCHEMA_KEY, schema);
            TranspileResult transpileResult = TreeEvaluator.runAlfalfa(lines, container, pattern); // TODO pass pattern here and do conditional based on variable mode
            TemplateParser.writeCompilerResultToFile(fileOutputDirectoryPath.toString(), transpileResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
