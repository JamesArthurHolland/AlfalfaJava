package com.github.jamesarthurholland.alfalfa.genericApiTests;

import com.github.jamesarthurholland.alfalfa.FileUtils;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.PatternFileScanner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenericApiTests {

//    @Test
//    public void patternScan() {
//        Path patternPath = Paths.get("src/test/resources/examplePatternDirectory");
//        Pattern pattern = PatternFileScanner.scanPatternFile(patternPath);
//        System.out.println("PatternScanTest");
//    }

    @Test void api(@TempDir Path tempDir) {
        // arrange

        String patternName = "com.github.jamesarthurholland/genericapi"; // TODO use dots the whole way or look at how maven does it
        String version = "0.1";

        com.github.jamesarthurholland.alfalfa.PatternImporter.importPattern(patternName, version, tempDir);

        Path patternPath = FileUtils.patternPath(patternName, version);
        Pattern pattern = PatternFileScanner.scanRootPatternFile(patternPath);

        // assert
        assertAll(
                () -> assertTrue(Files.exists(tempDir.resolve("test")))
        );
    }
}
