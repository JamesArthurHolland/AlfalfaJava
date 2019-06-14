package com.github.jamesarthurholland.alfalfa;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.PatternFileScanner;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.NoEntityFileException;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.header.InvalidHeaderException;
import com.google.common.io.Files;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.Callable;

@CommandLine.Command(description = "Prints the checksum (MD5 by default) of a file to STDOUT.",
        name = "afa", mixinStandardHelpOptions = true, version = "checksum 3.0")
public class AlfalfaMain implements Callable<Void>
{
//    @CommandLine.Parameters(arity = "1", paramLabel = "Runtime directory", description = "Working directory to run Alfalfa.")
//    private String workingDirectoryString = ""; TODO defaults to current directory but maybe can run other dirs in future?

    @CommandLine.Option(names = { "-t", "--template" }, description = "Templates folder or template file.")
    private String patternDirectoryString = "";

    @CommandLine.Option(names = { "--debug" })
    private boolean debug = false;

    public static void main(String[] args)
    {
        CommandLine.call(new AlfalfaMain(), args);
    }

    @Override
    public Void call() throws Exception {
        try {
            if(debug == true) {
                String patternName = "com.github.jamesarthurholland/genericapi/main"; // TODO use dots the whole way or look at how maven does it
                String version = "0.1";

//        com.github.jamesarthurholland.alfalfa.PatternImporter.importPattern(patternName, version, tempDir);

                Path outputPath = Paths.get("/Users/beljh/testoutput");

                FileUtils.copyDirRecursive(Paths.get("src/test/resources/exampleWorkingDirectory"), outputPath);
                Pattern pattern = new PatternFileScanner(Paths.get("src/test/resources/exampleWorkingDirectory")).scan();


                Schema config = new Schema(Paths.get("src/test/resources/exampleWorkingDirectory/"));
                Alfalfa.alfalfaRun(outputPath, config, pattern);
                return null;
            }
            Path workingDirectory = Paths.get(System.getProperty("user.dir"));
            Schema config = new Schema(workingDirectory);
            Path patternDirectory = Paths.get(patternDirectoryString);
            Alfalfa.alfalfaSimpleRun(workingDirectory, patternDirectory, config);




//            Schema.ConfigElement configElement = modelScanner.readConfigFromLines(entityInfoArrayList);
//            config.addElement(configElement);

//            EntityInfo entityInfo = new EntityInfo();
//            Variable id = new Variable(true, "public", "long", "id");
//            Variable companyId = new Variable(true, "public", "long", "companyId");
//            Variable contractId = new Variable(true, "public", "String", "contractId");
//            Variable status = new Variable(true, "public", "int", "status");
//            entityInfo.name = "Shift";
//            entityInfo.variables.add(id);
//            entityInfo.variables.add(companyId);
//            entityInfo.variables.add(contractId);
//            entityInfo.variables.add(status);

//            for(File file : listOfFiles) {
//                String fileName = file.getName();
//                ArrayList<String> lines = StringUtils.fileToArrayList(alfalfaDirectory + fileName);
//
//                CompilerResult compilerResult = Compiler.runAlfalfa(configElement.getEntityInfo(), lines);
//                Compiler.writeCompilerResultToFile(workingDirectory, compilerResult);
//            }
//            ArrayList<String> evalResult = compiler.evaluateTree (parseTree, entityInfo);


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
