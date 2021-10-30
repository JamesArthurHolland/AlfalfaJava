package com.github.jamesarthurholland.alfalfa;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.PatternFileScanner;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.NoEntityFileException;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.header.InvalidHeaderException;
import com.github.jamesarthurholland.alfalfa.typeSystem.TypeSystemConverter;
import com.google.common.io.Files;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.Callable;

@CommandLine.Command(description = "Runs Alfalfa.",
        name = "afa", mixinStandardHelpOptions = true, version = "checksum 3.0")
public class AlfalfaMain implements Callable<Void>
{
//    @CommandLine.Parameters(arity = "1", paramLabel = "Runtime directory", description = "Working directory to run Alfalfa.")
//    private String workingDirectoryString = ""; TODO defaults to current directory but maybe can run other dirs in future?

    @CommandLine.Option(names = { "-t", "--template" }, description = "Templates folder or template file.")
    private String patternDirectoryString = "";

    @CommandLine.Option(names = { "-pv", "--pattern-version" }, description = "Templates folder or template file.")
    private String patternVersionString = "";

    @CommandLine.Option(names = { "--debug" })
    private boolean debug = false;

    public static void main(String[] args)
    {
        CommandLine.call(new AlfalfaMain(), args);
    }

    @Override
    public Void call() throws Exception {
        try {
            Path workingDirectory = Paths.get(System.getProperty("user.dir"));

            Pattern pattern = new PatternFileScanner(workingDirectory).scan();

            TypeSystemConverter converter = new TypeSystemConverter();

            Schema config = new Schema(workingDirectory);


            System.out.println("ALFALFA run \n\n\n========\n\n\n");
            Alfalfa.alfalfaRun(workingDirectory, config, pattern, converter);

        }
        catch (NoPatternDirectoryException e) {
            System.out.println(e.getMessage());
        }
        catch (PatternDirectoryEmptyException e) {
            System.out.println(e.getMessage());
        }
        catch (InvalidHeaderException e) {
            System.out.println(e.getMessage());
        }
        catch (NoEntityFileException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    protected static ArrayList<File> getFiles(File[] allFiles, String extension)
    {
        ArrayList<File> templates = new ArrayList<>();

        for(File file : allFiles) {
            String fileExtension = Files.getFileExtension(file.getName());
            if(fileExtension.equals(extension)) {
                templates.add(file);
            }
        }
        return templates;
    }


}
