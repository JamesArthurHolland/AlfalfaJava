package com.github.jamesarthurholland.alfalfa;

import java.nio.file.Files;
import java.nio.file.Path;
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
}
