package com.github.jamesarthurholland.alfalfa.configurationBuilder;

public class MalformedVariableLineException extends RuntimeException
{
    public MalformedVariableLineException() // TODO pass line number for better debugging.
    {
        super("Variable definitions must be of the form: \n\t k|v i|o|u var_type var_name [mapping]");
    }


}
