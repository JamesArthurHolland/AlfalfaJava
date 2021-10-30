package com.github.jamesarthurholland.alfalfa.typeSystem;

import com.github.jamesarthurholland.alfalfa.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.InputMismatchException;

public class TypeSystemConverter
{

    public HashMap<String, HashMap<TypeSystem.Type, String>> typeSystems = new HashMap<>();

    public TypeSystemConverter() {
        URL res = getClass().getClassLoader().getResource("typeSystems");
        try {
            Path typeSystemPath = Paths.get(res.toURI()).toAbsolutePath();

            for (File file : typeSystemPath.toFile().listFiles()) {
                String langName = FilenameUtils.removeExtension(file.getName());
                HashMap<TypeSystem.Type, String> typeSystem = null;
                try {
                    typeSystem = readTypeSystemFromFilePath(file.toPath());
                } catch (IOException e) {
                    System.out.println("Could not read typesystem for path: " + file.toPath());
                    e.printStackTrace();
                }
                typeSystems.put(langName, typeSystem);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public HashMap<TypeSystem.Type, String> readTypeSystemFromFilePath(Path path) throws IOException {
        HashMap<TypeSystem.Type, String> typeSystem = new HashMap<>();
        Files.readAllLines(path).forEach(line -> {
            String[] lineArray = line.split("\\=");
            if(lineArray.length != 2) {
                System.out.println("Incorrect syntax for line: " + line + ", in typesystem: " + path);
            }
            String alfalfaType = lineArray[0];
            String targetType = lineArray[1];
            TypeSystem.Type type = TypeSystem.types.getOrDefault(alfalfaType, null);
            if(type == null) {
                throw new InputMismatchException("Type not found for " + alfalfaType + " in " + path);
            }
            typeSystem.put(type, targetType);
        });
        return typeSystem;
    }

    public String convert(String alfalfaTypeString, String typeSystemName) throws IllegalStateException {
        TypeSystem.Type type = TypeSystem.types.getOrDefault(alfalfaTypeString, null);
        if(type == null) {
            throw new IllegalStateException("Could not find type in Alfalfa's type system: " + alfalfaTypeString);
        }
        HashMap<TypeSystem.Type, String> langTypeSystem = typeSystems.getOrDefault(typeSystemName, null);
        if(langTypeSystem == null) {
            System.out.println("WARNING:: Could not find typesystem for language: " + typeSystemName);
            return "";
        }
        return langTypeSystem.get(type);
    }
}
