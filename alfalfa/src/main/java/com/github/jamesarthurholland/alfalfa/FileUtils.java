package com.github.jamesarthurholland.alfalfa;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtils {
    public static boolean patternIsASubModuleOfCurrentPattern(String patternName) {
        return patternName.contains(".") == false;
    }

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

    public static Path modulePath(String patternName, String version, String moduleName) {
        return getAlfalfaRepository().resolve(patternName).resolve(version).resolve(moduleName);
    }

    public static Path patternPathMain(String patternName, String version) {
        return modulePath(patternName, version).resolve("main");
    }

    public static Path modulePathFromParentAndName(String parentPatternFullName, String version, String moduleName) {
        String[] patternNameParts = parentPatternFullName.split("/");
        String origin = patternNameParts[0];
        String pattern = patternNameParts[1];
        return getAlfalfaRepository().resolve(origin).resolve(pattern).resolve(version).resolve(moduleName);
    }

    // TODO if no module, ie com.github.jamesarthurholland/genericapi then it should import main
    public static Path modulePath(String patternFullName, String version) {
        String[] patternNameParts = patternFullName.split("/");
        String origin = patternNameParts[0];
        String pattern = patternNameParts[1];
        String module = patternNameParts[2];
        return getAlfalfaRepository().resolve(origin).resolve(pattern).resolve(version).resolve(module);
    }


}
