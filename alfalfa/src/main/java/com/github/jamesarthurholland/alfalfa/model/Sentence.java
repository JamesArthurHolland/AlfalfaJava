package com.github.jamesarthurholland.alfalfa.model;

import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.Node;

public class Sentence extends Node
{
    private String sentence;
    public static final String type = "SENTENCE";

    public Sentence(String givenSentence)
    {
        this.sentence = givenSentence;
    }

    public void print() {
        System.out.println("sentence " + sentence + "\n");
        if (left != null) {
            left.print ();
        } else {
            System.out.println ("left null \n");
        }
        if (right != null) {
            right.print ();
        } else {
            System.out.println ("right null \n");
        }
    }

    public String getSentenceString()
    {
        return this.sentence;
    }

    public String getType()
    {
        return type;
    }
}
