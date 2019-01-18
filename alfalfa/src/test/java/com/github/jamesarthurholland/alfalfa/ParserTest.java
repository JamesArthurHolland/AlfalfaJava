package com.github.jamesarthurholland.alfalfa;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;


import static junit.framework.TestCase.assertTrue;

public class ParserTest {

    private static EntityInfo getStudentEntity()  {
        ArrayList<Variable> studentVars = new ArrayList<Variable>();
        studentVars.add(new Variable(true, "protected", "long", "id"));
        studentVars.add(new Variable(false, "protected", "String", "firstName"));
        studentVars.add(new Variable(false, "protected", "String", "lastName"));
        studentVars.add(new Variable(false, "protected", "String", "course"));
        studentVars.add(new Variable(false, "protected", "String", "passport"));

        return new EntityInfo("Student", "com.exampleapp.test", "", studentVars);
    }

    private static EntityInfo getPassportEntity()  {
        ArrayList<Variable> vars = new ArrayList<Variable>();
        vars.add(new Variable(true, "protected", "long", "id"));
        vars.add(new Variable(false, "protected", "int", "countryOfIssue"));
        vars.add(new Variable(false, "protected", "long", "studentId"));

        return new EntityInfo("Passport", "com.exampleapp.test", "", vars);
    }

    @Test
    public void simpleEntityStudentParseTest() {
        EntityInfo entityInfo = getStudentEntity();
        EntityInfo scannedEntity = getConfigFromFilename("src/test/resources/Student.afae").getEntityInfo();
        assertTrue(scannedEntity.equals(entityInfo));
    }

    @Test
    public void simpleEntityPassportParseTest() {
        EntityInfo entityInfo = getPassportEntity();
        EntityInfo scannedEntity = getConfigFromFilename("src/test/resources/Passport.afae").getEntityInfo();
        assertTrue(scannedEntity.equals(entityInfo));
    }

    private Config.ConfigElement getConfigFromFilename(String fileName)
    {
        ArrayList<String> arrayList = readFileToArrayList(fileName);
        ConfigScanner configScanner = new ConfigScanner();
        return configScanner.readConfigFromLines(arrayList);
    }

    private ArrayList<String> readFileToArrayList(String fileName)
    {
        File studentEntityFile = new File(fileName);
        ArrayList<String> arrayList = null;
        try {
            List<String> list = Files.readAllLines(studentEntityFile.toPath(), Charset.defaultCharset());
            arrayList = new ArrayList<>();
            arrayList.addAll(list);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return arrayList;
    }

    @Test
    public void oneToOneEntityParseTest() {
        EntityInfo studentInfo = getStudentEntity();
        EntityInfo passportInfo = getPassportEntity();

        ArrayList<String> arrayList = readFileToArrayList("src/test/resources/Passport.afae");

        ConfigScanner configScanner = new ConfigScanner();
        Config.ConfigElement config = configScanner.readConfigFromLines(arrayList);
        EntityInfo scannedEntity = config.getEntityInfo();

//        assertTrue(entityInfo.equals(scannedEntity));
    }


}
