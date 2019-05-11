package com.github.jamesarthurholland.alfalfa.configurationBuildTests;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.Config;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.NoDotAlfalfaDirectoryException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConfigScanTests {

    private static final String EMPTY_DIR = "empty_dir";

    @Test
    public void simpleEntityStudentParseTest() {
        assertTrue(true);
    }

    @Test
    public void noDotAlfalfaDirectoryExceptionTest() {
        assertThrows(NoDotAlfalfaDirectoryException.class, () -> {
            File alfalfaDir = Config.readDotAlfalfaDirectory("src/test/resources/empty_dir");
        });
    }
}
