package com.github.jamesarthurholland.alfalfa;

/**
 * Created by jamie on 03/07/16.
 */
public class PatternDirectoryEmptyException extends RuntimeException
{
    public PatternDirectoryEmptyException()
    {
        super("The pattern directory is empty. Put the path to the folder of the Alfalfa" +
                " patterns you wish to use for this installation in the file /src/patternDirectory.txt.");
    }
}
