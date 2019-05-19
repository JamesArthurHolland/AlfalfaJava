package com.github.jamesarthurholland.alfalfa;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class PatternImporter
{
    public static void importPattern(String patternName, String version, Path outputDir) {
        Path userHome = Paths.get(System.getProperty("user.home"));
        Path patternDir = userHome.resolve(".alfalfa/repository/").resolve(patternName).resolve(version).resolve("main"); // TODO main should be static string


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
}
