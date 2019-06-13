package com.github.jamesarthurholland.alfalfa.genericApiTests;

import com.github.jamesarthurholland.alfalfa.Alfalfa;
import com.github.jamesarthurholland.alfalfa.FileUtils;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.PatternFileScanner;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
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

        String patternName = "com.github.jamesarthurholland/genericapi/main"; // TODO use dots the whole way or look at how maven does it
        String version = "0.1";

//        com.github.jamesarthurholland.alfalfa.PatternImporter.importPattern(patternName, version, tempDir);

        FileUtils.copyDirRecursive(Paths.get("src/test/resources/exampleWorkingDirectory"), tempDir);
        Pattern pattern = new PatternFileScanner(Paths.get("src/test/resources/exampleWorkingDirectory")).scan();


        Schema config = new Schema(Paths.get("src/test/resources/exampleWorkingDirectory/"));


        System.out.println("ALFALFA run \n\n\n========\n\n\n");
        Alfalfa.alfalfaRun(tempDir, config, pattern);

        // assert
        assertAll(
                () -> assertTrue(Files.exists(tempDir.resolve("main/src/main/kotlin/com/aetasa/rest/genericapiapp/di/")))
        );
    }
}
