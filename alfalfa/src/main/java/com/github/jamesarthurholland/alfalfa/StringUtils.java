package com.github.jamesarthurholland.alfalfa;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.NoEntityFileException;
import com.google.common.io.Files;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.OptionalInt;

public class StringUtils {

    public static final String TEMPLATE_EXTENSION = "afat";
    public static final String MODEL_EXTENSION = "afam";

    public static ArrayList<String> fileToArrayList(String fileName) throws NoEntityFileException // TODO move to string utils or use streams
    {
        String line = null;
        ArrayList<String> list = new ArrayList<String>();
        try {
            FileReader fileReader = new FileReader(new File(fileName));
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            while ((line = bufferedReader.readLine()) != null) {
                list.add(line);
            }
            bufferedReader.close();
        }
        catch (FileNotFoundException e) {
            throw new NoEntityFileException();
        } catch (IOException e) {
            e.printStackTrace(); // TODO make proper exception
        }
        finally {
            return list;
        }
    }

    public static String evaluateForNamespace(String sentence, EntityInfo entityInfo)
    {
        String nameSpace = entityInfo.getNameSpace();
        nameSpace = nameSpace.replaceAll("\\\\", "\\\\\\\\");
        String outputSentence = sentence.replaceAll("\\{\\{NAMESPACE\\}\\}", nameSpace);
        return outputSentence;
    }

    public static String evaluateForEntityReplacements (String sentence, EntityInfo entityInfo)
    {
        String outputString = sentence.replaceAll("\\{\\{entity\\}\\}", entityInfo.getName());
        outputString = outputString.replaceAll("\\{\\{en_tity\\}\\}", camelToLowerUnderScore(entityInfo.getName()));
        outputString = outputString.replaceAll("\\{\\{EN_TITY\\}\\}", camelToUpperUnderScore(entityInfo.getName()));
        outputString = outputString.replaceAll("\\{\\{Entity\\}\\}", uppercaseFirst(entityInfo.getName()));

        return outputString;
    }

    public static String evaluateForTableReplacements (String sentence, EntityInfo entityInfo)
    {
        if(entityInfo.getTableName().isPresent()) {
            String tableName = entityInfo.getTableName().get();
            String outputString = sentence.replaceAll("\\{\\{table\\}\\}", tableName);
            outputString = outputString.replaceAll("\\{\\{ta_ble\\}\\}", camelToLowerUnderScore(tableName));
            outputString = outputString.replaceAll("\\{\\{TA_BLE\\}\\}", camelToUpperUnderScore(tableName));
            outputString = outputString.replaceAll("\\{\\{Table\\}\\}", uppercaseFirst(tableName));

            return outputString;
        }

        return sentence;
    }



    public static Path processPathEntityReplacement(Path fileOutputDirectoryPath, EntityInfo info) {
        return Paths.get(StringUtils.evaluateForEntityReplacements(fileOutputDirectoryPath.toString(), info));
    }

    public static boolean fileIsModelFile(Path path)
    {
        return Files.getFileExtension(path.toString()).equals(StringUtils.MODEL_EXTENSION);
    }

    public static boolean fileIsTemplateFile(Path path)
    {
        if(java.nio.file.Files.isDirectory(path)) {
            return false;
        }
        return Files.getFileExtension(path.toString()).equals(StringUtils.TEMPLATE_EXTENSION);
    }

    public static String camelToLowerUnderScore(String variableString)
    {
        OptionalInt firstChar = variableString.chars().findFirst();

        if ( firstChar.isPresent() ) {
            char first = (char) firstChar.getAsInt();
            if(Character.isUpperCase(first) && numberOfUpperCases(variableString) == 1) {
                return variableString.toLowerCase();
            }
        }
        String StringParts = variableString.replaceAll("([A-Z])", "_$1");
        return StringParts.toLowerCase();
    }

    public static int numberOfUpperCases(String givenString) {
        return (int) givenString.chars().filter((s)->Character.isUpperCase(s)).count();
    }

    public static String camelToUpperUnderScore(String variableString)
    {
        OptionalInt firstChar = variableString.chars().findFirst();

        if ( firstChar.isPresent() ) {
            char first = (char) firstChar.getAsInt();
            if(Character.isUpperCase(first) && numberOfUpperCases(variableString) == 1) {
                return variableString.toUpperCase();
            }
        }
        String StringParts = variableString.replaceAll("([A-Z])", "_$1");
        return StringParts.toUpperCase();
    }

    public static String uppercaseFirst(String s)
    {
        if (s.isEmpty())
        {
            return "";
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
