package com.github.jamesarthurholland.alfalfa.model;

import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.Node;

public class Loop extends Node
{
    public boolean leftTreeFixed;
    public static String type = "LOOP";

    public Loop()
    {
        leftTreeFixed = false;
    }

    public void print() {
        System.out.println("loop " + "\n");
        right.print ();
        return;
    }

    public boolean isLeftTreeFixed()
    {
        return leftTreeFixed;
    }

    public String getType()
    {
        return type;
    }
}
