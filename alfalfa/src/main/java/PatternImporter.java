package com.github.jamesarthurholland.alfalfa;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class PatternImporter
{
    public static void importPattern(String patternName, String version, Path outputDir) {
        // TODO must pass this alfalfafile of calling pattern, in order to do overrides.
        //  must return pattern meta file? maybe read meta file first to check requirements met re vars
        Path userHome = Paths.get(System.getProperty("user.home"));
        Path patternDir = userHome.resolve(".alfalfa/repository/").resolve(patternName).resolve(version).resolve("main"); // TODO main should be static string


        FileUtils.copyDirRecursive(patternDir, outputDir);
    }


}
