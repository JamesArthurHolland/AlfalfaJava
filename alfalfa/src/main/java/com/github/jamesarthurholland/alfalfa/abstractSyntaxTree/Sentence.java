package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

public class Sentence extends Node implements Cloneable
{
    private String sentence;
    public static final String type = "SENTENCE";

    public Sentence(String sentence) {
        super(Types.SENTENCE);
        this.sentence = sentence;
    }

    public Sentence(Node left, Node right, String sentence, Context context) {
        super(Types.SENTENCE, left, right, context);
        this.sentence = sentence;
    }

    public boolean isInLoop() {
        return context != null;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Context context = null;
        if(this.context != null) {
            context = (Context) this.context.clone();
        }
        Node left = null;
        Node right = null;
        if(this.left != null) {
            left = copy(this.left);
        }
        if(this.right != null) {
            right = copy(this.right);
        }

        return new Sentence(left, right, this.sentence, context);
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
        if (this.context == null) {
            return sentence;
        }
        return this.context.evaluator.evaluate(sentence);
    }

}
