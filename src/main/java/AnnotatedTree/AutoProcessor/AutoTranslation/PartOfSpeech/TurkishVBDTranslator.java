package AnnotatedTree.AutoProcessor.AutoTranslation.PartOfSpeech;

import Dictionary.TxtWord;
import MorphologicalAnalysis.Transition;
import AnnotatedTree.ParseNodeDrawable;

import java.util.List;

public class TurkishVBDTranslator extends TurkishVerbTranslator{

    public TurkishVBDTranslator(ParseNodeDrawable parseNode, List<String> parentList, List<String> englishWordList, String prefix, String lastWordForm, TxtWord lastWord) {
        super(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord);
    }

    public String translate(){
        Transition transition = new Transition("DH");
        if (parentList.size() > 1 && parentList.get(1).equals("PRP")){
            transition = new Transition("DH" + personalSuffix2(englishWordList.get(1).toLowerCase()));
        }
        return prefix + transition.makeTransition(lastWord, lastWordForm);
    }
}
