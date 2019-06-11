package com.github.jamesarthurholland.alfalfa;



import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.io.TempDir;


public class ParserTest {

    @Test
    public void simpleParseTest(@TempDir Path tempDir) {
        FileUtils.copyDirRecursive(Paths.get("src/test/resources/exampleWorkingDirectory"), tempDir);
        Schema config = new Schema(tempDir);
        Alfalfa.alfalfaSimpleRun(tempDir, Paths.get("src/test/resources/exampleTemplateDirectory"), config);
        System.out.println("SimpleParseTest debug");
    }

//
//    @Test
//    public void simpleEntityPassportParseTest() {
//        EntityInfo entityInfo = getPassportEntity();
//        EntityInfo scannedEntity = getConfigFromFilename("src/test/resources/Passport.afam").getEntityInfo();
//        assertTrue(scannedEntity.equals(entityInfo));
//    }
//
//    private Schema.Model getModelFromFilename(String fileName)
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
//        ArrayList<String> arrayList = readFileToArrayList("src/test/resources/Passport.afam");
//
//
//        Schema config = new Schema();
//        ConfigScanner configScanner = new ConfigScanner();
//        Schema.ConfigElement configElement = configScanner.readConfigFromLines(arrayList);
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