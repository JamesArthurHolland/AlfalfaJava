package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import com.google.gson.Gson;

public abstract class Node
{
    public final Types type;
    public Node left, right;
    public Context context;

    public static enum Types {
        SENTENCE,
        VAR_LOOP,
        ENTITY_LOOP,
        INDICES_LOOP,
        VAR_CONDITIONAL
    }

    public Node(Types type)
    {
        left = null;
        right = null;
        this.type = type;
    }

    public Node(Types type, Node left, Node right, Context context) {
        this.type = type;
        this.left = left;
        this.right = right;
        this.context = context;
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
                    case INDICES_LOOP:
                        return new IndicesLoop((IndicesLoop)node);
                    default:
                        System.out.println("ERROR in node copy");
                        break;
                }
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public abstract void print ();


}
