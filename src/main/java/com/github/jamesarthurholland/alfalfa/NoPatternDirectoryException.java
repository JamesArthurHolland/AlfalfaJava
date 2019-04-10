package com.github.jamesarthurholland.alfalfa;

/**
 * Created by jamie on 03/07/16.
 */
public class NoPatternDirectoryException extends RuntimeException
{
    public NoPatternDirectoryException()
    {
        super("The file /src/patternDirectory.txt file is missing, please create it or reinstall Alfalfa.");
    }
}
