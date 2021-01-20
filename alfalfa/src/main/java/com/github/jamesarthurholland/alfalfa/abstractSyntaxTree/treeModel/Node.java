package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree.treeModel;

import com.google.gson.Gson;

public abstract class Node
{
    public final Type type;
    public Node left, right;
    public Context context;

    public enum Type {
        SENTENCE,
        VAR_LOOP,
        ENTITY_LOOP,
        VAR_KEY_CONDITIONAL,
        CHILD_MAPPING_LOOP,
//        PARENT_MAPPING_LOOP,
        MANY_TO_MANY_CONDITIONAL,
        ONE_TO_MANY_CONDITIONAL,
        MANY_OR_ONE_TO_MANY_CONDITIONAL,
        ONE_TO_ONE_CONDITIONAL,
    }

    public Node(Type type)
    {
        left = null;
        right = null;
        this.type = type;
    }

    public Node(Type type, Node left, Node right, Context context) {
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
                    case VAR_KEY_CONDITIONAL:
                        // TODO
                        break;
                    case CHILD_MAPPING_LOOP:
                        return new ChildMappingLoop((ChildMappingLoop)node);
                    case ONE_TO_ONE_CONDITIONAL:
                        return new OneToOneIndexConditional((OneToOneIndexConditional) node);
                    case ONE_TO_MANY_CONDITIONAL:
                        return new OneToManyIndexConditional((OneToManyIndexConditional) node);
                    case MANY_OR_ONE_TO_MANY_CONDITIONAL:
                        return new ManyOrOneToManyIndexConditional((ManyOrOneToManyIndexConditional) node);
//                    case MANY_TO_MANY_CONDITIONAL: TODO
//                        return new Many
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
