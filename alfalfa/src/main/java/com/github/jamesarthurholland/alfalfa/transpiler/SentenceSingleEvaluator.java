package com.github.jamesarthurholland.alfalfa.transpiler;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;

import static com.github.jamesarthurholland.alfalfa.StringUtils.*;

public class SentenceSingleEvaluator
{
    private EntityInfo entityInfo;

    public SentenceSingleEvaluator(EntityInfo entityInfo) {
        this.entityInfo = entityInfo;
    }

    public static String evaluate(String sentenceToEvaluate, EntityInfo entityInfo)
    {
        String generatedSentence = evaluateForEntityReplacements(sentenceToEvaluate, entityInfo);
//        generatedSentence = evaluateForNamespace(sentence, entityInfo);

        return generatedSentence;
    }

    public static String evaluateForEntityReplacements (String sentence, EntityInfo entityInfo)
    {
        String outputString = sentence.replaceAll("\\{\\{entity\\}\\}", entityInfo.getName());
        outputString = outputString.replaceAll("\\{\\{en_tity\\}\\}", camelToLowerUnderScore(entityInfo.getName()));
        outputString = outputString.replaceAll("\\{\\{EN_TITY\\}\\}", camelToUpperUnderScore(entityInfo.getName()));
        outputString = outputString.replaceAll("\\{\\{Entity\\}\\}", uppercaseFirst(entityInfo.getName()));

        return outputString;
    }

//    protected String evaluateForNamespace(String sentence, EntityInfo entityInfo)
//    {
//        String nameSpace = entityInfo.getNameSpace();
//        nameSpace = nameSpace.replaceAll("\\\\", "\\\\\\\\");
//        String outputSentence = sentence.replaceAll("\\{\\{NAMESPACE\\}\\}", nameSpace);
//        return outputSentence;
//    }
}
