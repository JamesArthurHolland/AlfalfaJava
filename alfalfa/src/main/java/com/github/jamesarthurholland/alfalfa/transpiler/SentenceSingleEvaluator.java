package com.github.jamesarthurholland.alfalfa.transpiler;

import com.github.jamesarthurholland.alfalfa.StringUtils;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;
import com.github.jamesarthurholland.alfalfa.typeSystem.TypeSystemConverter;

public class SentenceSingleEvaluator implements Cloneable, SentenceEvaluator
{
    private EntityInfo entityInfo;

    public SentenceSingleEvaluator(EntityInfo entityInfo) {
        this.entityInfo = entityInfo;
    }

    @Override
    public Object clone() {
        return new SentenceSingleEvaluator((EntityInfo) this.entityInfo.clone());
    }

    public String evaluate(String sentenceToEvaluate, TypeSystemConverter converter, String langName)
    {
        String generatedSentence = StringUtils.evaluateForEntityReplacements(sentenceToEvaluate, this.entityInfo);
        generatedSentence = StringUtils.evaluateForNamespace(generatedSentence, entityInfo);
        generatedSentence = StringUtils.evaluateForTableReplacements(generatedSentence, entityInfo);

        return generatedSentence;
    }


}
