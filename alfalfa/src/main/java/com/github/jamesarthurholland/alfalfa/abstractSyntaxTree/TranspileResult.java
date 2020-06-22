package com.github.jamesarthurholland.alfalfa.abstractSyntaxTree;

import java.util.ArrayList;

/**
 * Created by jamie on 04/07/16.
 */
public class TranspileResult
{
    protected String fileName;
    protected ArrayList<String> output;

    public TranspileResult(String fileName, ArrayList<String> output) {
        this.fileName = fileName;
        this.output = output;
    }

    public ArrayList<String> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<String> output) {
        this.output = output;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
