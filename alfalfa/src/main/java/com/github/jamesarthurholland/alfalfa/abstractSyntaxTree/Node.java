package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

public abstract class Node
{
    public Node left, right;

    public Node()
    {
        left = null;
        right = null;
    }

    public abstract void print ();
    public abstract String getType ();
}
