package AnnotatedTree.AutoProcessor.AutoTransfer;

import AnnotatedSentence.ViewLayerType;
import Dictionary.*;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import MorphologicalAnalysis.FsmParse;
import MorphologicalAnalysis.FsmParseList;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.AutoProcessor.AutoTranslation.TurkishAutoPreprocessor;
import AnnotatedTree.Processor.Condition.IsLeafNode;
import AnnotatedTree.Processor.Condition.IsTransferable;
import AnnotatedTree.Processor.NodeDrawableCollector;
import Translation.AutomaticTranslationDictionary;
import Translation.WordTranslations;

import java.util.ArrayList;
import java.util.Locale;

public class TurkishAutoTransfer extends AutoTransfer{
    private FsmMorphologicalAnalyzer morphologicalAnalyzer;
    private TxtDictionary txtDictionary;
    private AutomaticTranslationDictionary translationDictionary;

    public TurkishAutoTransfer(){
        super(ViewLayerType.TURKISH_WORD);
        autoPreprocessor = new TurkishAutoPreprocessor();
        txtDictionary = new TxtDictionary("Data/Dictionary/turkish_dictionary.txt", new TurkishWordComparator());
        morphologicalAnalyzer = new FsmMorphologicalAnalyzer("turkish_finite_state_machine.xml", txtDictionary);
        translationDictionary = new AutomaticTranslationDictionary("Data/Dictionary/translation.xml", new EnglishWordComparator());
    }

