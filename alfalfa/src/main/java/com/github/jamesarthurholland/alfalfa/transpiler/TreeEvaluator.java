package com.github.jamesarthurholland.alfalfa.transpiler;

import com.github.jamesarthurholland.alfalfa.StringUtils;
import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.*;
import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel.Foldable;
import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel.Node;
import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel.Sentence;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;
import com.github.jamesarthurholland.alfalfa.typeSystem.TypeSystemConverter;
import com.google.common.io.Files;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.stream.Collectors;

public class TreeEvaluator
{
    public static TranspileResult runAlfalfa(ArrayList<String> lines, Container container, Pattern pattern, TypeSystemConverter typeSystemConverter) {
        TemplateParser parser = new TemplateParser(lines);
        TemplateASTree parseTree = parser.parseTemplateLines(parser.getTemplateLines());
        String fileName = parser.getHeader().getFileName();
        // TODO pick type system here
        String tsName = null;
        if(parser.getHeader().getTypeSystemName() != null) {
            tsName = parser.getHeader().getTypeSystemName();
        }
        else {
            tsName = Files.getFileExtension(fileName);
        }
        if(pattern.mode == Pattern.ImportMode.FOR_EACH_ENTITY) {
            EntityInfo entityInfo = (EntityInfo) container.get(Container.ENTITY_INFO_KEY);
            fileName = StringUtils.evaluateForEntityReplacements(fileName, entityInfo);
        }
        fileName = pattern.injectVarsToLine(fileName);

        ArrayList<String> processedFileLines = TreeEvaluator.evaluateTree(parseTree, container, pattern, typeSystemConverter, tsName);
        processedFileLines = processedFileLines
            .stream()
            .map(pattern::injectVarsToLine)
            .collect(Collectors.toCollection(ArrayList::new));
        return new TranspileResult(fileName, processedFileLines);
    }

    // TODO refactor passing of both typeSystemConverter and tsName
    public static ArrayList<String> evaluateTree(TemplateASTree tree, Container container, Pattern pattern, TypeSystemConverter typeSystemConverter, String tsName)
    {
        Node rootNode = tree.getRoot ();

        Stack<Node> nodeStack = new Stack<>();
        nodeStack.push(rootNode);

        ArrayList<String> eval = new ArrayList<> ();

        while ( ! nodeStack.empty() ) {
            Node currentNode = nodeStack.pop();
            if (currentNode instanceof Sentence) {
                Sentence sentence = (Sentence) currentNode;
                String evaluatedSentence = null;
                if(sentence.isInLoop()) {
                    evaluatedSentence = sentence.evaluate(typeSystemConverter, tsName);
                }
                else if(pattern.mode == Pattern.ImportMode.ONCE_FOR_ENTITY) {
                    // TODO does this ever get hit? should be handled by sentence.evaluate
                    evaluatedSentence = sentence.getSentenceString();
                }
                else if(pattern.mode == Pattern.ImportMode.FOR_EACH_ENTITY) {
                    // TODO does this ever get hit? SentenceSingleEvaluator heavily duplicates.
                    EntityInfo info = (EntityInfo) container.get(Container.ENTITY_INFO_KEY);
                    evaluatedSentence = StringUtils.evaluateForEntityReplacements(sentence.getSentenceString(), info);
                    evaluatedSentence = StringUtils.evaluateForNamespace(evaluatedSentence, info);
                    evaluatedSentence = StringUtils.evaluateForTableReplacements(evaluatedSentence, info);
                }

                if (evaluatedSentence != null) {
                    eval.add(evaluatedSentence);
                }
                if (currentNode.left != null && sentence.isInLoop() == false) {
                    nodeStack.push(currentNode.left);
                }
                // TODO entities loop bug
            }

            if (currentNode instanceof Foldable) {
                Foldable foldable = (Foldable) currentNode;
                ArrayList<Node> nodes = foldable.evaluate(container);
                Collections.reverse(nodes);
                if (currentNode.left != null && currentNode.left.context == null) {
                    nodeStack.push(currentNode.left);
                }
                for(Node node : nodes) {
                    nodeStack.push(node);
                }
            }

        }

        return eval;
    }
}
