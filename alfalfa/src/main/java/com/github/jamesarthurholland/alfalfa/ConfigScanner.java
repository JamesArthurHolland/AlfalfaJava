package com.github.jamesarthurholland.alfalfa;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ConfigScanner
{
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

    private static boolean isVariableDefinitionSemanticallyCorrect() // TODO
    {
        return true;
//        return lineArray.length != 4
    }

    private static boolean isVariableDefinition(String[] lineArray) throws Exception
    {
        if(lineArray[0].equals("k") || lineArray[0].equals("v")) {
            if(isVariableDefinitionSemanticallyCorrect() == false) {
                throw new Exception("Variable definitions must be of the form: <k|v> <i|o|u> <var type> <var name>");
            }

            return true;
        }
        return false;
    }

    public static EntityInfo readConfig(ArrayList<String> entity)
    {
        EntityInfo entityInfo = new EntityInfo();
//        ArrayList<String> name = fileToArrayList(fileName);
        boolean entityParsed = false;

        for(String line : entity)
        {
            // Get entity name from given config file, if there is one.
            if(entityParsed == false)  {
                entityInfo.setName(line);
                entityParsed = true;
                continue;
            }

            Variable var = new Variable();
//            String chop = line;

            // Get the name's key variable ( id in most cases)
            if(line.startsWith("-n"))  {
//                Pattern patternForNameSpace = Pattern.compile("-n\s(w+\\w+)");
//                Matcher matcher = patternForNameSpace.matcher(line);
//                MatchCollection matches = Regex.Matches(line, pattern);
//                entityInfo.setNameSpace(matches[0].Groups[1].Value);
//                entityInfo.setNameSpace(line.substring(3));
                continue;
            }

            if(line.startsWith("-db"))  {
//                entityInfo.setDbName(line.substring(4));
                continue;
            }

            String[] lineArray = line.split("[ ]+");

            try {
                if(isVariableDefinition(lineArray))  {
                    if(lineArray[0].equals("k")) {
                        var.setPrimary(true);
                    }
                    else {
                        var.setPrimary(false);
                    }

                    if( ! (lineArray[1].equals("i") || lineArray[1].equals("o") || lineArray[1].equals("u")) ) {
                        throw new Exception("Second token on variable definition line must be < i (private) | o (protected)| u (public) >");
                    }
                    if(lineArray[1].equals("i")) {
                        var.setVisibility(Variable.PRIVATE);
                    }
                    if(lineArray[1].equals("o")) {
                        var.setVisibility(Variable.PROTECTED);
                    }
                    if(lineArray[1].equals("u")) {
                        var.setVisibility(Variable.PUBLIC);
                    }

                    var.setType(lineArray[2]);
                    var.setName(lineArray[3]);
                    entityInfo.getVariables().add(var);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entityInfo;
    }
}
