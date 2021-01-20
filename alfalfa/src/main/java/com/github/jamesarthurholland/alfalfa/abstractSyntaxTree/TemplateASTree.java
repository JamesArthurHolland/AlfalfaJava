package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel.Foldable;
import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel.Node;

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
        currentNode.left = node;
    }

    public Node findCurrentNode()
    {
        Node currentNode = root;
        while (currentNode.left != null || currentNode.right != null) {
            if (currentNode.right != null) {
                if (currentNode instanceof Foldable) {
                    Foldable currentAsLoop = (Foldable) currentNode;
                    if (currentAsLoop.isRightNodeFixed() == true) {
                        if (currentNode.left != null) {
                            currentNode = currentNode.left;
                            continue;
                        } else if (currentNode.left == null) {
                            return currentNode;
                        }
                    }
                    else if (currentAsLoop.isRightNodeFixed() == false) {
                        currentNode = currentNode.right;
                        continue;
                    }
                }

            }
            if (currentNode.left != null) {
                currentNode = currentNode.left;
                continue;
            }
        }
        return currentNode;
    }

    public Node getRoot() {
        return root;
    }
}
