import header.HeaderHandler;
import header.HeaderValidationResponse;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Compiler
{
    public static final String TEMPLATE_EXTENSION = "afat";
    private int nestLevel = 0;
    private ArrayList<String> templateLines;
    private Tree parseTree;
    private int lineNumber = 1;
    private HeaderValidationResponse header;

    public Compiler(ArrayList<String> lines)
    {
        HeaderValidationResponse headerValidationResponse = HeaderHandler.validateHeader(lines.get(0));
        if(headerValidationResponse.isValidHeader()) {
            lines.remove(0);
            this.header = headerValidationResponse;
            this.templateLines = lines;
            parseTree = new Tree();
        }
        else {
            throw new InvalidHeaderException();
        }
    }

    public static void main(String[] args)
    {
        try {
            String workingDirectory = System.getProperty("user.dir");
            String alfalfaDirectory = Compiler.getAlfalfaPatternDirectory() + "/";
            File folder = new File(alfalfaDirectory);
            ArrayList<File> listOfFiles = getTemplateFiles(folder.listFiles());


//
            ArrayList<String> entityInfoArrayList = ConfigScanner.fileToArrayList(System.getProperty("user.dir") + "/entity.afae");
//            ArrayList<String> entityInfoArrayList = ConfigScanner.fileToArrayList(workingDirectory + "/src/entity.afae");

            EntityInfo entityInfo = ConfigScanner.readConfig(entityInfoArrayList);

//            EntityInfo entityInfo = new EntityInfo();
//            Variable id = new Variable(true, "public", "long", "id");
//            Variable companyId = new Variable(true, "public", "long", "companyId");
//            Variable contractId = new Variable(true, "public", "String", "contractId");
//            Variable status = new Variable(true, "public", "int", "status");
//            entityInfo.entity = "Shift";
//            entityInfo.variables.add(id);
//            entityInfo.variables.add(companyId);
//            entityInfo.variables.add(contractId);
//            entityInfo.variables.add(status);

            for(File file : listOfFiles) {
                String fileName = file.getName();
                ArrayList<String> lines = fileToArrayList(alfalfaDirectory + fileName);

                CompilerResult compilerResult = Compiler.runAlfalfa(entityInfo, lines);
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
//        Tree parseTree = parser.parseLines (parser.getTemplateLines());
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

    protected static ArrayList<File> getTemplateFiles(File[] allFiles)
    {
        ArrayList<File> templates = new ArrayList<>();

        for(File file : allFiles) {
            String fileExtension = getExtension(file.getName());
            if(fileExtension.equals(Compiler.TEMPLATE_EXTENSION)) {
                templates.add(file);
            }
        }
        return templates;
    }

    protected static String getAlfalfaPatternDirectory() throws NoPatternDirectoryException, PatternDirectoryEmptyException
    {
        try {
            URL url = Compiler.class.getResource("patternDirectory.txt");
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
        Tree parseTree = compiler.parseLines (compiler.getTemplateLines());
        String fileName = compiler.evaluateForEntityReplacements(compiler.getHeader().getFileName(), entityInfo);

        return new CompilerResult(fileName, compiler.evaluateTree (parseTree, entityInfo));
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

    public Tree parseLines(ArrayList<String> lines)
    {
        Tree parseTree = new Tree ();

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
                    loopLines.add (lines.get(0));
                    lines.remove (0);
                }
                loopLines.remove (loopLines.size() - 1);
                Tree subTree = parseLines(loopLines);
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

    public ArrayList<String> evaluateTree(Tree tree, EntityInfo info)
    {
        Node rootNode = tree.getRoot ();
        return evaluateSubTree(rootNode, info);
    }

    public ArrayList<String> evaluateSubTree(Node node, EntityInfo info)
    {
        ArrayList<String> eval = new ArrayList<String> ();
        if (node instanceof Sentence) {
            Sentence s = (Sentence) node;
            String outputSentence = evaluateForEntityReplacements(s.getSentenceString (), info);
            outputSentence = evaluateForNamespace(outputSentence, info);
            eval.add (outputSentence);
        }

        if (node.left != null) { // node.left always a loop subtree
            for(Variable var : info.getVariables()) {
                eval.addAll(evaluateLoopSubTree (node.left, info, var));
            }
        }
        if (node.right != null) {
            eval.addAll(evaluateSubTree (node.right, info));
        }

        return eval;
    }

    public ArrayList<String> evaluateLoopSubTree(Node node, EntityInfo info, Variable givenVar)
    {
        ArrayList<String> eval = new ArrayList<String> ();
        if (node instanceof Sentence) {
            Sentence s = (Sentence) node;
            String evaluatedSentence = evaluateSentence (givenVar, s.getSentenceString (), info);
            if (evaluatedSentence != null) {
//					eval.add (evaluatedSentence + " " + givenVar.getName());
                eval.add (evaluatedSentence);
            }
        }

        if (node.left != null) { // node.left always a loop subtree
            for(Variable var : info.getVariables()) {
                eval.addAll (evaluateLoopSubTree (node.left, info, var));
            }
        }
        if (node.right != null) {
            eval.addAll(evaluateLoopSubTree (node.right, info, givenVar));
        }

        return eval;
    }

    String evaluateForKeyStatement (Variable givenVar, String sentence)
    {
        Pattern patternForKey = Pattern.compile("(.*)\\{\\{KEY\\}\\}(.*)\\{\\{/KEY\\}\\}.*");
        Pattern patternForKeyWholeGroup = Pattern.compile(".*(\\{\\{KEY\\}\\}.*\\{\\{/KEY\\}\\}).*"); // TODO get rid of this line?
        String generatedSentence = sentence;
        Matcher matcher = patternForKey.matcher(sentence);
        if (matcher.matches()) {
            //				Console.Write ("boom " + match.Groups [0].Value);
            if (givenVar.isPrimary ()) {
                generatedSentence = matcher.group(1) + replaceVarsInString (givenVar, matcher.group(2));
            }
            else {
                // TODO: rethink for potential multi alfalfa statements on one line!!!
                return null;
            }
        }
        return generatedSentence;
    }

    public String evaluateForNotKeyStatement (Variable givenVar, String sentence, EntityInfo entityInfo)
    {
        Pattern patternForKey = Pattern.compile("(.*)\\{\\{NOT-KEY\\s+(NOT-LAST|LAST)?\\}\\}(.*)\\{\\{/NOT-KEY\\}\\}.*");
        Pattern patternForKeyWholeGroup = Pattern.compile(".*(\\{\\{NOT-KEY\\}\\}.*\\{\\{/NOT-KEY\\}\\}).*");
        String generatedSentence = sentence;
        Matcher matcher = patternForKey.matcher(sentence);
        if (matcher.matches()) {
            if (!givenVar.isPrimary ()) {
                generatedSentence = matcher.group(1) + replaceVarsInString (givenVar, matcher.group(3));
                String lastOrNot = matcher.group(2);
                if (lastOrNot.equals("NOT-LAST")) {
                    if (!entityInfo.isVarLast (givenVar)) {
                        return generatedSentence;
                    } else {
                        return null;
                    }
                }
                if (lastOrNot.equals("LAST")) {
                    if (entityInfo.isVarLast (givenVar)) {
                        return generatedSentence;
                    } else {
                        return null;
                    }
                }
            }
            else {
                return null;
            }
        }
        return generatedSentence;
    }

    public String evaluateForNotLastStatement (Variable givenVar, String sentence, EntityInfo entityInfo)
    {
        Pattern patternForKey = Pattern.compile("(.*)\\{\\{\\s*NOT-LAST\\s*\\}\\}(.*)\\{\\{\\s*/NOT-LAST\\s*\\}\\}.*");
        Matcher matcher = patternForKey.matcher(sentence);
        if (matcher.matches()) {
            String generatedSentence = matcher.group(1) + replaceVarsInString (givenVar, matcher.group(2));
            if (!entityInfo.isVarLast (givenVar)) {
                return generatedSentence;
            } else {
                return null;
            }
        }
        return sentence;
    }

    public String evaluateForLastStatement (Variable givenVar, String sentence, EntityInfo entityInfo)
    {
        Pattern patternForKey = Pattern.compile("(.*)\\{\\{\\s*LAST\\s*\\}\\}(.*)\\{\\{\\s*/LAST\\s*\\}\\}.*");
        Matcher matcher = patternForKey.matcher(sentence);
        if (matcher.matches()) {
            String generatedSentence = matcher.group(1) + replaceVarsInString (givenVar, matcher.group(2));
            if (entityInfo.isVarLast (givenVar)) {
                return generatedSentence;
            } else {
                return null;
            }
        }
        return sentence;
    }

    public String evaluateForEntityReplacements (String sentence, EntityInfo entityInfo)
    {
        String outputString = sentence.replaceAll("\\{\\{entity\\}\\}", entityInfo.getEntity());
        outputString = outputString.replaceAll("\\{\\{en_tity\\}\\}", camelToLowerUnderScore(entityInfo.getEntity()));
        outputString = outputString.replaceAll("\\{\\{ENTITY\\}\\}", camelToUpperUnderScore(entityInfo.getEntity()));
        outputString = outputString.replaceAll("\\{\\{Entity\\}\\}", uppercaseFirst(entityInfo.getEntity()));

        return outputString;
    }

    protected String evaluateForNamespace(String sentence, EntityInfo entityInfo)
    {
        String nameSpace = entityInfo.getNameSpace();
        nameSpace = nameSpace.replaceAll("\\\\", "\\\\\\\\");
        String outputSentence = sentence.replaceAll("\\{\\{NAMESPACE\\}\\}", nameSpace);
        return outputSentence;
    }

    public String evaluateSentence(Variable givenVar, String sentence, EntityInfo entityInfo)
    {
        String generatedSentence = evaluateForEntityReplacements(sentence, entityInfo);
        generatedSentence = evaluateForNamespace(sentence, entityInfo);
        generatedSentence = evaluateForKeyStatement (givenVar, generatedSentence);

        if (generatedSentence == null) {
            return generatedSentence;
        }
        generatedSentence = evaluateForNotKeyStatement(givenVar, generatedSentence, entityInfo);
        if (generatedSentence == null) {
            return generatedSentence;
        }
        generatedSentence = evaluateForNotLastStatement(givenVar, generatedSentence, entityInfo);
        if (generatedSentence == null) {
            return generatedSentence;
        }
        generatedSentence = evaluateForLastStatement(givenVar, generatedSentence, entityInfo);
        if (generatedSentence != null) {
            generatedSentence = replaceVarsInString (givenVar, generatedSentence);
            generatedSentence = replaceTypesInString(givenVar, generatedSentence);
            generatedSentence = replaceVisibilityInString(givenVar, generatedSentence);
        }
        return generatedSentence;
    }

    public String replaceInputInString (Variable givenVar, String sentence)
    {
        String outputString = sentence.replaceAll("\\{\\{input\\}\\}", givenVar.getName());

        return outputString;
    }

    public String replaceVarsInString (Variable givenVar, String sentence)
    {
        String outputString = sentence.replaceAll("\\{\\{var\\}\\}", givenVar.getName());
        outputString = outputString.replaceAll("\\{\\{v_ar\\}\\}", camelToLowerUnderScore(givenVar.getName()));
        outputString = outputString.replaceAll("\\{\\{VAR\\}\\}", camelToUpperUnderScore(givenVar.getName()));
        outputString = outputString.replaceAll("\\{\\{Var\\}\\}", uppercaseFirst(givenVar.getName()));

        return outputString;
    }

    public String replaceTypesInString (Variable givenVar, String sentence)
    {
        String outputString = sentence.replaceAll("\\{\\{type\\}\\}", givenVar.getType());
        outputString = outputString.replaceAll("\\{\\{t_ype\\}\\}", camelToLowerUnderScore(givenVar.getType()));
        outputString = outputString.replaceAll("\\{\\{TYPE\\}\\}", camelToUpperUnderScore(givenVar.getType()));
        outputString = outputString.replaceAll("\\{\\{Type\\}\\}", uppercaseFirst(givenVar.getType()));

        return outputString;
    }

    public String replaceVisibilityInString (Variable givenVar, String sentence)
    {
        String outputString = sentence.replaceAll("\\{\\{visibility\\}\\}", givenVar.getVisibility());
        return outputString;
    }

    public String camelToLowerUnderScore(String variableString)
    {
        String StringParts = variableString.replaceAll("([A-Z])", "_$1");
        return StringParts.toLowerCase();
    }

    public String camelToUpperUnderScore(String variableString)
    {
        String StringParts = variableString.replaceAll("([A-Z])", "_$1");
        return StringParts.toUpperCase();
    }

    static String uppercaseFirst(String s)
    {
        if (s.isEmpty())
        {
            return "";
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public HeaderValidationResponse getHeader() {
        return header;
    }

    public void setHeader(HeaderValidationResponse header) {
        this.header = header;
    }
}

