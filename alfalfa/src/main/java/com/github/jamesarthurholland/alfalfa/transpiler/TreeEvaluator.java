package com.github.jamesarthurholland.alfalfa.transpiler;

import com.github.jamesarthurholland.alfalfa.StringUtils;
import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.*;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Stack;

public class TreeEvaluator
{
    public static TranspileResult runAlfalfa(ArrayList<String> lines, LinkedHashMap<String, Object> container, Pattern pattern) {
        TemplateParser parser = new TemplateParser(lines);
        TemplateASTree parseTree = parser.parseTemplateLines(parser.getTemplateLines());
        String fileName = parser.getHeader().getFileName();

        if(pattern.mode == Pattern.ImportMode.FOR_EACH_ENTITY) {
            EntityInfo entityInfo = (EntityInfo) container.get(TemplateParser.ENTITY_INFO_KEY);
            fileName = StringUtils.evaluateForEntityReplacements(fileName, entityInfo);
        }

        ArrayList<String> processedFileLines = TreeEvaluator.evaluateTree(parseTree, container, pattern);
        return new TranspileResult(fileName, processedFileLines);
    }

    public static ArrayList<String> evaluateTree(TemplateASTree tree, LinkedHashMap<String, Object> container, Pattern pattern)
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
                    evaluatedSentence = sentence.evaluate();
                }
                else if(pattern.mode == Pattern.ImportMode.ONCE_FOR_ENTITY) {
                    evaluatedSentence = sentence.getSentenceString();
                }
                else if(pattern.mode == Pattern.ImportMode.FOR_EACH_ENTITY) {
                    EntityInfo info = (EntityInfo) container.get(TemplateParser.ENTITY_INFO_KEY);
                    evaluatedSentence = StringUtils.evaluateForEntityReplacements(sentence.getSentenceString(), info);
                }
                if (evaluatedSentence != null) {
                    eval.add (evaluatedSentence);
                }
                if (currentNode.left != null & sentence.isInLoop() == false) {
                    nodeStack.push(currentNode.left);
                }
                // TODO entities loop bug
            }

            if (currentNode instanceof Foldable) {
                Foldable foldable = (Foldable) currentNode;
                ArrayList<Node> nodes = foldable.evaluate(container);
                Collections.reverse(nodes);
                if (currentNode.left != null) {
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