    protected void autoTransferSameWords(ParseTreeDrawable parseTree, TransferredSentence sentence) {
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            String english = parseNode.getLayerData(ViewLayerType.ENGLISH_WORD);
            if (parseNode.getLayerData(ViewLayerType.TURKISH_WORD) == null && english != null){
                for (int i = 0; i < sentence.wordCount(); i++){
                    if (english.equalsIgnoreCase(sentence.getWord(i).getName()) || sentence.getWord(i).getName().startsWith(english + "'")){
                        parseNode.getLayerInfo().setLayerData(ViewLayerType.TURKISH_WORD, sentence.getWord(i).getName());
                        sentence.transfer(i);
                        break;
                    }
                }
            }
        }
    }

    private void autoTransferSinglesForPos(ArrayList<ParseNodeDrawable> leafList, TransferredSentence sentence, FsmParseList[] fsmParses, String pos, String[] parentPosList){
        int englishCount = 0, turkishCount, parseCount, turkishIndex;
        ParseNodeDrawable transferNode = null;
        for (ParseNodeDrawable parseNode : leafList){
            if (parseNode.getLayerData(ViewLayerType.TURKISH_WORD) == null){
                for (String parentPos : parentPosList) {
                    if (parseNode.getParent().getData().getName().equals(parentPos)) {
                        englishCount++;
                        transferNode = parseNode;
                        break;
                    }
                }
            }
        }
        if (englishCount == 1){
            turkishCount = 0;
            turkishIndex = -1;
            for (int i = 0; i < sentence.wordCount(); i++){
                if (!sentence.isTransferred(i)){
                    parseCount = 0;
                    for (int j = 0; j < fsmParses[i].size(); j++){
                        if (fsmParses[i].getFsmParse(j).getFinalPos() != null && fsmParses[i].getFsmParse(j).getFinalPos().equals(pos)){
                            parseCount++;
                        }
                    }
                    if (parseCount == 1){
                        turkishCount++;
                        turkishIndex = i;
                    }
                }
            }
            if (turkishCount == 1){
                transferNode.getLayerInfo().setLayerData(ViewLayerType.TURKISH_WORD, sentence.getWord(turkishIndex).getName());
                sentence.transfer(turkishIndex);
            }
        }
    }

    protected void autoTransferSingles(ParseTreeDrawable parseTree, TransferredSentence sentence) {
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        FsmParseList[] fsmParses = morphologicalAnalyzer.robustMorphologicalAnalysis(sentence);
        autoTransferSinglesForPos(leafList, sentence, fsmParses, "CONJ", new String[]{"CC"});
        autoTransferSinglesForPos(leafList, sentence, fsmParses, "NUM", new String[]{"CD"});
        autoTransferSinglesForPos(leafList, sentence, fsmParses, "DET", new String[]{"DT"});
    }

    private boolean checkPos(ParseNodeDrawable parentNode, Word word){
        FsmParseList fsmParseList = morphologicalAnalyzer.robustMorphologicalAnalysis(word.getName());
        if (parentNode.getData().getName().equals("JJ") || parentNode.getData().getName().equals("JJR") || parentNode.getData().getName().equals("JJS")){
            for (int i = 0; i < fsmParseList.size(); i++){
                FsmParse fsmParse = fsmParseList.getFsmParse(i);
                if (fsmParse.getFinalPos() != null && fsmParse.getFinalPos().equals("ADJ")){
                    return true;
                }
            }
            return false;
        }
        if (parentNode.getData().getName().equals("RB") || parentNode.getData().getName().equals("RBR") || parentNode.getData().getName().equals("RBS")){
            for (int i = 0; i < fsmParseList.size(); i++){
                FsmParse fsmParse = fsmParseList.getFsmParse(i);
                if (fsmParse.getFinalPos() != null && fsmParse.getFinalPos().equals("ADVERB")){
                    return true;
                }
            }
            return false;
        }
        if (parentNode.getData().getName().equals("NN") || parentNode.getData().getName().equals("NNS") || parentNode.getData().getName().equals("NNP")  || parentNode.getData().getName().equals("NNPS")){
            for (int i = 0; i < fsmParseList.size(); i++){
                FsmParse fsmParse = fsmParseList.getFsmParse(i);
                if (fsmParse.getFinalPos() != null && fsmParse.getFinalPos().equals("NOUN")){
                    return true;
                }
            }
            return false;
        }
        if (parentNode.getData().getName().equals("VB") || parentNode.getData().getName().equals("VBG") || parentNode.getData().getName().equals("VBD")  || parentNode.getData().getName().equals("VBN") || parentNode.getData().getName().equals("VBZ")  || parentNode.getData().getName().equals("VBP")){
            for (int i = 0; i < fsmParseList.size(); i++){
                FsmParse fsmParse = fsmParseList.getFsmParse(i);
                if (fsmParse.getFinalPos() != null && fsmParse.getFinalPos().equals("VERB")){
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private int checkForPossibleWord(String english, TransferredSentence sentence, int startIndex, int endIndex){
        WordTranslations translations = (WordTranslations) translationDictionary.getWord(english);
        if (translations != null){
            for (int i = startIndex; i <= endIndex; i++){
                for (int j = 0; j < translations.translationCount(); j++){
                    if (sentence.getWord(i).getName().toLowerCase(new Locale("tr")).startsWith(translations.getTranslation(j).getTranslation().toLowerCase(new Locale("tr")))){
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private void autoTransferInterval(TransferredSentence sentence, ArrayList<ParseNodeDrawable> leafList, int i, int pi, int j, int pj){
        if (i - pi == j - pj){
            for (int k = pi + 1; k < i; k++){
                if (leafList.get(k).getLayerData(ViewLayerType.TURKISH_WORD) == null && checkPos((ParseNodeDrawable) leafList.get(k).getParent(), sentence.getWord(k - pi + pj))){
                    leafList.get(k).getLayerInfo().setLayerData(ViewLayerType.TURKISH_WORD, sentence.getWord(k - pi + pj).getName());
                }
            }
        } else {
            for (int k = pi + 1; k < i; k++){
                int wordIndex = checkForPossibleWord(leafList.get(k).getLayerData(ViewLayerType.ENGLISH_WORD), sentence, pj + 1, j - 1);
                if (wordIndex != -1 && leafList.get(k).getLayerData(ViewLayerType.TURKISH_WORD) == null && checkPos((ParseNodeDrawable) leafList.get(k).getParent(), sentence.getWord(wordIndex))){
                    leafList.get(k).getLayerInfo().setLayerData(ViewLayerType.TURKISH_WORD, sentence.getWord(wordIndex).getName());
                }
            }
        }
    }

    protected void autoTransferWordsOnInterval(ParseTreeDrawable parseTree, TransferredSentence sentence){
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTransferable(ViewLayerType.TURKISH_WORD));
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        int i = 0, j = 0, pi = -1, pj = -1;
        while (i < leafList.size()){
            String turkish = leafList.get(i).getLayerData(ViewLayerType.TURKISH_WORD);
            if (turkish != null){
                while (j < sentence.wordCount() && !turkish.equals(sentence.getWord(j).getName())){
                    j++;
                }
                if (j != sentence.wordCount()){
                    autoTransferInterval(sentence, leafList, i, pi, j, pj);
                    pi = i;
                    pj = j;
                } else {
                    i++;
                    if (i == leafList.size()){
                        autoTransferInterval(sentence, leafList, i, pi, j, pj);
                    }
                    break;
                }
            }
            i++;
        }
    }

}
