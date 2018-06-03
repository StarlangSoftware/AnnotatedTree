package AnnotatedTree.AutoProcessor.AutoTranslation.PartOfSpeech;

import Dictionary.TxtDictionary;
import Dictionary.TxtWord;
import AnnotatedTree.ParseNodeDrawable;

import java.util.List;

public class TurkishCDTranslator extends TurkishNounTranslator{
    protected boolean withDigits;

    public TurkishCDTranslator(ParseNodeDrawable parseNode, List<String> parentList, List<String> englishWordList, String prefix, String lastWordForm, TxtWord lastWord, boolean withDigits, TxtDictionary txtDictionary) {
        super(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord, txtDictionary);
        this.withDigits = withDigits;
    }

    public String translate(){
        String result;
        String posArray[] = {"IN", "TO"};
        String wordArray[][] = {{"in", "from", "than"}, {"to"}};
        String suffixArray1[][] = {{"'DA", "'DAn", "'DAn"}, {"'yA"}};
        String suffixArray2[][] = {{"DA", "DAn", "DAn"}, {"yA"}};
        if (parentList.size() > 1){
            if (withDigits){
                result = translateNouns(posArray, wordArray, suffixArray1, parentList, englishWordList, prefix, lastWord, lastWordForm);
            } else {
                result = translateNouns(posArray, wordArray, suffixArray2, parentList, englishWordList, prefix, lastWord, lastWordForm);
            }
            if (result != null){
                return result;
            }
        }
        return null;
    }
}
