package AnnotatedTree.AutoProcessor.AutoTranslation.PartOfSpeech;

import Dictionary.TxtWord;
import AnnotatedTree.ParseNodeDrawable;

import java.util.List;

public class TurkishJJTranslator extends TurkishPartOfSpeechTranslator{

    public TurkishJJTranslator(ParseNodeDrawable parseNode, List<String> parentList, List<String> englishWordList, String prefix, String lastWordForm, TxtWord lastWord) {
        super(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord);
    }

    public String translate(){
        String posArray[] = {"VBZ", "VBZ", "VBP", "VBP", "VBD", "VBD"};
        String suffixArray[] = {"DHr", "DHr", "DHr", "DHr", "yDH", "yDH"};
        String wordArray[] = {"is", "'s", "are", "'re", "was", "were"};
        if (parentList.size() > 1){
            for (int i = 0; i < posArray.length; i++){
                if (parentList.get(1).equals(posArray[i]) && englishWordList.get(1).equals(wordArray[i])){
                    return addSuffix(suffixArray[i], prefix, lastWord, lastWordForm);
                }
            }
        }
        return prefix + lastWord.getName();
    }
}
