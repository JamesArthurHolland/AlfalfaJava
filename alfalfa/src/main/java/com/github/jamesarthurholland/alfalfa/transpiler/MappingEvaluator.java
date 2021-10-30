package com.github.jamesarthurholland.alfalfa.transpiler;

import com.github.jamesarthurholland.alfalfa.StringUtils;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Variable;
import com.github.jamesarthurholland.alfalfa.typeSystem.TypeSystemConverter;

import static com.github.jamesarthurholland.alfalfa.StringUtils.*;

public class MappingEvaluator implements Cloneable, SentenceEvaluator
{
    private Variable givenVar;
    private EntityInfo entityInfo;

    public MappingEvaluator(Variable givenVar, EntityInfo entityInfo) {
        this.givenVar = givenVar;
        this.entityInfo = entityInfo;
    }

    @Override
    public Object clone() {
        return new MappingEvaluator(
            ((Variable)this.givenVar.clone()),
            ((EntityInfo)this.entityInfo.clone())
        );
    }

    //    public SentenceForEachEntityEvaluator(Variable givenVar, SentenceForEachEntityEvaluator evaluator) {
//        this.givenVar = givenVar;
//        this.entityInfo = evaluator.entityInfo;
//    }

    public String evaluate(String sentence, TypeSystemConverter converter, String langName)
    {
        String generatedSentence = evaluateForEntityReplacements(sentence, entityInfo);

        if (generatedSentence == null) {
            return generatedSentence;
        }

        if (generatedSentence != null) {
            generatedSentence = replaceIndicesInString (givenVar, generatedSentence);
            generatedSentence = Utils.replaceTypesInString(givenVar, generatedSentence, converter, langName);
            generatedSentence = replaceVisibilityInString(givenVar, generatedSentence);
        }
        generatedSentence = StringUtils.evaluateForNamespace(generatedSentence, entityInfo);
        generatedSentence = StringUtils.evaluateForTableReplacements(generatedSentence, entityInfo);

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

    public static String replaceIndicesInString(Variable givenVar, String sentence)
    {
        String outputString = sentence.replaceAll("\\{\\{from\\}\\}", givenVar.getName());
        outputString = outputString.replaceAll("\\{\\{in_dex\\}\\}", camelToLowerUnderScore(givenVar.getName()));
        outputString = outputString.replaceAll("\\{\\{INDEX\\}\\}", camelToUpperUnderScore(givenVar.getName()));
        outputString = outputString.replaceAll("\\{\\{Index\\}\\}", uppercaseFirst(givenVar.getName()));

        return outputString;
    }

    public static String replaceVisibilityInString(Variable givenVar, String sentence)
    {
        String outputString = sentence.replaceAll("\\{\\{visibility\\}\\}", givenVar.getVisibility());
        return outputString;
    }
}
