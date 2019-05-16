package com.github.jamesarthurholland.alfalfa.configurationBuildTests;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.Config;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.ModelFileScanner;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.NoDotAlfalfaDirectoryException;
import com.github.jamesarthurholland.alfalfa.model.EntityInfo;
import com.github.jamesarthurholland.alfalfa.model.Mapping;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigScanTests {

    private static final String EMPTY_DIR = "empty_dir";

    private static HashMap<String, HashSet<Mapping>> getPassportMappings()  {
        HashSet<Mapping> mappings = new HashSet<Mapping>();

        mappings.add(
            new Mapping(
                    "com.exampleapp.test.Student",
                    "com.exampleapp.test.Passport",
                    "id",
                    "studentId",
                    Mapping.Type.ONE_TO_ONE

            )
        );

        HashMap<String, HashSet<Mapping>> mappingsForName = new HashMap<>();
        mappingsForName.put("com.exampleapp.test.Passport", mappings);
        mappingsForName.put("com.exampleapp.test.Student", mappings);

        return mappingsForName;
    }

    @Test
    public void compareHashMap() {
        HashMap<String, HashSet<Mapping>> map1 = getPassportMappings();
        HashMap<String, HashSet<Mapping>> map2 = getPassportMappings();
        assertTrue(map1.equals(map2));
    }

    @Test
    public void noDotAlfalfaDirectoryExceptionTest() {
        assertFalse(Config.hasDotAlfalfaDirectory(Paths.get("src/test/resources/model/empty_dir")));
    }

    @Test
    public void simpleEntityStudentParseTest() {
        HashMap<String, HashSet<Mapping>> knownMap = getPassportMappings();
        HashMap<String, HashSet<Mapping>> givenMap = ModelFileScanner.readMappingsFromFile(Paths.get("src/test/resources/model/Passport.afae"));
        assertTrue(knownMap.equals(givenMap));
    }
}
