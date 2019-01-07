package com.github.jamesarthurholland.alfalfa;

/**
 * Created by jamie on 03/07/16.
 */
public class NoEntityFileException extends RuntimeException
{
    public NoEntityFileException()
    {
        super("No name.afae file in current directory.");
    }


}
