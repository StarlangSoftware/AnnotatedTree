package AnnotatedTree.AutoProcessor.AutoTranslation.PartOfSpeech;

import Dictionary.TxtWord;
import MorphologicalAnalysis.Transition;
import AnnotatedTree.ParseNodeDrawable;

import java.util.List;

public class TurkishVBZTranslator extends TurkishVerbTranslator{

    public TurkishVBZTranslator(ParseNodeDrawable parseNode, List<String> parentList, List<String> englishWordList, String prefix, String lastWordForm, TxtWord lastWord) {
        super(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord);
    }

    public String translate(){
        Transition transition;
        if (lastWord.takesSuffixIRAsAorist()){
            transition = new Transition("Hr");
            if (parentList.size() > 1 && parentList.get(1).equals("PRP")){
                transition = new Transition("Hr" + personalSuffix1(englishWordList.get(1).toLowerCase()));
            }
        } else {
            transition = new Transition("Ar");
            if (parentList.size() > 1 && parentList.get(1).equals("PRP")){
                transition = new Transition("Ar" + personalSuffix1(englishWordList.get(1).toLowerCase()));
            }
        }
        return prefix + transition.makeTransition(lastWord, lastWordForm);
    }
}
