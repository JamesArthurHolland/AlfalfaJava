import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ConfigScanner
{
    public static ArrayList<String> fileToArrayList(String fileName) throws NoEntityFileException
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

    public static EntityInfo readConfig(ArrayList<String> entity)
    {
        EntityInfo entityInfo = new EntityInfo();
//        ArrayList<String> entity = fileToArrayList(fileName);
        boolean entityParsed = false;

        for(String line : entity)
        {
            // Get entity name from given config file, if there is one.
            if(entityParsed == false)  {
                entityInfo.setEntity(line.substring(3));
                entityParsed = true;
                continue;
            }

            Variable var = new Variable();
//            String chop = line;

            // Get the entity's key variable ( id in most cases)
            if(line.startsWith("-n"))  {
//                Pattern patternForNameSpace = Pattern.compile("-n\s(w+\\w+)");
//                Matcher matcher = patternForNameSpace.matcher(line);
//                MatchCollection matches = Regex.Matches(line, pattern);
//                entityInfo.setNameSpace(matches[0].Groups[1].Value);
                entityInfo.setNameSpace(line.substring(3));
                continue;
            }

            if(line.startsWith("-db"))  {
                entityInfo.setDbName(line.substring(4));
                continue;
            }

            if(line.startsWith("-k"))  {
                var.setPrimary(true);
                var.setVisibility("protected");
                var.setType("long");
                var.setName(line.substring(3));
                entityInfo.getVariables().add(var);
                continue;
            }

            // p prefix means public, pr means private, all other variables assumed protected.
//            if(line.startsWith("public")) {
//                var.setVisibility("public");
//                line = line.substring(7);
//            }
//            else if(line.startsWith("private")) {
//                var.setVisibility("private");
//                line = line.substring(8);
//            }
//            else if(line.startsWith("protected")){
//                var.setVisibility("protected");
//                line = line.substring(10);
//            }

            //TODO here one must get the correct pattern for the file type, as members varaibles have different structures
            Pattern parseTypeAndNamePattern = Pattern.compile("\\s*(\\w+)\\s+(\\w+)\\s+(\\w+);\\s*");
            Matcher typeAndName = parseTypeAndNamePattern.matcher(line);

            if(typeAndName.matches()) { // WORKS FOR JAVA ONLY, need filetype by this stage to determine token recognisation function
                var.setVisibility(typeAndName.group(1));
                var.setType(typeAndName.group(2));
                var.setName(typeAndName.group(3));
            }
            else {
                System.out.println("No match " + line);
            }

            entityInfo.getVariables().add(var);
        }
        return entityInfo;
    }
}