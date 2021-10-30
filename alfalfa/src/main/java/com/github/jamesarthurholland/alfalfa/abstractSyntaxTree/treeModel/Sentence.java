package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel;

import com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.Container;
import com.github.jamesarthurholland.alfalfa.typeSystem.TypeSystemConverter;

public class Sentence extends Node implements Cloneable
{
    private String sentence;
    public static final String type = "SENTENCE";

    public Sentence(String sentence) {
        super(Type.SENTENCE);
        this.sentence = sentence;
    }

    public Sentence(Node left, Node right, String sentence, Context context) {
        super(Type.SENTENCE, left, right, context);
        this.sentence = sentence;
    }

    public boolean isInLoop() {
        if (context == null) {
            return false;
        }
        return true;
//        Container.VarLoopStatus status = (Container.VarLoopStatus) context.container.getOrDefault(Container.IS_IN_VAR_LOOP, null);
//
//        if (status == null) {
//            return false;
//        }
//
//        return status.isInVarLoop();
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

    public String evaluate(TypeSystemConverter typeSystemConverter, String langName) {
        if (this.context == null) {
            return sentence;
        }
        return this.context.evaluator.evaluate(sentence, typeSystemConverter, langName);
    }

}
