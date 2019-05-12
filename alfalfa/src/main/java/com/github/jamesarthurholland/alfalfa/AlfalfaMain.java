package com.github.jamesarthurholland.alfalfa;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.Config;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.NoEntityFileException;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.header.InvalidHeaderException;
import com.google.common.io.Files;
import picocli.CommandLine;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.Callable;

@CommandLine.Command(description = "Prints the checksum (MD5 by default) of a file to STDOUT.",
        name = "afa", mixinStandardHelpOptions = true, version = "checksum 3.0")
public class AlfalfaMain implements Callable<Void>
{
    @CommandLine.Parameters(arity = "1", paramLabel = "Runtime directory", description = "Working directory to run Alfalfa.")
    private String workingDirectory = "";

    @CommandLine.Option(names = { "-t", "--template" }, description = "Templates folder or template file.")
    private String patternDirectoryString = "";



    public static void main(String[] args)
    {
        CommandLine.call(new AlfalfaMain(), args);
    }

    @Override
    public Void call() throws Exception {
        try {
            String workingDirectory = System.getProperty("user.dir");
            File patternFolder = new File(patternDirectoryString);
            ArrayList<File> listOfFiles = getFiles(patternFolder.listFiles(), StringUtils.TEMPLATE_EXTENSION);

            File templateFolder = new File(workingDirectory);


//
            ArrayList<String> entityInfoArrayList = StringUtils.fileToArrayList(System.getProperty("user.dir") + "/name.afae");
//            ArrayList<String> entityInfoArrayList = EntityScanner.fileToArrayList(workingDirectory + "/src/name.afae");


            Config config = new Config(Paths.get(workingDirectory));

//            Config.ConfigElement configElement = modelScanner.readConfigFromLines(entityInfoArrayList);
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
