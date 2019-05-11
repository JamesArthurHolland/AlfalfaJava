package com.github.jamesarthurholland.alfalfa.configurationBuilder;

public class NoDotAlfalfaDirectoryException extends RuntimeException
{
    public NoDotAlfalfaDirectoryException()
    {
        super("No dotalfalfa directory found.");
    }
}
