package AnnotatedTree.AutoProcessor.AutoTranslation;

import AnnotatedSentence.ViewLayerType;
import Translation.AutomaticTranslationDictionary;
import Translation.BilingualDictionary;
import Translation.WordTranslations;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;

import java.util.ArrayList;

public class PersianAutoTranslator extends AutoTranslator{

    public PersianAutoTranslator(AutomaticTranslationDictionary dictionary, BilingualDictionary bilingualDictionary) {
        super(ViewLayerType.PERSIAN_WORD, dictionary, bilingualDictionary);
    }

    protected void autoFillWithNoneTags(ParseTreeDrawable parseTree) {
    }

    protected void autoSwap(ParseTreeDrawable parseTree) {
    }

    protected String autoTranslateWithRules(ParseNodeDrawable parseNode, boolean noneCase, ArrayList<String> parents, ArrayList<String> englishWords, int index, WordTranslations translations) {
        return null;
    }

}
