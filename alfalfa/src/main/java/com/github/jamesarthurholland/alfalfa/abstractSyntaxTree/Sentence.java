package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.github.jamesarthurholland.alfalfa.transpiler.SentenceEvaluator;

public class Sentence extends Node implements Cloneable
{
    private String sentence;
    private SentenceEvaluator evaluator;
    public static final String type = "SENTENCE";

    public Sentence(String givenSentence)
    {
        this.sentence = givenSentence;
    }

    public Sentence(String sentence, SentenceEvaluator evaluator) {
        this.sentence = sentence;
        this.evaluator = evaluator;
    }

    public Sentence(Node left, Node right, String sentence, SentenceEvaluator evaluator) {
        super(left, right);
        this.sentence = sentence;
        this.evaluator = evaluator;
    }

    public boolean isInLoop() {
        return evaluator != null;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        SentenceEvaluator evaluator = null;
        if(this.evaluator != null) {
            evaluator = (SentenceEvaluator) this.evaluator.clone();
        }
        return new Sentence(this.left, this.right, this.sentence, evaluator);
    }

    public Sentence(Sentence other) {
        this.sentence = other.sentence;
        this.evaluator = (SentenceEvaluator) other.evaluator.clone();

        if(other.left != null) {
            this.left = copy(other.left);
        }
        if(other.right != null) {
            this.right = copy(other.right);
        }
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

    public String evaluate() {
        if (this.evaluator == null) {
            return sentence;
        }
        return evaluator.evaluate(sentence);
    }

    public Sentence setEvaluator(SentenceEvaluator evaluator) {
        this.evaluator = evaluator;
        return this;
    }
}
