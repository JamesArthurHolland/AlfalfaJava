package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.github.jamesarthurholland.alfalfa.NoPatternDirectoryException;
import com.github.jamesarthurholland.alfalfa.PatternDirectoryEmptyException;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Stack;
import java.util.regex.Matcher;

public class TemplateParser extends BaseTemplateParser
{
    public final static String ENTITY_INFO_KEY = "ENTITY_INFO";
    public final static String SCHEMA_KEY = "SCHEMA";

    public TemplateParser(ArrayList<String> lines) {
        super(lines);
    }

    public static void writeCompilerResultToFile(String workingDirectory, TranspileResult transpileResult)
    {
        try {
            PrintWriter writer = new PrintWriter("" + workingDirectory + "/" + transpileResult.getFileName(), "UTF-8");
            for(String line : transpileResult.getOutput()) {
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

    protected static String getAlfalfaPatternDirectory() throws NoPatternDirectoryException, PatternDirectoryEmptyException
    {
        try {
            URL url = TemplateParser.class.getResource("main/patternDirectory.txt");
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



    public TemplateASTree parseTemplateLines(ArrayList<String> lines)
    {
        TemplateASTree parseTree = new TemplateASTree();

        String line;
        while (lines.size() != 0) {
            line = lines.get(0);

            boolean isValid = false;
            if (isValidLoopOpener(line).isPresent()) {
                Optional<Foldable.Types> type = isValidLoopOpener(line);
                isValid = true;
                Foldable foldable = parseFoldable(lines, type.get());
                parseTree.insert(foldable);
            }
            if (isValid == false) {
                Sentence sentenceNode = new Sentence (line);
                lines.remove (0);
                parseTree.insert(sentenceNode);
            }
        }

        return parseTree;
    }

    public Foldable parseFoldable(ArrayList<String> lines, Foldable.Types type) {

        Foldable varLoopNode = FoldableFactory.newFoldable(type);

        lines.remove (0); 		// remove loop opener
        ArrayList<String> loopLines = new ArrayList<String> ();

        int nestLevel = 0;

        Stack<Foldable.Types> typesStack = new Stack<>();
        typesStack.push(type);

        nestLevel++;
        while (nestLevel != 0) {
            String subLine = lines.get(0);
            Optional<Foldable.Types> typeIfValidOpener = isValidLoopOpener(subLine);
            if(typeIfValidOpener.isPresent()) {
                typesStack.push(typeIfValidOpener.get());
                nestLevel++;
            }
            if (isValidLoopCloser(subLine, typesStack.peek())) {
                typesStack.pop();
                nestLevel--;
            }
            loopLines.add (lines.get(0)); // TODO use remove, it passes back
            lines.remove (0);
        }
        loopLines.remove (loopLines.size() - 1);
        TemplateASTree subTree = parseTemplateLines(loopLines);
        varLoopNode.setRightNodeFixed();
        varLoopNode.right = subTree.getRoot();

        return varLoopNode;
    }

    public Optional<Foldable.Types> isValidLoopOpener(String line) // TODO change to Foldable, not loop, in name
    {
        for (Foldable.Types type : Node.Types.values()) {
            if(type == Node.Types.SENTENCE) {
                continue;
            }
            java.util.regex.Pattern varsLoopOpenerPattern = Foldable.FOLDABLE_OPENERS.get(type); // TODO there was $ at end. assumed mistake. inspect
            Matcher matcher = varsLoopOpenerPattern.matcher(line);
            if (matcher.matches()) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    public boolean isValidLoopCloser(String line, Foldable.Types type)
    {
        java.util.regex.Pattern varsLoopOpenerPattern = Foldable.FOLDABLE_CLOSERS.get(type);
        Matcher matcher = varsLoopOpenerPattern.matcher(line);
        return matcher.matches();
    }


}

