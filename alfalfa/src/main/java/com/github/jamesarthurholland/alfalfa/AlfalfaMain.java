package com.github.jamesarthurholland.alfalfa;

import picocli.CommandLine;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;

@CommandLine.Command(description = "Prints the checksum (MD5 by default) of a file to STDOUT.",
        name = "checksum", mixinStandardHelpOptions = true, version = "checksum 3.0")
public class AlfalfaMain implements Callable<Void>
{
    @CommandLine.Option(names = { "-m", "--model" }, description = "Models folder or model file.")
    private String modelFileString = "";

    @CommandLine.Option(names = { "-t", "--template" }, description = "Templates folder or template file.")
    private String patternFileString = "";



    public static void main(String[] args)
    {
        CommandLine.call(new AlfalfaMain(), args);
    }

    @Override
    public Void call() throws Exception {
        try {
            String workingDirectory = System.getProperty("user.dir");
            File folder = new File(patternFileString);
            ArrayList<File> listOfFiles = getTemplateFiles(folder.listFiles());

//
            ArrayList<String> entityInfoArrayList = StringUtils.fileToArrayList(System.getProperty("user.dir") + "/name.afae");
//            ArrayList<String> entityInfoArrayList = ConfigScanner.fileToArrayList(workingDirectory + "/src/name.afae");

            Config config = new Config();

            ConfigScanner configScanner = new ConfigScanner();
            Config.ConfigElement configElement = configScanner.readConfigFromLines(entityInfoArrayList);
            config.addElement(configElement);

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

            for(File file : listOfFiles) {
                String fileName = file.getName();
                ArrayList<String> lines = StringUtils.fileToArrayList(alfalfaDirectory + fileName);

                CompilerResult compilerResult = Compiler.runAlfalfa(configElement.getEntityInfo(), lines);
                Compiler.writeCompilerResultToFile(workingDirectory, compilerResult);
            }
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
    }
}
