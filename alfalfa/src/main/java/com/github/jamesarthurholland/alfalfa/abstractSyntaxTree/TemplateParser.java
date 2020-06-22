package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.github.jamesarthurholland.alfalfa.NoPatternDirectoryException;
import com.github.jamesarthurholland.alfalfa.PatternDirectoryEmptyException;
import com.github.jamesarthurholland.alfalfa.SentenceEvaluator;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.header.HeaderHandler;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.header.HeaderValidationResponse;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.header.InvalidHeaderException;
import com.github.jamesarthurholland.alfalfa.model.EntityInfo;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateParser
{

    private ArrayList<String> templateLines;
    private HeaderValidationResponse header;

    public TemplateParser(ArrayList<String> lines)
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



    public static void writeCompilerResultToFile(String workingDirectory, TemplateParseResult templateParseResult)
    {
        try {
            PrintWriter writer = new PrintWriter("" + workingDirectory + "/" + templateParseResult.getFileName(), "UTF-8");
            for(String line : templateParseResult.getOutput()) {
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

    public static TemplateParseResult runAlfalfa(EntityInfo entityInfo, ArrayList<String> lines) {
        TemplateParser parser = new TemplateParser(lines);
        TemplateASTree parseTree = parser.parseTemplateLines(parser.getTemplateLines());
        String fileName = SentenceEvaluator.evaluateForEntityReplacements(parser.getHeader().getFileName(), entityInfo);

        return new TemplateParseResult(fileName, parseTree.evaluateTree (parseTree, entityInfo));
    }

    public TemplateASTree parseTemplateLines(ArrayList<String> lines)
    {
        TemplateASTree parseTree = new TemplateASTree();

        String line;
        while (lines.size() != 0) {
            line = lines.get(0);

            boolean isValid = false;
            for (Foldable.Types type : Foldable.Types.values()) {
                if (isValidLoopOpenerForType(line, type)) {
                    isValid = true;
                    parseTree = parseFoldable(lines, parseTree, type);
                }
            }
            if (isValid == false) {
                Sentence sentenceNode = new Sentence (line);
                lines.remove (0);
                parseTree.insert(sentenceNode);
            }
        }

        return parseTree;
    }

    public TemplateASTree parseFoldable(ArrayList<String> lines, TemplateASTree parseTree, Foldable.Types type) {
        Foldable varLoopNode = new Foldable(type);

        lines.remove (0); 		// remove loop opener
        ArrayList<String> loopLines = new ArrayList<String> ();

        int nestLevel = 0;

        nestLevel++;
        while (nestLevel != 0) {
            String subLine = lines.get(0);
            if(isValidLoopOpenerForType(subLine, type)) {
                nestLevel++;
            }
            if (isValidLoopCloserForType(subLine, type)) {
                nestLevel--;
            }
            loopLines.add (lines.get(0)); // TODO use remove, it passes back
            lines.remove (0);
        }
        loopLines.remove (loopLines.size() - 1);
        TemplateASTree subTree = parseTemplateLines(loopLines);
        varLoopNode.fixLeftTree();
        varLoopNode.left = subTree.getRoot();
        parseTree.insert (varLoopNode);

        return parseTree;
    }

    public boolean isValidLoopOpenerForType(String line, Foldable.Types type)
    {
        Pattern varsLoopOpenerPattern = Pattern.compile(Foldable.FOLDABLE_KEYS.get(type)); // TODO there was $ at end. assumed mistake. inspect
        Matcher matcher = varsLoopOpenerPattern.matcher(line);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public boolean isValidLoopCloserForType(String line, Foldable.Types type)
    {
        Pattern varsLoopOpenerPattern = Pattern.compile(Foldable.FOLDABLE_KEYS.get(type));
        Matcher matcher = varsLoopOpenerPattern.matcher(line);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public ArrayList<String> getTemplateLines(){
        return templateLines;
    }

    public HeaderValidationResponse getHeader() {
        return header;
    }

    public void setHeader(HeaderValidationResponse header) {
        this.header = header;
    }
}

