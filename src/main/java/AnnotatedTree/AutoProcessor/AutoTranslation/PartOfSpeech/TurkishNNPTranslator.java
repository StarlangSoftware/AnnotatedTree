package AnnotatedTree.AutoProcessor.AutoTranslation.PartOfSpeech;

import Dictionary.TxtDictionary;
import Dictionary.TxtWord;
import AnnotatedTree.ParseNodeDrawable;

import java.util.List;

public class TurkishNNPTranslator extends TurkishNounTranslator{

    public TurkishNNPTranslator(ParseNodeDrawable parseNode, List<String> parentList, List<String> englishWordList, String prefix, String lastWordForm, TxtWord lastWord, TxtDictionary txtDictionary) {
        super(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord, txtDictionary);
    }

    public String translate(){
        String posArray[] = {"VBZ", "VBP", "IN", "TO", "POS"};
        String wordArray[][] = {{"'s", "is"}, {"'re", "are"}, {"of", "in", "on", "at", "into", "with", "by", "from", "since"}, {"to"}, {}};
        String suffixArray[][] = {{"'DHr", "'DHr"}, {"'DHr", "'DHr"}, {"'nHn", "'DA", "'DA", "'DA", "'nA", "'ylA", "'ylA", "'DAn", "'DAn"}, {"'yA"}, {"'nHn"}};
        if (parentList.size() > 1){
            String result = translateNouns(posArray, wordArray, suffixArray, parentList, englishWordList, prefix, lastWord, lastWordForm);
            if (result != null){
                return result;
            }
        }
        return prefix + lastWord.getName();
    }
}
