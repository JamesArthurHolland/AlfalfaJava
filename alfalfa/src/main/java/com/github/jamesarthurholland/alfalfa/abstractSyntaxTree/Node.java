package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.google.gson.Gson;

public abstract class Node
{
    public Node left, right;

    public Node()
    {
        left = null;
        right = null;
    }

    public Node(Node left, Node right) {
        this.left = left;
        this.right = right;
    }

    public static Node copy(Node node) {
        Gson gson = new Gson();

        try {
            if(node instanceof Sentence) {
                return (Node) node.clone();
            }
            if(node instanceof Foldable) {
                switch (((Foldable) node).type) {
                    case VAR_LOOP:
                        return new VarLoop(((VarLoop)node));
                    case ENTITY_LOOP:
                        return new EntityLoop((EntityLoop)node);
                    case VAR_CONDITIONAL:
                        // TODO
                        break;
                }
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract void print ();
}
