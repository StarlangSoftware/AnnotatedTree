package AnnotatedTree.AutoProcessor.AutoTranslation.PartOfSpeech;

import Dictionary.TxtDictionary;
import Dictionary.TxtWord;
import MorphologicalAnalysis.Transition;
import AnnotatedTree.ParseNodeDrawable;

import java.util.List;

public class TurkishNNTranslator extends TurkishNounTranslator {

    public TurkishNNTranslator(ParseNodeDrawable parseNode, List<String> parentList, List<String> englishWordList, String prefix, String lastWordForm, TxtWord lastWord, TxtDictionary txtDictionary) {
        super(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord, txtDictionary);
    }

    public String translate(){
        String newLastWordForm, result, result2;
        boolean previousWordNoun = false;
        String posArray1[] = {"VBZ", "VBP"};
        String posArray2[] = {"IN", "TO"};
        String posArray3[] = {"POS", "PRP$"};
        String posArray3Prime[] = {"POS"};
        String wordArray1[][] = {{"'s", "is"}, {"'re", "are"}};
        String wordArray2[][] = {{"of", "in", "on", "at", "into", "with", "from", "since", "by", "until", "than"}, {"to"}};
        String wordArray3[][] = {{}, {"my", "your", "his", "her", "its", "our", "their"}};
        String wordArray3Prime[][] = {{}};
        String suffixArray1[][] = {{"DHr", "DHr"}, {"DHr", "DHr"}};
        String suffixArray2[][] = {{"nHn", "DA", "DA", "DA", "nA", "ylA", "DAn", "DAn", "ylA", "nA", "DAn"}, {"yA"}};
        String suffixArray2Prime[][] = {{"nHn", "nDA", "nDA", "nDA", "nA", "ylA", "nDAn", "nDAn", "ylA", "nA", "nDAn"}, {"nA"}};
        String suffixArray3[][] = {{"nHn"}, {"Hm", "Hn", "sH", "sH", "sH", "HmHz", "lArH"}};
        String suffixArray3Prime[][] = {{"nHn"}};
        if (parentList.size() > 1) {
            if (prefix != null){
                TxtWord prefixWord = (TxtWord) txtDictionary.getWord(prefix.trim());
                if (prefixWord != null && prefixWord.isNominal()){
                    previousWordNoun = true;
                }
            }
            if (isLastWordOfNounPhrase(parseNode) || previousWordNoun){
                Transition transition = new Transition("sH");
                newLastWordForm = transition.makeTransition(lastWord, lastWordForm);
                result = translateNouns(posArray3Prime, wordArray3Prime, suffixArray3Prime, parentList, englishWordList, prefix, lastWord, newLastWordForm);
                if (result != null){
                    result2 = translateNouns(posArray2, wordArray2, suffixArray2Prime, parentList, englishWordList, "", lastWord, result);
                    if (result2 != null){
                        result = result2;
                    }
                } else {
                    result = translateNouns(posArray2, wordArray2, suffixArray2Prime, parentList, englishWordList, prefix, lastWord, newLastWordForm);
                }
                if (result != null){
                    return result;
                }
                result = translateNouns(posArray1, wordArray1, suffixArray1, parentList, englishWordList, prefix, lastWord, newLastWordForm);
            } else {
                newLastWordForm = lastWordForm;
                result = translateNouns(posArray3, wordArray3, suffixArray3, parentList, englishWordList, prefix, lastWord, newLastWordForm);
                if (result != null){
                    result2 = translateNouns(posArray2, wordArray2, suffixArray2, parentList, englishWordList, "", lastWord, result);
                    if (result2 != null){
                        result = result2;
                    }
                } else {
                    result = translateNouns(posArray2, wordArray2, suffixArray2, parentList, englishWordList, prefix, lastWord, newLastWordForm);
                }
                if (result == null){
                    result = translateNouns(posArray1, wordArray1, suffixArray1, parentList, englishWordList, prefix, lastWord, newLastWordForm);
                }
            }
            if (result != null){
                return result;
            }
            if (isLastWordOfNounPhrase(parseNode) || previousWordNoun){
                return addSuffix("sH", prefix, lastWord, lastWordForm);
            }
        }
        return prefix + lastWord.getName();
    }
}
