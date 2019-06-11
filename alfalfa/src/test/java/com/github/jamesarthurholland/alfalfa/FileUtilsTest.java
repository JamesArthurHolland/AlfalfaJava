package com.github.jamesarthurholland.alfalfa;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.PatternFileScanner;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileUtilsTest {

    @Test
    public void fileUtils() {
        String patternName = "com.github.jamesarthurholland/genericapi"; // TODO use dots the whole way or look at how maven does it
        String version = "0.1";


        Path modulePatternPath = FileUtils.modulePatternPath(patternName, version, "di");
        assertTrue(FileUtils.patternPath(patternName, version).relativize(modulePatternPath).toString().equals("modules/di"));
    }
}
