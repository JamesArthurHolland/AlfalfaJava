package com.github.jamesarthurholland.alfalfa;

import com.github.jamesarthurholland.alfalfa.Compiler;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.github.jamesarthurholland.alfalfa.*;

import javax.swing.text.html.parser.Entity;

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
        vars.add(new Variable(true, "protected", "int", "id"));
        vars.add(new Variable(false, "protected", "String", "firstName"));
        vars.add(new Variable(false, "protected", "String", "lastName"));
        vars.add(new Variable(false, "protected", "String", "course"));
        vars.add(new Variable(false, "protected", "String", "passport"));

        EntityInfo entityInfo = new EntityInfo("Student", "", "", vars);

        try {
            Path path = null;
            path = Paths.get(this.getClass().getResource("Employee.txt").toURI());
            List<String> list = Files.readAllLines(path, Charset.defaultCharset());
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.addAll(list);

            EntityInfo scannedEntity = ConfigScanner.readConfig(arrayList);

            assertTrue(entityInfo.equals(scannedEntity));
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        assertTrue(true);
    }
}
