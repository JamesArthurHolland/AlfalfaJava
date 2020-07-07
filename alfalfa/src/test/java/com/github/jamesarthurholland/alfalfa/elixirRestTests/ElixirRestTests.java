package com.github.jamesarthurholland.alfalfa.elixirRestTests;

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

public class ElixirRestTests {

//    @Test
//    public void patternScan() {
//        Path patternPath = Paths.get("src/test/resources/examplePatternDirectory");
//        Pattern pattern = PatternFileScanner.scanPatternFile(patternPath);
//        System.out.println("PatternScanTest");
//    }

    @Test void api(@TempDir Path tempDir) {
        // arrange

        FileUtils.copyDirRecursive(Paths.get("src/test/resources/exampleWorkingDirectoryElixir"), tempDir);
        Pattern pattern = new PatternFileScanner(tempDir).scan();

        Schema config = new Schema(tempDir);


        System.out.println("ALFALFA run \n\n\n========\n\n\n");
        Alfalfa.alfalfaRun(tempDir, config, pattern);

        // assert
        assertAll(
                () -> assertTrue(Files.exists(tempDir.resolve("api/lib/elixir_rest_web/controllers"))),
                () -> assertTrue(Files.exists(tempDir.resolve("api/lib/elixir_rest/student")), "No student dir"),
                () -> assertTrue(Files.exists(tempDir.resolve("api/lib/elixir_rest/student/student_entity.ex")), "No student_entity file")
        );
    }
}
