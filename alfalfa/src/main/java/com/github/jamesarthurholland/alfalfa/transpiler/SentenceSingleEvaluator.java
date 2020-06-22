package com.github.jamesarthurholland.alfalfa.transpiler;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;

import static com.github.jamesarthurholland.alfalfa.StringUtils.*;

public class SentenceSingleEvaluator implements SentenceEvaluator
{
    private EntityInfo entityInfo;

    public SentenceSingleEvaluator(EntityInfo entityInfo) {
        this.entityInfo = entityInfo;
    }

    @Override
    public String evaluate(String sentenceToEvaluate)
    {
        String generatedSentence = evaluateForEntityReplacements(sentenceToEvaluate);
//        generatedSentence = evaluateForNamespace(sentence, entityInfo);

        return generatedSentence;
    }

    public String evaluateForEntityReplacements (String sentence)
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
