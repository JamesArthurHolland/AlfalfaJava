package com.github.jamesarthurholland.alfalfa.configurationBuilder;

import com.github.jamesarthurholland.alfalfa.model.Variable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class VariableConditional
{
    private final String token;

    public VariableConditional(String token) {
        this.token = token;
    }

    public Matcher newMatcher(String sentence)
    {
        Pattern patternForKey = Pattern.compile("(.*)\\{\\{" + token + "\\}\\}(.*)\\{\\{/" + token + "\\}\\}.*");
        return patternForKey.matcher(sentence);
    }

    abstract boolean isVariableOfConditional(Variable variable);
}
