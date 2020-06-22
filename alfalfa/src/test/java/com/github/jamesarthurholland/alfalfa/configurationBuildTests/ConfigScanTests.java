package com.github.jamesarthurholland.alfalfa.configurationBuildTests;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.ModelFileScanner;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Mapping;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Variable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
        LinkedHashSet<Variable> studentVars = new LinkedHashSet<>();
        studentVars.add(new Variable(true, "protected", "long", "id"));
        studentVars.add(new Variable(false, "protected", "String", "firstName"));
        studentVars.add(new Variable(false, "protected", "String", "lastName"));
        studentVars.add(new Variable(false, "protected", "String", "course"));

        return new EntityInfo("Student", "com.exampleapp.test", studentVars);
    }

    private static EntityInfo getPassportEntity()  {
        LinkedHashSet<Variable> vars = new LinkedHashSet<Variable>();
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
        Schema config = new Schema(Paths.get("src/test/resources/exampleWorkingDirectory/"));

        EntityInfo student = getStudentEntity();
        assertTrue(config.getEntityInfo().contains(student));
        assertTrue(config.getEntityInfo().contains(getPassportEntity()));

        ModelFileScanner.ModelFileScan modelFile = ModelFileScanner.readModelFile(Paths.get("src/test/resources/exampleWorkingDirectory/.alfalfa/Passport.afam"));

    }

    @Test
    public void variableEqualsTest() {
        Variable v1 = new Variable(true, "protected", "long", "id");
        Variable v2 = new Variable(true, "protected", "long", "id");

        assertTrue(v1.equals(v2));
    }

    @Test
    public void variablesEqualsTest() {
        Variable v1 = new Variable(true, "protected", "long", "id");
        Variable v2 = new Variable(true, "protected", "long", "id");
        LinkedHashSet<Variable> vs1 = new LinkedHashSet<>();
        LinkedHashSet<Variable> vs2 = new LinkedHashSet<>();
        vs1.add(v1);
        vs2.add(v2);

        assertTrue(vs1.equals(vs2));
    }

    @Test
    public void entityEqualsTest() {
        EntityInfo p1 = getPassportEntity();
        EntityInfo p2 = getPassportEntity();

        assertTrue(p1.equals(p2));
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
        assertFalse(Schema.hasDotAlfalfaDirectory(Paths.get("src/test/resources/empty_dir")));
    }


}
