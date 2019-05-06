package com.github.jamesarthurholland.alfalfa;

import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.PatternASTree;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.header.InvalidHeaderException;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.header.HeaderHandler;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.header.HeaderValidationResponse;
import com.github.jamesarthurholland.alfalfa.model.EntityInfo;
import com.github.jamesarthurholland.alfalfa.model.Loop;
import com.github.jamesarthurholland.alfalfa.model.Sentence;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Compiler
{

    private ArrayList<String> templateLines;
    private int lineNumber = 1;
    private HeaderValidationResponse header;

    public Compiler(ArrayList<String> lines)
    {
        HeaderValidationResponse headerValidationResponse = HeaderHandler.validateHeader(lines.get(0));
        if(headerValidationResponse.isValidHeader()) {
            lines.remove(0);
            this.header = headerValidationResponse;
            this.templateLines = lines;
        }
        else {
            throw new InvalidHeaderException();
        }
    }



    protected static void writeCompilerResultToFile(String workingDirectory, CompilerResult compilerResult)
    {
        try {
            PrintWriter writer = new PrintWriter("" + workingDirectory + "/" + compilerResult.getFileName(), "UTF-8");
            for(String line : compilerResult.getOutput()) {
                writer.println(line);
            }
            writer.close();
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        catch(UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
    }

//    protected void makeFile(Alfalfa app, EntityInfo entityInfo, String templateName, String outputFileName)
//    {
//        string[] entityTemplate = System.IO.File.ReadAllLines(@"template/" + templateName);
//        string fileName = System.IO.Path.Combine(path, outputFileName);
//
//        Parser parser = new Parser(@"/home/jamie/Alfalfa/Alfalfa/src/template/" + templateName);
//        PatternASTree parseTree = parser.parseLines (parser.getTemplateLines());
//
//        List<string> evalResult = parser.evaluateTree (parseTree, entityInfo);
//
//        using (System.IO.StreamWriter file = new System.IO.StreamWriter(fileName)) {
//            foreach (string line in evalResult) {
//            file.WriteLine (line);
//            }
//        }
//    }
    protected static String getExtension(String fileName)
    {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i+1);
        }
        return extension;
    }



    protected static String getAlfalfaPatternDirectory() throws NoPatternDirectoryException, PatternDirectoryEmptyException
    {
        try {
            URL url = Compiler.class.getResource("main/patternDirectory.txt");
            if(url == null) {
                throw new NoPatternDirectoryException();
            }
            File file = new File(url.getPath());
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String patternDirectory = bufferedReader.readLine();
            if(patternDirectory == null) {
                throw new PatternDirectoryEmptyException();
            }

            return patternDirectory;
        }
        catch (NullPointerException e) {
            throw new NoPatternDirectoryException();
        }
        catch (FileNotFoundException e) {
            throw new NoPatternDirectoryException();
        }
        catch (IOException e) {
            throw new PatternDirectoryEmptyException();
        }
    }

    public static CompilerResult runAlfalfa(EntityInfo entityInfo, ArrayList<String> lines) {
        Compiler compiler = new Compiler(lines);
        PatternASTree parseTree = compiler.parseLines (compiler.getTemplateLines());
        String fileName = SentenceEvaluator.evaluateForEntityReplacements(compiler.getHeader().getFileName(), entityInfo);

        return new CompilerResult(fileName, parseTree.evaluateTree (parseTree, entityInfo));
    }

    public static ArrayList<String> fileToArrayList(String fileName) {
        try {
            Scanner s = new Scanner(new File(fileName));
            ArrayList<String> list = new ArrayList<String>();
            while (s.hasNextLine()) {
                list.add(s.nextLine());
            }
            s.close();
            return  list;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isNestingClosureComplete()
    {
        int nestLevel = 0;
        for (int i = 0; i < this.templateLines.size(); i++) {
            String line = this.templateLines.get(i);
            if(isValidLoopOpener(line)) {
                nestLevel++;
            }
            if (isValidLoopCloser (line)) {
                nestLevel--;
            }
        }
        if (nestLevel == 0) {
            return true;
        }
        return false;
    }

    public PatternASTree parseLines(ArrayList<String> lines)
    {
        PatternASTree parseTree = new PatternASTree();

        int nestLevel = 0;
        String line;
        while (lines.size() != 0) {
//				Console.Write ("its " + lines.size() + "\n");
            line = lines.get(0);
            if (isValidLoopOpener(line)) {
                //TODO get the options, validate they are ok for loop
//					Console.Write ("loopopened " + line + "\n");
                Loop loopNode = new Loop ();

                lines.remove (0); 		// remove loop opener
                ArrayList<String> loopLines = new ArrayList<String> ();

                nestLevel++;
                while (nestLevel != 0) {
//						Console.Write ("nest " + nestLevel + "\n");
                    String subLine = lines.get(0);
                    if(isValidLoopOpener(subLine)) {
                        nestLevel++;
                    }
                    if (isValidLoopCloser(subLine)) {
                        nestLevel--;
                    }
                    loopLines.add (lines.get(0)); // TODO use remove, it passes back
                    lines.remove (0);
                }
                loopLines.remove (loopLines.size() - 1);
                PatternASTree subTree = parseLines(loopLines);
                loopNode.leftTreeFixed = true;
                loopNode.left = subTree.getRoot();
                parseTree.insert (loopNode);

            } else {
                Sentence sentenceNode = new Sentence (line);
                lines.remove (0);
                parseTree.insert(sentenceNode);
            }
        }

        return parseTree;
    }


    public boolean isValidLoopOpener(String line)
    {
        Pattern varsLoopOpenerPattern = Pattern.compile("^\\s*\\{\\{VARS\\}\\}\\s*$"); // TODO there was $ at end. assumed mistake. inspect
        Matcher matcher = varsLoopOpenerPattern.matcher(line);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public boolean isValidLoopCloser(String line)
    {
        Pattern varsLoopOpenerPattern = Pattern.compile("^\\s*\\{\\{/VARS\\}\\}\\s*$");
        Matcher matcher = varsLoopOpenerPattern.matcher(line);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public ArrayList<String> getTemplateLines(){
        return templateLines;
    }






//    protected String evaluateForNamespace(String sentence, EntityInfo entityInfo)
//    {
//        String nameSpace = entityInfo.getNameSpace();
//        nameSpace = nameSpace.replaceAll("\\\\", "\\\\\\\\");
//        String outputSentence = sentence.replaceAll("\\{\\{NAMESPACE\\}\\}", nameSpace);
//        return outputSentence;
//    }


    public HeaderValidationResponse getHeader() {
        return header;
    }

    public void setHeader(HeaderValidationResponse header) {
        this.header = header;
    }
}

