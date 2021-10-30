package com.github.jamesarthurholland.alfalfa.transpiler;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Variable;
import com.github.jamesarthurholland.alfalfa.typeSystem.TypeSystemConverter;

import static com.github.jamesarthurholland.alfalfa.StringUtils.uppercaseFirst;

public class Utils
{
    public static String replaceTypesInString(Variable givenVar, String sentence, TypeSystemConverter converter, String langName)
    {
        String type = converter.convert(givenVar.getType(), langName);
        String outputString = sentence.replaceAll("\\{\\{type\\}\\}", type);
        outputString = outputString.replaceAll("\\{\\{Type\\}\\}", uppercaseFirst(type));

        return outputString;
    }
}
