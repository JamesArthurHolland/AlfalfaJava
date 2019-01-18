package com.github.jamesarthurholland.alfalfa;

import java.io.*;
import java.util.ArrayList;

public class StringUtils {
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
}
