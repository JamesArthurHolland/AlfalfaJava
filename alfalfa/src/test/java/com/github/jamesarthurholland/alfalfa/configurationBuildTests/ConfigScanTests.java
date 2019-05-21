package com.github.jamesarthurholland.alfalfa.configurationBuildTests;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.Config;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.ModelFileScanner;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.NoDotAlfalfaDirectoryException;
import com.github.jamesarthurholland.alfalfa.model.EntityInfo;
import com.github.jamesarthurholland.alfalfa.model.Mapping;
import com.github.jamesarthurholland.alfalfa.model.Variable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigScanTests {

    private static final String EMPTY_DIR = "empty_dir";

    private static HashMap<String, HashSet<Mapping>> getMappings()  { // Passport holds the data because it's the child
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

    private static EntityInfo getStudentEntity()  {
        ArrayList<Variable> studentVars = new ArrayList<Variable>();
        studentVars.add(new Variable(true, "protected", "long", "id"));
        studentVars.add(new Variable(false, "protected", "String", "firstName"));
        studentVars.add(new Variable(false, "protected", "String", "lastName"));
        studentVars.add(new Variable(false, "protected", "String", "course"));
        studentVars.add(new Variable(false, "protected", "String", "passport"));

        return new EntityInfo("Student", "com.exampleapp.test", studentVars);
    }

    private static EntityInfo getPassportEntity()  {
        ArrayList<Variable> vars = new ArrayList<Variable>();
        vars.add(new Variable(true, "protected", "long", "id"));
        vars.add(new Variable(false, "protected", "int", "countryOfIssue"));
        vars.add(new Variable(false, "protected", "long", "studentId"));

        return new EntityInfo("Passport", "com.exampleapp.test", vars);
    }


//    @Test
//    public void testAllStructs() {
//        EntityInfo entityInfo = getStudentEntity();
//    }

    @Test
    public void configScanTest() {
        Config config = new Config(Paths.get("src/test/resources/exampleWorkingDirectory/"));

        ModelFileScanner.ModelFileScan modelFile = ModelFileScanner.readModelFile(Paths.get("src/test/resources/exampleWorkingDirectory/.alfalfa/Passport.afam"));

    }

    @Test
    public void modelFileScanTest() {
        EntityInfo entityInfo = getPassportEntity();
        ModelFileScanner.ModelFileScan modelFile = ModelFileScanner.readModelFile(Paths.get("src/test/resources/exampleWorkingDirectory/.alfalfa/Passport.afam"));
        assertTrue(modelFile.getEntityInfo().equals(entityInfo));
        assertTrue(modelFile.getMappings().equals(getMappings()));
    }

    @Test
    public void scanEntityStudentParseTest() {
        EntityInfo entityInfo = getStudentEntity();
        EntityInfo scannedEntity = ModelFileScanner.readEntityInfoFromFile(Paths.get("src/test/resources/exampleWorkingDirectory/.alfalfa/Student.afam"));
        assertTrue(scannedEntity.equals(entityInfo));
    }

    @Test
    public void scanMappingsTest() {
        HashMap<String, HashSet<Mapping>> knownMap = getMappings();
        HashMap<String, HashSet<Mapping>> givenMap = ModelFileScanner.readMappingsFromFile(Paths.get("src/test/resources/exampleWorkingDirectory/.alfalfa/Passport.afam"));
        assertTrue(knownMap.equals(givenMap));
    }


    @Test void copyDirectoryFromAlfalfaRepo(@TempDir Path tempDir) {
        // arrange

        String patternName = "com.github.jamesarthurholland/genericapi";
        String version = "0.1";

        com.github.jamesarthurholland.alfalfa.PatternImporter.importPattern(patternName, version, tempDir);

        // assert
        assertAll(
                () -> assertTrue(Files.exists(tempDir.resolve("test")))
        );
    }

    @Test
    public void compareHashMap() {
        HashMap<String, HashSet<Mapping>> map1 = getMappings();
        HashMap<String, HashSet<Mapping>> map2 = getMappings();
        assertTrue(map1.equals(map2));
    }

    @Test
    public void noDotAlfalfaDirectoryExceptionTest() {
        assertFalse(Config.hasDotAlfalfaDirectory(Paths.get("src/test/resources/empty_dir")));
    }


}
