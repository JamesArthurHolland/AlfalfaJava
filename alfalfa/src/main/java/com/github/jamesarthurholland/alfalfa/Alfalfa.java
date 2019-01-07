package com.github.jamesarthurholland.alfalfa;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Alfalfa
{
    protected String path;

    static void Main() {
        Alfalfa app = new Alfalfa ();
//        ConfigScanner scanner = new ConfigScanner();
        EntityInfo entityInfo = new EntityInfo(); //scanner.scan("name.txt");

//        Console.Write ("Main namespace is " + entityInfo.getNameSpace () + "==\n");
//        app = app.createFolder(app, entityInfo);

        //			app.makeFile(app, entityInfo, @"Nesting", "FrontDi");

        //PHP
//        app.makeFile(app, entityInfo, "entityTemplate.txt", "" + Alfalfa.uppercaseFirst(entityInfo.getName()) + "Entity.php");
//        app.makeFile(app, entityInfo, "filterTemplate.txt", "" + Alfalfa.uppercaseFirst(entityInfo.getName()) + "Filter.php");
//        app.makeFile(app, entityInfo, "mapperTemplate.txt", "" + Alfalfa.uppercaseFirst(entityInfo.getName()) + "Mapper.php");
//        app.makeFile(app, entityInfo, "serviceTemplate.txt", "" + Alfalfa.uppercaseFirst(entityInfo.getName()) + "Service.php");
//        app.makeFile(app, entityInfo, "databaseTemplate.txt", "" + Alfalfa.uppercaseFirst(entityInfo.getName()) + "Db.php");
//        app.makeFile(app, entityInfo, "FrontEndDi.txt", "" + Alfalfa.uppercaseFirst(entityInfo.getName()) + "FrontDi.php");
//        app.makeFile(app, entityInfo, "ServicesDi.txt", "" + Alfalfa.uppercaseFirst(entityInfo.getName()) + "ServiceDi.php");
//
//        //JAVA
//        app.makeFile(app, entityInfo, "AndroidEntity.txt", "" + Alfalfa.uppercaseFirst(entityInfo.getName()) + ".java");
//        app.makeFile(app, entityInfo, "AndroidSqliteTemplate.txt", "" + Alfalfa.uppercaseFirst(entityInfo.getName()) + "Db.java");
//
//			Parser parser = new Parser(@"/home/jamie/Alfalfa/Alfalfa/Tests/TestTemplates/NotLastLast.txt");
//			Tree parseTree = parser.parseLines (parser.getTemplateLines());
//			List<String> evalResult = parser.evaluateTree (parseTree, entityInfo);
    }

//    protected Alfalfa createFolder(Alfalfa app, EntityInfo entityInfo)
//    {
//        String pathString = System.IO.Path.Combine(@"../GeneratedLibraries", "asa-" + entityInfo.getName());
//        app.path = pathString;
//        System.(pathString);
//
//        return app;
//    }

    public static String uppercaseFirst(String s)
    {
        if (s.isEmpty())
        {
            return "";
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public ArrayList<String> fileToArrayList(String fileName) {
        try {
            Scanner s = new Scanner(new File(fileName));
            ArrayList<String> list = new ArrayList<String>();
            while (s.hasNext()) {
                list.add(s.next());
            }
            s.close();
            return  list;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

//    protected void makeFile(Alfalfa app, EntityInfo entityInfo, String templateName, String outputFileName)
//    {
//        String[] entityTemplate = System.IO.File.ReadAllLines(@"template/" + templateName);
//        String fileName = System.IO.Path.Combine(path, outputFileName);
//
//        Parser parser = new Parser(@"/home/jamie/Alfalfa/Alfalfa/src/template/" + templateName);
//        Tree parseTree = parser.parseLines (parser.getTemplateLines());
//
//        ArrayList<String> evalResult = parser.evaluateTree (parseTree, entityInfo);
//
//        using (System.IO.StreamWriter file = new System.IO.StreamWriter(fileName)) {
//            foreach (String line : evalResult) {
//                file.WriteLine (line);
//            }
//        }
//    }
}
