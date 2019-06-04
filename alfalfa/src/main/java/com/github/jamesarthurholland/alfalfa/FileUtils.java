package com.github.jamesarthurholland.alfalfa;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    public static Path patternPathMain(String patternName, String version) {
        return patternPath(patternName, version).resolve("main");
    }

    public static Path patternPath(String patternName, String version) {
        Path userHome = Paths.get(System.getProperty("user.home"));
        return userHome.resolve(".alfalfa/repository/").resolve(patternName).resolve(version).resolve("main");
    }


}
