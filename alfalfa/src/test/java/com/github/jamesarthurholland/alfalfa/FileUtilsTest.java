package com.github.jamesarthurholland.alfalfa;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.PatternFileScanner;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileUtilsTest {

    @Test
    public void patternPath() {
        String fullPatternName = "com.github.jamesarthurholland/genericapi/main";
        String pathString = FileUtils.modulePath(fullPatternName, "0.1").toString();
        assertTrue(pathString.equals("com.github.jamesarthurholland/genericapi/0.1/main"));

    }
}
