package com.github.jamesarthurholland.alfalfa;

public class Tree
{
    Node root;

    public Tree()
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

    public void insert(Tree subTree)
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

    public Node getRoot() {
        return root;
    }
}
