package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Variable;
import com.github.jamesarthurholland.alfalfa.transpiler.SentenceSingleEvaluator;
import com.github.jamesarthurholland.alfalfa.transpiler.VarLoopEvaluator;

import java.util.ArrayList;

public class TemplateASTree
{
    Node root;

    public TemplateASTree()
    {

    }

    public void insert(Node node)
    {
        if (root == null) {
            root = node;
            return;
        }

        Node currentNode = findCurrentNode ();
        currentNode.right = node;
    }

    public Node findCurrentNode()
    {
        Node currentNode = root;
        while (currentNode.right != null || currentNode.left != null) {
            if (currentNode.left != null) {
                if (currentNode instanceof Foldable) {
                    Foldable currentAsLoop = (Foldable) currentNode;
                    if (currentAsLoop.isLeftTreeFixed() == true) {
                        if (currentNode.right != null) {
                            currentNode = currentNode.right;
                            continue;
                        } else if (currentNode.right == null) {
                            return currentNode;
                        }
                    }
                    else if (currentAsLoop.isLeftTreeFixed() == false) {
                        currentNode = currentNode.left;
                        continue;
                    }
                }

            }
            if (currentNode.right != null) {
                currentNode = currentNode.right;
                continue;
            }
        }
        return currentNode;
    }

    public ArrayList<String> evaluateTree(TemplateASTree tree, EntityInfo info, Schema schema, Pattern pattern)
    {
        Node rootNode = tree.getRoot ();

        switch (pattern.mode) {
            case FOR_EACH_ENTITY:
                return evaluateTreeVarLoop(rootNode, info, pattern);
            case ONCE_FOR_ENTITY:
                return evaluateTreeSingleEntity(rootNode, schema, pattern);
        }

        return null;
    }

    public ArrayList<String> evaluateTreeSingleEntity(Node node, Schema schema, Pattern pattern)
    {
        ArrayList<String> eval = new ArrayList<String> ();


        if (node instanceof Sentence) {
            Sentence s = (Sentence) node;
            eval.add (s.getSentenceString());
        }

        if (node.left != null) { // node.left always a foldable subtree
            schema.getEntityInfo()
                    .forEach(entityInfo -> {
                        eval.addAll(evaluateEntitySubTree(node.left, entityInfo));
                    });
        }

        if (node.right != null) {
            eval.addAll(evaluateTreeSingleEntity(node.right, schema, pattern));
        }

        return eval;
    }

    public ArrayList<String> evaluateEntitySubTree(Node node, EntityInfo info)
    {
        ArrayList<String> eval = new ArrayList<> ();

        Node currentNode = node;

        while(currentNode != null) {
            Sentence s = (Sentence) currentNode;
            String evaluatedSentence = SentenceSingleEvaluator.evaluate(s.getSentenceString(), info);
            if (evaluatedSentence != null) {
                eval.add (evaluatedSentence);
            }
            currentNode = currentNode.right;
        }

        return eval;
    }

    public ArrayList<String> evaluateTreeVarLoop(Node node, EntityInfo info, Pattern pattern)
    {
        ArrayList<String> eval = new ArrayList<String> ();
        if (node instanceof Sentence) {
            Sentence s = (Sentence) node;
            String evaluatedSentence = "";

            evaluatedSentence = VarLoopEvaluator.evaluateForEntityReplacements(s.getSentenceString (), info);
            eval.add (evaluatedSentence);
        }

        if (node.left instanceof Foldable) { // node.left always a foldable subtree
            info.getVariables()
                    .forEach(var -> {
                        eval.addAll(evaluateVarSubTree(node.left, var, info));
                    });
        }

        if (node.right != null) {
            eval.addAll(evaluateTreeVarLoop(node.right, info, pattern));
        }

        return eval;
    }

    public ArrayList<String> evaluateVarSubTree(Node node, Variable givenVar, EntityInfo entityInfo)
    {
        ArrayList<String> eval = new ArrayList<> ();
        if (node instanceof Sentence) {
            Sentence s = (Sentence) node;
            String evaluatedSentence = VarLoopEvaluator.evaluate(s.getSentenceString(), givenVar, entityInfo);
            if (evaluatedSentence != null) {
                eval.add (evaluatedSentence);
            }
        }

        if (node.left != null) { // node.left always a loop subtree
            for(Variable var : entityInfo.getVariables()) {
                eval.addAll (evaluateVarSubTree(node.left, var, entityInfo));
            }
        }
        if (node.right != null) {
            eval.addAll(evaluateVarSubTree(node.right, givenVar, entityInfo));
        }

        return eval;
    }

    public Node getRoot() {
        return root;
    }
}
