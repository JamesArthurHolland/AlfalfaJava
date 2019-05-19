package com.github.jamesarthurholland.alfalfa;



import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


import com.github.jamesarthurholland.alfalfa.PatternImporter;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.ModelFileScanner;
import com.github.jamesarthurholland.alfalfa.model.EntityInfo;
import com.github.jamesarthurholland.alfalfa.model.Variable;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.io.TempDir;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.*;


public class ParserTest {

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
    public void simpleEntityStudentParseTest() {
        EntityInfo entityInfo = getStudentEntity();
        EntityInfo scannedEntity = ModelFileScanner.readConfigFromFile(Paths.get("src/test/resources/model/Student.afae"));
        assertTrue(scannedEntity.equals(entityInfo));
    }



    @Test void copyDirectoryFromAlfalfaRepo(@TempDir Path tempDir) {
        // arrange

        String patternName = "com.github.jamesarthurholland/genericapi";
        String version = "0.1";

        PatternImporter.importPattern(patternName, version, tempDir);

        // assert
        assertAll(
                () -> assertTrue(Files.exists(tempDir.resolve("test")))
        );
    }
//
//    @Test
//    public void simpleEntityPassportParseTest() {
//        EntityInfo entityInfo = getPassportEntity();
//        EntityInfo scannedEntity = getConfigFromFilename("src/test/resources/Passport.afae").getEntityInfo();
//        assertTrue(scannedEntity.equals(entityInfo));
//    }
//
//    private Config.Model getModelFromFilename(String fileName)
//    {
//        ArrayList<String> arrayList = readFileToArrayList(fileName);
//        ConfigScanner configScanner = new ConfigScanner();
//        return configScanner.readConfigFromLines(arrayList);
//    }
//
//    private ArrayList<String> readFileToArrayList(String fileName)
//    {
//        File studentEntityFile = new File(fileName);
//        ArrayList<String> arrayList = null;
//        try {
//            List<String> list = Files.readAllLines(studentEntityFile.toPath(), Charset.defaultCharset());
//            arrayList = new ArrayList<>();
//            arrayList.addAll(list);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return arrayList;
//    }
//
//    @Test
//    public void oneToOneEntityParseTest() {
//        EntityInfo studentInfo = getStudentEntity();
//        EntityInfo passportInfo = getPassportEntity();
//
//        ArrayList<String> arrayList = readFileToArrayList("src/test/resources/Passport.afae");
//
//
//        Config config = new Config();
//        ConfigScanner configScanner = new ConfigScanner();
//        Config.ConfigElement configElement = configScanner.readConfigFromLines(arrayList);
//        EntityInfo scannedEntity = configElement.getEntityInfo();
//        config.addElement(configElement);
//
//        assertTrue(false);
//    }

    public class FileWriter {

        public void writeTo(String path, String content) throws IOException {
            Path target = Paths.get(path);
            if (Files.exists(target)) {
                throw new IOException("file already exists");
            }
            Files.copy(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), target);
        }
    }
}
