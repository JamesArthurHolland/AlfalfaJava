package com.github.jamesarthurholland.alfalfa.elixirRestTests;

import com.github.jamesarthurholland.alfalfa.Alfalfa;
import com.github.jamesarthurholland.alfalfa.FileUtils;
import com.github.jamesarthurholland.alfalfa.PatternImporter;
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

public class ElixirRestTests {

//    @Test
//    public void patternScan() {
//        Path patternPath = Paths.get("src/test/resources/examplePatternDirectory");
//        Pattern pattern = PatternFileScanner.scanPatternFile(patternPath);
//        System.out.println("PatternScanTest");
//    }

    @Test void api(@TempDir Path tempDir) {
        // arrange

        String patternName = "com.github.jamesarthurholland/elixir-rest"; // TODO use dots the whole way or look at how maven does it
        String version = "0.1";

        PatternImporter.importPattern(patternName, version, tempDir);

        FileUtils.copyDirRecursive(Paths.get("src/test/resources/exampleWorkingDirectoryElixir"), tempDir);
        Pattern pattern = new PatternFileScanner(Paths.get("src/test/resources/exampleWorkingDirectoryElixir")).scan();


        Schema config = new Schema(Paths.get("src/test/resources/exampleWorkingDirectoryElixir/"));


        System.out.println("ALFALFA run \n\n\n========\n\n\n");
        Alfalfa.alfalfaRun(tempDir, config, pattern);

        // assert
        assertAll(
                () -> assertTrue(Files.exists(tempDir.resolve("lib/elixir_rest_web/controllers")))
        );
    }
}
