package com.github.jamesarthurholland.alfalfa.transpiler;

import com.github.jamesarthurholland.alfalfa.typeSystem.TypeSystemConverter;

public interface SentenceEvaluator {
    String evaluate(String sentence, TypeSystemConverter converter, String langName);
    Object clone();
}
