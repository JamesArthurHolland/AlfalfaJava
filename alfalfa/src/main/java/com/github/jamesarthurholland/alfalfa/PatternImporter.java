package com.github.jamesarthurholland.alfalfa;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class PatternImporter
{
    public static void importPattern(String patternName, String version, Path outputDir) {
        // TODO must pass this alfalfafile of calling pattern, in order to do overrides.
        //  must return pattern meta file? maybe read meta file first to check requirements met re vars
        Path patternDir = FileUtils.patternPathMain(patternName, version); // TODO main should be static string

        FileUtils.copyDirRecursive(patternDir, outputDir);
    }


}
