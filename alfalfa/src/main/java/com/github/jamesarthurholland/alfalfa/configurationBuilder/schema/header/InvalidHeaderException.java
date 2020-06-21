package com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.header;

/**
 * Created by jamie on 03/07/16.
 */
public class InvalidHeaderException extends RuntimeException
{
    public InvalidHeaderException()
    {
        super("Invalid header. Must be of form {{ALFALFA}} or {{ALFALFA -o filename.format}}");
    }
}
