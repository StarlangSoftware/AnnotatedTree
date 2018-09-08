package AnnotatedTree.AutoProcessor.AutoDisambiguation;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.AutoProcessor.AutoDisambiguation.PartOfSpeech.*;
import Corpus.Sentence;
import Dictionary.Word;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import MorphologicalAnalysis.FsmParse;
import MorphologicalAnalysis.FsmParseList;
import MorphologicalDisambiguation.RootWordStatistics;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.Processor.Condition.IsTurkishLeafNode;
import AnnotatedTree.Processor.NodeDrawableCollector;

import java.util.ArrayList;
import java.util.HashMap;

public class TurkishTreeAutoDisambiguator extends TreeAutoDisambiguator {

    public TurkishTreeAutoDisambiguator(RootWordStatistics rootWordStatistics) {
        super(new FsmMorphologicalAnalyzer(), rootWordStatistics);
    }

    protected boolean autoFillSingleAnalysis(ParseTreeDrawable parseTree){
        boolean modified = false;
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            if (parseNode.getLayerData(ViewLayerType.INFLECTIONAL_GROUP) == null){
                String turkishWords = parseNode.getLayerData(ViewLayerType.TURKISH_WORD);
                if (turkishWords != null){
                    String[] words = turkishWords.split(" ");
                    String morphologicalAnalysis = "", morphotactics = "";
                    for (String word : words){
                        FsmParseList fsmParseList = morphologicalAnalyzer.robustMorphologicalAnalysis(word);
                        if (fsmParseList.size() == 1){
                            morphologicalAnalysis = morphologicalAnalysis + " " + fsmParseList.getFsmParse(0).transitionList();
                            morphotactics = morphotactics + " " + fsmParseList.getFsmParse(0).withList();
                        } else {
                            morphologicalAnalysis = "";
                            morphotactics = "";
                            break;
                        }
                    }
                    if (morphologicalAnalysis.length() > 0){
                        modified = true;
                        parseNode.getLayerInfo().setLayerData(ViewLayerType.INFLECTIONAL_GROUP, morphologicalAnalysis.trim());
                        parseNode.getLayerInfo().setLayerData(ViewLayerType.META_MORPHEME, morphotactics.trim());
                    }
                }
            }
        }
        return modified;
    }

    private Word[][] getRootWords(FsmParseList[] fsmParses){
        Word[][] rootWords = new Word[fsmParses.length][];
        for (int i = 0; i < fsmParses.length; i++){
            HashMap<String, Word> roots = new HashMap<String, Word>();
            for (int j = 0; j < fsmParses[i].size(); j++){
                if (!roots.containsKey(fsmParses[i].getFsmParse(j).getWord().getName())){
                    roots.put(fsmParses[i].getFsmParse(j).getWord().getName(), fsmParses[i].getFsmParse(j).getWord());
                }
            }
            rootWords[i] = new Word[roots.size()];
            int j = 0;
            for (Word word : roots.values()){
                rootWords[i][j] = word;
                j++;
            }
        }
        return rootWords;
    }

    private boolean containsSingleRootWord(Word[][] rootWords){
        for (Word[] rootWord : rootWords) {
            if (rootWord == null || rootWord.length != 1) {
                return false;
            }
        }
        return true;
    }

    private void setDisambiguatedParses(FsmParse[] disambiguatedFsmParses, ParseNodeDrawable parseNode){
        String morphologicalAnalysis = disambiguatedFsmParses[0].transitionList();
        String morphotactics = disambiguatedFsmParses[0].withList();
        for (int i = 1; i < disambiguatedFsmParses.length; i++){
            morphologicalAnalysis = morphologicalAnalysis + " " + disambiguatedFsmParses[i].transitionList();
            morphotactics = morphotactics + " " + disambiguatedFsmParses[i].withList();
        }
        parseNode.getLayerInfo().setLayerData(ViewLayerType.INFLECTIONAL_GROUP, morphologicalAnalysis);
        parseNode.getLayerInfo().setLayerData(ViewLayerType.META_MORPHEME, morphotactics);
    }

    private boolean disambiguateSingleRootWord(ParseNodeDrawable parseNode, FsmParseList[] fsmParseLists){
        FsmParse[] disambiguatedFsmParses = new FsmParse[fsmParseLists.length];
        boolean disambiguated = true;
        for (int i = 0; i < fsmParseLists.length; i++){
            disambiguatedFsmParses[i] = fsmParseLists[i].caseDisambiguator();
            if (disambiguatedFsmParses[i] == null){
                disambiguated = false;
            }
        }
        if (disambiguated){
            setDisambiguatedParses(disambiguatedFsmParses, parseNode);
        }
        return disambiguated;
    }

    protected boolean autoDisambiguateSingleRootWords(ParseTreeDrawable parseTree){
        boolean modified = false;
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList) {
            if (parseNode.getLayerData(ViewLayerType.INFLECTIONAL_GROUP) == null) {
                String turkishWords = parseNode.getLayerData(ViewLayerType.TURKISH_WORD);
                if (turkishWords != null) {
                    FsmParseList[] fsmParseLists = morphologicalAnalyzer.robustMorphologicalAnalysis(new Sentence(turkishWords));
                    if (containsSingleRootWord(getRootWords(fsmParseLists))){
                        modified = modified || disambiguateSingleRootWord(parseNode, fsmParseLists);
                    }
                }
            }
        }
        return modified;
    }

    protected boolean autoDisambiguateMultipleRootWords(ParseTreeDrawable parseTree) {
        boolean modified = false;
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList) {
            if (parseNode.getLayerData(ViewLayerType.INFLECTIONAL_GROUP) == null) {
                String turkishWords = parseNode.getLayerData(ViewLayerType.TURKISH_WORD);
                if (turkishWords != null) {
                    FsmParseList[] fsmParseLists = morphologicalAnalyzer.robustMorphologicalAnalysis(new Sentence(turkishWords));
                    if (!containsSingleRootWord(getRootWords(fsmParseLists))){
                        for (FsmParseList parseList : fsmParseLists){
                            String bestRootWord = rootWordStatistics.bestRootWord(parseList, 0.0);
                            if (bestRootWord != null){
                                parseList.reduceToParsesWithSameRoot(bestRootWord);
                            }
                        }
                    }
                    if (containsSingleRootWord(getRootWords(fsmParseLists))){
                        modified = modified || disambiguateSingleRootWord(parseNode, fsmParseLists);
                    }
                }
            }
        }
        return modified;
    }

    protected boolean autoDisambiguateWithRules(ParseTreeDrawable parseTree) {
        PartOfSpeechDisambiguator disambiguator;
        FsmParse[] disambiguatedFsmParses;
        boolean modified = false;
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (int i = 0; i < leafList.size(); i++){
            ParseNodeDrawable parseNode = leafList.get(i);
            if (parseNode.getLayerData(ViewLayerType.INFLECTIONAL_GROUP) == null){
                String turkishWords = parseNode.getLayerData(ViewLayerType.TURKISH_WORD);
                if (turkishWords != null) {
                    FsmParseList[] fsmParseList = morphologicalAnalyzer.robustMorphologicalAnalysis(new Sentence(turkishWords));
                    switch (parseNode.getParent().getData().getName()){
                        case "RB":
                        case "RBR":
                        case "RBS":
                            disambiguator = new TurkishRBDisambiguator();
                            break;
                        case "IN":
                        case "TO":
                            disambiguator = new TurkishINTODisambiguator();
                            break;
                        case "CC":
                            disambiguator = new TurkishCCDisambiguator();
                            break;
                        case "WDT":
                        case "DT":
                            disambiguator = new TurkishDTDisambiguator();
                            break;
                        case "CD":
                            disambiguator = new TurkishCDDisambiguator();
                            break;
                        case "PRP":
                        case "PRP$":
                        case "WP":
                        case "WP$":
                            disambiguator = new TurkishPRPDisambiguator();
                            break;
                        case "NNP":
                        case "NNPS":
                            disambiguator = new TurkishNNPDisambiguator();
                            break;
                        case "NN":
                        case "NNS":
                            disambiguator = new TurkishNNDisambiguator();
                            break;
                        case "JJ":
                        case "JJR":
                        case "JJS":
                            disambiguator = new TurkishJJDisambiguator();
                            break;
                        case "$":
                            disambiguator = new TurkishDollarDisambiguator();
                            break;
                        case "VBN":
                        case "VBZ":
                        case "VBD":
                        case "VB":
                        case "VBG":
                        case "VBP":
                            if (TurkishPartOfSpeechDisambiguator.isLastNode(i, leafList)){
                                disambiguator = new TurkishVBDisambiguator();
                            } else {
                                disambiguator = null;
                            }
                            break;
                        default:
                            disambiguator = null;
                    }
                    if (disambiguator != null){
                        disambiguatedFsmParses = disambiguator.disambiguate(fsmParseList, parseNode, parseTree);
                        if (disambiguatedFsmParses != null){
                            modified = true;
                            setDisambiguatedParses(disambiguatedFsmParses, parseNode);
                        }
                    }
                }
            }
        }
        return modified;
    }

}
