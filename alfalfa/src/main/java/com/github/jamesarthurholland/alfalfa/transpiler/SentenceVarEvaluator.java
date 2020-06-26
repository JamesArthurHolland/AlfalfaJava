package com.github.jamesarthurholland.alfalfa.transpiler;

import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.EntityInfo;
import com.github.jamesarthurholland.alfalfa.configurationBuilder.schema.Variable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.jamesarthurholland.alfalfa.StringUtils.camelToLowerUnderScore;
import static com.github.jamesarthurholland.alfalfa.StringUtils.camelToUpperUnderScore;
import static com.github.jamesarthurholland.alfalfa.StringUtils.uppercaseFirst;

public class SentenceVarEvaluator implements Cloneable, SentenceEvaluator
{
    private Variable givenVar;
    private EntityInfo entityInfo;

    public SentenceVarEvaluator(Variable givenVar, EntityInfo entityInfo) {
        this.givenVar = givenVar;
        this.entityInfo = entityInfo;
    }

    @Override
    public Object clone() {
        return new SentenceVarEvaluator(
            ((Variable)this.givenVar.clone()),
            ((EntityInfo)this.entityInfo.clone())
        );
    }

    //    public SentenceForEachEntityEvaluator(Variable givenVar, SentenceForEachEntityEvaluator evaluator) {
//        this.givenVar = givenVar;
//        this.entityInfo = evaluator.entityInfo;
//    }

    public String evaluate(String sentence)
    {
        String generatedSentence = evaluateForEntityReplacements(sentence, entityInfo);
//        generatedSentence = evaluateForNamespace(sentence, entityInfo);
        generatedSentence = evaluateForKeyStatement (givenVar, generatedSentence);

        if (generatedSentence == null) {
            return generatedSentence;
        }
        generatedSentence = evaluateForNotKeyStatement(givenVar, generatedSentence, entityInfo);
        if (generatedSentence == null) {
            return generatedSentence;
        }
        generatedSentence = evaluateForNotLastStatement(givenVar, generatedSentence, entityInfo);
        if (generatedSentence == null) {
            return generatedSentence;
        }
        generatedSentence = evaluateForLastStatement(givenVar, generatedSentence, entityInfo);
        if (generatedSentence != null) {
            generatedSentence = replaceVarsInString (givenVar, generatedSentence);
            generatedSentence = replaceTypesInString(givenVar, generatedSentence);
            generatedSentence = replaceVisibilityInString(givenVar, generatedSentence);
        }
        return generatedSentence;
    }

    public static String evaluateForNotKeyStatement (Variable givenVar, String sentence, EntityInfo entityInfo)
    {
        Pattern patternForKey = Pattern.compile("(.*)\\{\\{NOT-KEY\\s+(NOT-LAST|LAST)?\\}\\}(.*)\\{\\{/NOT-KEY\\}\\}.*");
        Pattern patternForKeyWholeGroup = Pattern.compile(".*(\\{\\{NOT-KEY\\}\\}.*\\{\\{/NOT-KEY\\}\\}).*");
        String generatedSentence = sentence;
        Matcher matcher = patternForKey.matcher(sentence);
        if (matcher.matches()) {
            if (!givenVar.isPrimary ()) {
                generatedSentence = matcher.group(1) + SentenceVarEvaluator.replaceVarsInString (givenVar, matcher.group(3));
                String lastOrNot = matcher.group(2);
                if (lastOrNot.equals("NOT-LAST")) {
                    if (!entityInfo.isVarLast (givenVar)) {
                        return generatedSentence;
                    } else {
                        return null;
                    }
                }
                if (lastOrNot.equals("LAST")) {
                    if (entityInfo.isVarLast (givenVar)) {
                        return generatedSentence;
                    } else {
                        return null;
                    }
                }
            }
            else {
                return null;
            }
        }
        return generatedSentence;
    }

    static String evaluateForKeyStatement (Variable givenVar, String sentence)
    {
        Pattern patternForKey = Pattern.compile("(.*)\\{\\{KEY\\}\\}(.*)\\{\\{/KEY\\}\\}.*");
        Pattern patternForKeyWholeGroup = Pattern.compile(".*(\\{\\{KEY\\}\\}.*\\{\\{/KEY\\}\\}).*"); // TODO get rid of this line?
        String generatedSentence = sentence;
        Matcher matcher = patternForKey.matcher(sentence);
        if (matcher.matches()) {
            //				Console.Write ("boom " + match.Groups [0].Value);
            if (givenVar.isPrimary ()) {
                generatedSentence = matcher.group(1) + replaceVarsInString (givenVar, matcher.group(2));
            }
            else {
                // TODO: rethink for potential multi alfalfa statements on one line!!!
                return null;
            }
        }
        return generatedSentence;
    }

    public static String evaluateForNotLastStatement (Variable givenVar, String sentence, EntityInfo entityInfo)
    {
        Pattern patternForKey = Pattern.compile("(.*)\\{\\{\\s*NOT-LAST\\s*\\}\\}(.*)\\{\\{\\s*/NOT-LAST\\s*\\}\\}.*");
        Matcher matcher = patternForKey.matcher(sentence);
        if (matcher.matches()) {
            String generatedSentence = matcher.group(1) + replaceVarsInString (givenVar, matcher.group(2));
            if (!entityInfo.isVarLast (givenVar)) {
                return generatedSentence;
            } else {
                return null;
            }
        }
        return sentence;
    }

    public static String evaluateForLastStatement (Variable givenVar, String sentence, EntityInfo entityInfo)
    {
        Pattern patternForKey = Pattern.compile("(.*)\\{\\{\\s*LAST\\s*\\}\\}(.*)\\{\\{\\s*/LAST\\s*\\}\\}.*");
        Matcher matcher = patternForKey.matcher(sentence);
        if (matcher.matches()) {
            String generatedSentence = matcher.group(1) + replaceVarsInString (givenVar, matcher.group(2));
            if (entityInfo.isVarLast (givenVar)) {
                return generatedSentence;
            } else {
                return null;
            }
        }
        return sentence;
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

    public String replaceInputInString (Variable givenVar, String sentence)
    {
        String outputString = sentence.replaceAll("\\{\\{input\\}\\}", givenVar.getName());

        return outputString;
    }

    public static String replaceVarsInString(Variable givenVar, String sentence)
    {
        String outputString = sentence.replaceAll("\\{\\{var\\}\\}", givenVar.getName());
        outputString = outputString.replaceAll("\\{\\{v_ar\\}\\}", camelToLowerUnderScore(givenVar.getName()));
        outputString = outputString.replaceAll("\\{\\{VAR\\}\\}", camelToUpperUnderScore(givenVar.getName()));
        outputString = outputString.replaceAll("\\{\\{Var\\}\\}", uppercaseFirst(givenVar.getName()));

        return outputString;
    }

    public static String replaceTypesInString(Variable givenVar, String sentence)
    {
        String outputString = sentence.replaceAll("\\{\\{type\\}\\}", givenVar.getType());
        outputString = outputString.replaceAll("\\{\\{t_ype\\}\\}", camelToLowerUnderScore(givenVar.getType()));
        outputString = outputString.replaceAll("\\{\\{TYPE\\}\\}", camelToUpperUnderScore(givenVar.getType()));
        outputString = outputString.replaceAll("\\{\\{Type\\}\\}", uppercaseFirst(givenVar.getType()));

        return outputString;
    }

    public static String replaceVisibilityInString(Variable givenVar, String sentence)
    {
        String outputString = sentence.replaceAll("\\{\\{visibility\\}\\}", givenVar.getVisibility());
        return outputString;
    }
}
