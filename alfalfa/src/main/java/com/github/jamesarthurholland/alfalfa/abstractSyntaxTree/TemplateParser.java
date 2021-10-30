package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.github.jamesarthurholland.alfalfa.NoPatternDirectoryException;
import com.github.jamesarthurholland.alfalfa.PatternDirectoryEmptyException;
import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel.Foldable;
import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel.Node;
import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel.Sentence;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.header.HeaderHandler;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.header.HeaderValidationResponse;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.header.InvalidHeaderException;
import com.github.jamesarthurholland.alfalfa.transpiler.TranspileResult;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Stack;
import java.util.regex.Matcher;

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

    public static void writeCompilerResultToFile(String workingDirectory, TranspileResult transpileResult)
    {
        try {
            Path parent = Paths.get(workingDirectory).resolve(Paths.get(transpileResult.getFileName())).getParent();
            Files.createDirectories(parent);
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
        } catch (IOException e) {
            e.printStackTrace();
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
                Optional<Node.Type> type = isValidLoopOpener(line);
                isValid = true;
                Foldable foldable = parseFoldable(lines, parseTree, type.get());
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

    public Foldable parseFoldable(ArrayList<String> lines, TemplateASTree parseTree, Node.Type type) {

        Foldable varLoopNode = FoldableFactory.newFoldable(type);

        lines.remove (0); 		// remove loop opener
        ArrayList<String> loopLines = new ArrayList<String> ();

        int nestLevel = 0;

        Stack<Node.Type> typesStack = new Stack<>();
        typesStack.push(type);

        nestLevel++;
        while (nestLevel != 0) {
            String subLine = lines.get(0);
            Optional<Node.Type> typeIfValidOpener = isValidLoopOpener(subLine);
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

    public Optional<Node.Type> isValidLoopOpener(String line) // TODO change to Foldable, not loop, in name
    {
        for (Node.Type type : Node.Type.values()) {
            if(type == Node.Type.SENTENCE) {
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

    public boolean isValidLoopCloser(String line, Node.Type type)
    {
        java.util.regex.Pattern varsLoopOpenerPattern = Foldable.FOLDABLE_CLOSERS.get(type);
        Matcher matcher = varsLoopOpenerPattern.matcher(line);
        return matcher.matches();
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

