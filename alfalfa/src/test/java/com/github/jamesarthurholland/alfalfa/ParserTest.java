package com.github.jamesarthurholland.alfalfa;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.github.jamesarthurholland.alfalfa.*;


import static junit.framework.TestCase.assertTrue;

public class ParserTest {
//    @Test
//    public void entityParseTest() {
//        ArrayList<Variable> vars = new ArrayList<Variable>();
//        vars.add(new Variable(true, "protected", "int", "id"));
//        vars.add(new Variable(false, "protected", "String", "firstName"));
//        vars.add(new Variable(false, "protected", "String", "lastName"));
//        vars.add(new Variable(false, "protected", "String", "department"));
//        vars.add(new Variable(false, "protected", "String", "email"));
//
//        EntityInfo entityInfo = new EntityInfo("Employee", "", "", vars);
//
////        String xml = IOUtils.toString(
////                this.getClass().getResourceAsStream("Employee.txt"),
////                "UTF-8"
////        );
////        CompilerResult compilerResult = Compiler.runAlfalfa(entityInfo, );
//
//        assertTrue(true);
//    }

    @Test
    public void entityParseTest() {
        ArrayList<Variable> vars = new ArrayList<Variable>();
        vars.add(new Variable(true, "protected", "long", "id"));
        vars.add(new Variable(false, "protected", "String", "firstName"));
        vars.add(new Variable(false, "protected", "String", "lastName"));
        vars.add(new Variable(false, "protected", "String", "course"));
        vars.add(new Variable(false, "protected", "String", "passport"));

        EntityInfo entityInfo = new EntityInfo("Student", "", "", vars);

        try {
            File studentEntityFile = new File("src/test/resources/Student.afae");
            List<String> list = Files.readAllLines(studentEntityFile.toPath(), Charset.defaultCharset());
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.addAll(list);

            EntityInfo scannedEntity = ConfigScanner.readConfig(arrayList);

            assertTrue(entityInfo.equals(scannedEntity));
        } catch ( IOException e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }
}
