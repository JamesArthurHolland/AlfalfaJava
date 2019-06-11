package com.github.jamesarthurholland.alfalfa;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {


    public static void copyDirRecursive(Path patternDir, Path outputDir) {
        try (Stream<Path> stream = Files.walk(patternDir)) {
            stream.forEachOrdered(sourcePath -> {
                System.out.println(sourcePath);
                System.out.println(patternDir.relativize(sourcePath));
                try {
                    Files.copy(
                            sourcePath,
                            outputDir.resolve(patternDir.relativize(sourcePath))
                    );
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();

        }
    }

    public static ArrayList<Path> getFilePathsRecursive(Path patternDir) {
        ArrayList<Path> pathArrayList = new ArrayList<>();

        try (Stream<Path> stream = Files.walk(patternDir)) {
            return stream.collect(Collectors.toCollection(ArrayList::new));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return pathArrayList;
    }

    public static Path getAlfalfaRepository() {
        Path userHome = Paths.get(System.getProperty("user.home"));
        return userHome.resolve(".alfalfa/repository/");
    }

    public static Path modulePatternPath(String patternName, String version, String moduleName) {
        return patternPath(patternName, version).resolve("modules").resolve(moduleName);
    }

    public static Path patternPathMain(String patternName, String version) {
        return patternPath(patternName, version).resolve("main");
    }

    public static Path patternPath(String patternName, String version) {
        return getAlfalfaRepository().resolve(patternName).resolve(version);
    }


}