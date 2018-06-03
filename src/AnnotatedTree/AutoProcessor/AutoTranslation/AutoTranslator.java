package AnnotatedTree.AutoProcessor.AutoTranslation;

import AnnotatedSentence.ViewLayerType;
import Translation.AutomaticTranslationDictionary;
import Translation.BilingualDictionary;
import Translation.SourceWord;
import Translation.WordTranslations;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.Processor.Condition.*;
import AnnotatedTree.Processor.NodeDrawableCollector;

import java.util.ArrayList;

public abstract class AutoTranslator{
    protected ViewLayerType secondLanguage;
    protected AutomaticTranslationDictionary dictionary;
    protected BilingualDictionary bilingualDictionary;
    protected AutoPreprocessor autoPreprocessor;

    protected abstract String autoTranslateWithRules(ParseNodeDrawable parseNode, boolean noneCase, ArrayList<String> parentList, ArrayList<String> englishWordList, int index, WordTranslations translations);

    private void autoFillWithTranslations(ParseTreeDrawable parseTree){
        SourceWord sourceWord;
        WordTranslations word;
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        ArrayList<String> parentList = new ArrayList<>();
        ArrayList<String> englishWordList = new ArrayList<>();
        for (ParseNodeDrawable parseNode : leafList){
            if (!(new IsNullElement().satisfies(parseNode))){
                parentList.add(parseNode.getParent().getData().getName());
                englishWordList.add(parseNode.getLayerData(ViewLayerType.ENGLISH_WORD));
            }
        }
        for (int i = 0, j = 0; i < leafList.size(); i++){
            ParseNodeDrawable parseNode = leafList.get(i);
            if (!(new IsNullElement().satisfies(parseNode))){
                boolean noneCase = parseNode.getLayerData(ViewLayerType.TURKISH_WORD) != null && parseNode.getLayerData(ViewLayerType.TURKISH_WORD).equals("*NONE*") && (parseNode.getParent().getData().getName().equals("RB") || parseNode.getParent().getData().getName().equals("VBN") || parseNode.getParent().getData().getName().equals("VB") || parseNode.getParent().getData().getName().equals("VBZ") || parseNode.getParent().getData().getName().equals("VBP") || parseNode.getParent().getData().getName().equals("VBD"));
                if (parseNode.getLayerData(ViewLayerType.TURKISH_WORD) == null || noneCase){
                    word = (WordTranslations) dictionary.getWord(parseNode.getLayerData(ViewLayerType.ENGLISH_WORD).toLowerCase());
                    if (word != null && word.translationCount() > 0){
                        word.sortTranslations();
                        parseNode.getLayerInfo().setLayerData(ViewLayerType.TURKISH_WORD, autoTranslateWithRules((ParseNodeDrawable) parseNode.getParent(), noneCase, parentList, englishWordList, j, word));
                    } else {
                        String english = parseNode.getLayerData(ViewLayerType.ENGLISH_WORD).toLowerCase();
                        if (parseNode.getParent().getData().getName().startsWith("NNP")){
                            parseNode.getLayerInfo().setLayerData(ViewLayerType.TURKISH_WORD, english);
                        } else {
                            if (parseNode.getParent().getData().getName().equals("CD")){
                                try{
                                    Integer.parseInt(english);
                                    parseNode.getLayerInfo().setLayerData(ViewLayerType.TURKISH_WORD, english);
                                } catch (NumberFormatException nfe) {
                                    try {
                                        Double.parseDouble(english);
                                        parseNode.getLayerInfo().setLayerData(ViewLayerType.TURKISH_WORD, english);
                                    } catch (NumberFormatException nfe2) {
                                        parseNode.getLayerInfo().setLayerData(ViewLayerType.TURKISH_WORD, english.replaceAll(",", "").replaceAll("\\.", ","));
                                    }
                                }
                            } else {
                                sourceWord = (SourceWord) bilingualDictionary.getWord(english);
                                if (sourceWord != null && sourceWord.translationCount() > 0){
                                    parseNode.getLayerInfo().setLayerData(ViewLayerType.TURKISH_WORD, autoTranslateWithRules((ParseNodeDrawable) parseNode.getParent(), noneCase, parentList, englishWordList, j, new WordTranslations(sourceWord)));
                                } else {
                                    switch (parseNode.getParent().getData().getName()){
                                        case "NNS":
                                            word = bilingualDictionary.inPluralForm(english);
                                            break;
                                        case "VBG":
                                            word = bilingualDictionary.inIngForm(english);
                                            break;
                                        case "VBN":
                                        case "VBD":
                                            word = bilingualDictionary.inPastForm(english);
                                            break;
                                        case "VBZ":
                                            word = bilingualDictionary.inThirdPersonForm(english);
                                            break;
                                        case "CD":
                                        default:
                                            word = null;
                                    }
                                    if (word != null){
                                        parseNode.getLayerInfo().setLayerData(ViewLayerType.TURKISH_WORD, autoTranslateWithRules((ParseNodeDrawable) parseNode.getParent(), noneCase, parentList, englishWordList, j, word));
                                    } else {
                                        if (sourceWord != null){
                                            parseNode.getLayerInfo().setLayerData(ViewLayerType.TURKISH_WORD, sourceWord.getName());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                j++;
            }
        }
    }

    protected AutoTranslator(ViewLayerType secondLanguage, AutomaticTranslationDictionary dictionary, BilingualDictionary bilingualDictionary){
        this.dictionary = dictionary;
        this.bilingualDictionary = bilingualDictionary;
    }

    public void autoTranslate(ParseTreeDrawable parseTree){
        autoPreprocessor.autoFillPunctuation(parseTree);
        autoPreprocessor.autoFillNullElements(parseTree);
        autoPreprocessor.autoFillWithNoneTags(parseTree);
        autoPreprocessor.autoSwap(parseTree);
        autoFillWithTranslations(parseTree);
    }
}
