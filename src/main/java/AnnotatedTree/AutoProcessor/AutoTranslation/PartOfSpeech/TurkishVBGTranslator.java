package AnnotatedTree.AutoProcessor.AutoTranslation.PartOfSpeech;

import Dictionary.TxtWord;
import MorphologicalAnalysis.Transition;
import AnnotatedTree.ParseNodeDrawable;

import java.util.List;

public class TurkishVBGTranslator extends TurkishVerbTranslator{

    public TurkishVBGTranslator(ParseNodeDrawable parseNode, List<String> parentList, List<String> englishWordList, String prefix, String lastWordForm, TxtWord lastWord) {
        super(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord);
    }

    public String translate(){
        String posArray[] = {"VBZ", "VBP", "VBD", "VBN"};
        String suffixArray[] = {"yor", "yor", "yordu", "yor"};
        Transition transition;
        int i;
        String negation;
        if (parentList.size() > 4 && parentList.get(1).equals("RB") && parentList.get(2).equals("VB") && parentList.get(3).equals("MD") && parentList.get(4).equals("PRP")){
            transition = new Transition("mAyAcAk" + personalSuffix1(englishWordList.get(4).toLowerCase()));
            return prefix + transition.makeTransition(lastWord, lastWordForm);
        } else {
            if (parentList.size() > 3 && parentList.get(1).equals("RB") && parentList.get(2).equals("VB") && parentList.get(3).equals("MD")){
                transition = new Transition("mAyAcAk");
                return prefix + transition.makeTransition(lastWord, lastWordForm);
            } else {
                if (parentList.size() > 3 && parentList.get(1).equals("VB") && parentList.get(2).equals("MD") && parentList.get(3).equals("PRP")){
                    transition = new Transition("yAcAk" + personalSuffix1(englishWordList.get(3).toLowerCase()));
                    return prefix + transition.makeTransition(lastWord, lastWordForm);
                } else {
                    if (parentList.size() > 2 && parentList.get(1).equals("VB") && parentList.get(2).equals("MD")){
                        transition = new Transition("yAcAk");
                        return prefix + transition.makeTransition(lastWord, lastWordForm);
                    }
                }
            }
        }
        if (parentList.size() > 1 && parentList.get(1).equals("RB")){
            i = 2;
            negation = "mH";
        } else {
            i = 1;
            negation = "H";
        }
        if (i < parentList.size()){
            for (int j = 0; j < posArray.length; j++){
                if (parentList.get(i).equals(posArray[j])) {
                    if (i + 1 < parentList.size() && parentList.get(i + 1).equals("PRP")) {
                        transition = new Transition(negation + suffixArray[j] + personalSuffix1(englishWordList.get(i + 1).toLowerCase()));
                    } else {
                        transition = new Transition(negation + suffixArray[j]);
                    }
                    return prefix + transition.makeTransition(lastWord, lastWordForm);
                }
            }
        }
        if (isLastWordOfVerbAsNoun(parseNode)){
            return addSuffix("yAn", prefix, lastWord, lastWordForm);
        } else {
            return addSuffix("mAk", prefix, lastWord, lastWordForm);
        }
    }
}
