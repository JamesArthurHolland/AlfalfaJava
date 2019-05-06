package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.github.jamesarthurholland.alfalfa.SentenceEvaluator;
import com.github.jamesarthurholland.alfalfa.model.EntityInfo;
import com.github.jamesarthurholland.alfalfa.model.Loop;
import com.github.jamesarthurholland.alfalfa.model.Sentence;
import com.github.jamesarthurholland.alfalfa.model.Variable;

import java.util.ArrayList;

public class PatternASTree
{
    Node root;

    public PatternASTree()
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

    public void insert(PatternASTree subTree)
    {
        Node currentNode = findCurrentNode();

        Node node = subTree.getRoot ();
        currentNode.left = node;
    }

    public Node findCurrentNode()
    {
        Node currentNode = root;
        while (currentNode.right != null || currentNode.left != null) {
            if (currentNode.left != null) {
                if (currentNode instanceof Loop) {
                    Loop currentAsLoop = (Loop) currentNode;
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

    public ArrayList<String> evaluateTree(PatternASTree tree, EntityInfo info)
    {
        Node rootNode = tree.getRoot ();
        return evaluateSubTree(rootNode, info);
    }

    public ArrayList<String> evaluateSubTree(Node node, EntityInfo info)
    {
        ArrayList<String> eval = new ArrayList<String> ();
        if (node instanceof Sentence) {
            Sentence s = (Sentence) node;
            String outputSentence = SentenceEvaluator.evaluateForEntityReplacements(s.getSentenceString (), info);
//            outputSentence = evaluateForNamespace(outputSentence, info);
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
            String evaluatedSentence = SentenceEvaluator.evaluateSentence (givenVar, s.getSentenceString (), info);
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



    public Node getRoot() {
        return root;
    }
}
