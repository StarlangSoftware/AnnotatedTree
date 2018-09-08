package AnnotatedTree.AutoProcessor.AutoTranslation.PartOfSpeech;

import Dictionary.TxtWord;
import AnnotatedTree.ParseNodeDrawable;

import java.util.List;

public class TurkishRBTranslator extends TurkishPartOfSpeechTranslator {

    public TurkishRBTranslator(ParseNodeDrawable parseNode, List<String> parentList, List<String> englishWordList, String prefix, String lastWordForm, TxtWord lastWord) {
        super(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord);
    }

    public String translate(){
        String posArray[] = {"VBZ", "VBP", "VBD", "."};
        String suffixArray[] = {"dir", "dir", "di", ""};
        if (parentList.size() > 1){
            for (int i = 0; i < posArray.length; i++){
                if (parentList.get(1).equals(posArray[i])){
                    return "değil" + suffixArray[i];
                }
            }
        }
        return "*NONE*";
    }
}
