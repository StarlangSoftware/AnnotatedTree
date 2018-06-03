package AnnotatedTree.AutoProcessor.AutoTranslation;

import AnnotatedSentence.ViewLayerType;
import Translation.AutomaticTranslationDictionary;
import Translation.BilingualDictionary;
import Translation.WordTranslations;
import AnnotatedTree.ParseNodeDrawable;

import java.util.ArrayList;

public class BaseLineTurkishAutoTranslator extends AutoTranslator{

    public BaseLineTurkishAutoTranslator(AutomaticTranslationDictionary dictionary, BilingualDictionary bilingualDictionary){
        super(ViewLayerType.TURKISH_WORD, dictionary, bilingualDictionary);
        autoPreprocessor = new TurkishAutoPreprocessor();
    }

    protected String autoTranslateWithRules(ParseNodeDrawable parseNode, boolean noneCase, ArrayList<String> parentList, ArrayList<String> englishWordList, int index, WordTranslations translations) {
        if (translations.translationCount() > 0){
            return translations.getTranslation(0).getTranslation();
        } else {
            return "";
        }
    }
}
