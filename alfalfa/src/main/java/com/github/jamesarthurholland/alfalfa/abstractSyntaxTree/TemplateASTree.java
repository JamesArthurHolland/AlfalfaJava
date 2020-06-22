package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.github.jamesarthurholland.alfalfa.transpiler.SentenceEvaluator;
import com.github.jamesarthurholland.alfalfa.transpiler.SentenceForEachEntityEvaluator;
import com.github.jamesarthurholland.alfalfa.transpiler.SentenceSingleEvaluator;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.pattern.Pattern.ImportMode;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Schema;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Variable;
import org.jsoup.select.Evaluator;

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

    public void insert(TemplateASTree subTree)
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
        return evaluateTree(rootNode, info, schema, pattern, false);
    }

    public ArrayList<String> evaluateTree(Node node, EntityInfo info, Schema schema, Pattern pattern, boolean isFoldableSubTree)
    {
        ArrayList<String> eval = new ArrayList<String> ();
        if (node instanceof Sentence) {
            Sentence s = (Sentence) node;
            String evaluatedSentence = "";
            if (pattern.mode == ImportMode.FOR_EACH_ENTITY) {
                evaluatedSentence = SentenceForEachEntityEvaluator.evaluateForEntityReplacements(s.getSentenceString (), info);
            } else if (pattern.mode == ImportMode.ONCE_FOR_ENTITY) {
                evaluatedSentence = SentenceSingleEvaluator.evaluate(s.getSentenceString(), info);
            }

            if (evaluatedSentence != null && isFoldableSubTree) {
                eval.add (evaluatedSentence);
            }
            else {
                eval.add (evaluatedSentence);
            }
        }

        if (node.left != null && node.left instanceof Foldable) { // node.left always a foldable subtree
            switch (pattern.mode) {
                case FOR_EACH_ENTITY:
                    info.getVariables()
                            .forEach(var -> {
                                SentenceForEachEntityEvaluator evaluator = new SentenceForEachEntityEvaluator(var, info)
                                eval.addAll(evaluateVarSubTree(node.left, evaluator));
                            });
                    break;
                case ONCE_FOR_ENTITY:
                    schema.getEntityInfo()
                            .forEach(entityInfo -> {
                                SentenceSingleEvaluator evaluator = new SentenceSingleEvaluator(entityInfo);
                                eval.addAll(evaluateEntitySubTree(node.left, evaluator));
                            });
                    break;
            }
        }

        if (node.right != null) {
            eval.addAll(evaluateTree(node.right, info, schema, pattern, false));
        }

        return eval;
    }

    public ArrayList<String> evaluateEntitySubTree(Node node, SentenceEvaluator evaluator)
    {
        ArrayList<String> eval = new ArrayList<> ();
        if (node instanceof Sentence) {
            Sentence s = (Sentence) node;
            String evaluatedSentence = evaluator.evaluate(s.getSentenceString());
            if (evaluatedSentence != null) {
                eval.add (evaluatedSentence);
            }
        }

        if (node.left != null) { // node.left always a loop subtree
            eval.addAll(evaluateEntitySubTree(node.left, evaluator));
        }
        if (node.right != null) {
            eval.addAll(evaluateEntitySubTree(node.right, evaluator));
        }

        return eval;
    }

    public ArrayList<String> evaluateVarSubTree(Node node, SentenceEvaluator evaluator)
    {
        ArrayList<String> eval = new ArrayList<String> ();
        if (node instanceof Sentence) {
            Sentence s = (Sentence) node;
            String evaluatedSentence = SentenceForEachEntityEvaluator.evaluate(s.getSentenceString ());
            if (evaluatedSentence != null) {
                eval.add (evaluatedSentence);
            }
        }

        if(node instanceof Foldable) {
            Foldable foldable = (Foldable) node;

            for(Variable var : info.getVariables()) {
                SentenceForEachEntityEvaluator newEvaluator = new SentenceForEachEntityEvaluator(var, evaluator);
                eval.addAll (evaluateVarSubTree(node.left, info, var));
            }
        }

        if (node.left != null) { // node.left always a loop subtree
            for(Variable var : info.getVariables()) {
                SentenceForEachEntityEvaluator newEvaluator = new SentenceForEachEntityEvaluator(var, evaluator);
                eval.addAll (evaluateVarSubTree(node.left, info, var));
            }
        }
        if (node.right != null) {
            eval.addAll(evaluateVarSubTree(node.right, info, givenVar));
        }

        return eval;
    }

    public Node getRoot() {
        return root;
    }
}
