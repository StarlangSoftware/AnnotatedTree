package AnnotatedTree.AutoProcessor.AutoTranslation;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.AutoProcessor.AutoTranslation.PartOfSpeech.*;
import Dictionary.*;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import MorphologicalAnalysis.FsmParse;
import MorphologicalAnalysis.FsmParseList;
import AnnotatedTree.ParseNodeDrawable;
import Translation.AutomaticTranslationDictionary;
import Translation.BilingualDictionary;
import Translation.WordTranslationCandidate;
import Translation.WordTranslations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TurkishAutoTranslator extends AutoTranslator{
    private FsmMorphologicalAnalyzer morphologicalAnalyzer;
    private TxtDictionary txtDictionary;

    public TurkishAutoTranslator(AutomaticTranslationDictionary dictionary, BilingualDictionary bilingualDictionary) {
        super(ViewLayerType.TURKISH_WORD, dictionary, bilingualDictionary);
        autoPreprocessor = new TurkishAutoPreprocessor();
        txtDictionary = new TxtDictionary("Data/Dictionary/turkish_dictionary.txt", new TurkishWordComparator());
        morphologicalAnalyzer = new FsmMorphologicalAnalyzer("turkish_finite_state_machine.xml", txtDictionary);
    }

    private String prefix(String root){
        String[] words = root.split(" ");
        switch (words.length){
            case 2:
                return words[0] + " ";
            case 3:
                return words[0] + " " + words[1] + " ";
        }
        return "";
    }

    private String getPossibleRoot(WordTranslations translations, String pos){
        String newTag;
        double maxValue = 0.0;
        int validParseCount;
        String root = null, currentRoot;
        HashMap<String, Double> roots = new HashMap<>();
        switch (pos){
            case "JJ":
            case "JJR":
            case "JJS":
                newTag = "ADJ";
                break;
            case "VBG":
            case "VBD":
            case "VBN":
            case "VB":
            case "VBZ":
            case "VBP":
                newTag = "VERB";
                for (int i = 0; i < translations.translationCount(); i++){
                    WordTranslationCandidate candidate = (WordTranslationCandidate) translations.getTranslation(i);
                    String[] words = candidate.getTranslation().split(" ");
                    FsmParseList fsmParseList = morphologicalAnalyzer.morphologicalAnalysis(words[words.length - 1]);
                    for (int j = 0; j < fsmParseList.size(); j++){
                        FsmParse fsmParse = fsmParseList.getFsmParse(j);
                        String verbCandidate = fsmParse.getLastLemmaWithTag(newTag);
                        if (verbCandidate != null && txtDictionary.getWord(verbCandidate) == null){
                            txtDictionary.addVerb(verbCandidate);
                        }
                    }
                }
                break;
            case "NNP":
            case "NNPS":
                for (int i = 0; i < translations.translationCount(); i++){
                    WordTranslationCandidate candidate = (WordTranslationCandidate) translations.getTranslation(i);
                    String[] words = candidate.getTranslation().split(" ");
                    for (String word : words){
                        if (word.contains("'") && dictionary.getWord(word.substring(0, word.indexOf('\''))) == null){
                            txtDictionary.addProperNoun(word.substring(0, word.indexOf('\'')));
                        } else {
                            FsmParseList fsmParseList = morphologicalAnalyzer.morphologicalAnalysis(word);
                            if (fsmParseList.size() == 0){
                                txtDictionary.addProperNoun(word);
                            }
                        }
                    }
                }
            case "NN":
            case "NNS":
                newTag = "NOUN";
                for (int i = 0; i < translations.translationCount(); i++){
                    WordTranslationCandidate candidate = (WordTranslationCandidate) translations.getTranslation(i);
                    String[] words = candidate.getTranslation().split(" ");
                    FsmParseList fsmParseList = morphologicalAnalyzer.morphologicalAnalysis(words[words.length - 1]);
                    for (int j = 0; j < fsmParseList.size(); j++){
                        FsmParse fsmParse = fsmParseList.getFsmParse(j);
                        String nounCandidate = fsmParse.getLastLemmaWithTag(newTag);
                        if (nounCandidate != null && txtDictionary.getWord(nounCandidate) == null){
                            txtDictionary.addNoun(nounCandidate);
                        }
                    }
                }
                break;
            case "CD":
                newTag = "NUM";
                for (int i = 0; i < translations.translationCount(); i++){
                    WordTranslationCandidate candidate = (WordTranslationCandidate) translations.getTranslation(i);
                    String[] words = candidate.getTranslation().split(" ");
                    for (String word : words){
                        if (word.contains("'")){
                            String subWord = word.substring(0, word.indexOf('\''));
                            if (txtDictionary.getWord(subWord) == null){
                                searchAndAddNumber(subWord);
                            }
                        }
                    }
                }
                break;
            case "RB":
                return "değil";
            case "$":
                return "dolar";
            case "RBR":
            case "RBS":
                newTag = "ADV";
                break;
            default:
                return null;
        }
        for (int i = 0; i < translations.translationCount(); i++){
            WordTranslationCandidate candidate = (WordTranslationCandidate) translations.getTranslation(i);
            String[] words = candidate.getTranslation().split(" ");
            FsmParseList fsmParseList = morphologicalAnalyzer.morphologicalAnalysis(words[words.length - 1]);
            validParseCount = 0;
            for (int j = 0; j < fsmParseList.size(); j++) {
                FsmParse fsmParse = fsmParseList.getFsmParse(j);
                if (fsmParse.getRootPos().equals(newTag) || pos.equals("RBR") || pos.equals("RBS")){
                    if (pos.startsWith("NNP") && !fsmParse.isProperNoun() && !(words[0].equalsIgnoreCase("bay") || words[0].equalsIgnoreCase("bayan"))){
                        continue;
                    }
                    validParseCount++;
                }
            }
            for (int j = 0; j < fsmParseList.size(); j++) {
                FsmParse fsmParse = fsmParseList.getFsmParse(j);
                if (fsmParse.getRootPos().equals(newTag) || pos.equals("RBR") || pos.equals("RBS")){
                    if (pos.startsWith("NNP") && !fsmParse.isProperNoun() && !(words[0].equalsIgnoreCase("bay") || words[0].equalsIgnoreCase("bayan"))){
                        continue;
                    }
                    String candidateRoot;
                    if (pos.startsWith("VB") || pos.equals("NN") || pos.equals("NNS")){
                        candidateRoot = fsmParse.getLastLemmaWithTag(newTag);
                    } else {
                        candidateRoot = fsmParse.getWord().getName();
                    }
                    switch (words.length){
                        default:
                        case 1:
                            currentRoot = candidateRoot;
                            break;
                        case 2:
                            currentRoot = words[0] + " " + candidateRoot;
                            break;
                        case 3:
                            currentRoot = words[0] + " " + words[1] + " " + candidateRoot;
                            break;
                    }
                    if (roots.containsKey(currentRoot)){
                        roots.put(currentRoot, roots.get(currentRoot) + candidate.getCount() / (validParseCount + 0.0));
                    } else {
                        roots.put(currentRoot, candidate.getCount() / (validParseCount + 0.0));
                    }
                }
            }
        }
        for (String word : roots.keySet()){
            if (roots.get(word) > maxValue){
                maxValue = roots.get(word);
                root = word;
            }
        }
        return root;
    }

    private boolean searchAndAddNumber(String word){
        try{
            Integer.parseInt(word);
            txtDictionary.addNumber(word);
            return true;
        } catch (NumberFormatException nfe) {
            try {
                Double.parseDouble(word);
                txtDictionary.addRealNumber(word);
                return true;
            } catch (NumberFormatException nfe2) {
                if (word.matches("\\d+\\\\/\\d+")){
                    txtDictionary.addFraction(word);
                    return true;
                }
            }
        }
        return false;
    }

    protected String autoTranslateWithRules(ParseNodeDrawable parseNode, boolean noneCase, ArrayList<String> parents, ArrayList<String> englishWords, int index, WordTranslations translations) {
        String root, result = null;
        boolean withDigits;
        PartOfSpeechTranslator translator = null;
        List<String> parentList = parents.subList(index, parents.size());
        List<String> englishWordList = englishWords.subList(index, englishWords.size());
        if (noneCase && !parentList.get(0).equals("RB")){
            if (index > 0 && !parents.get(index - 1).startsWith("V") && !parents.get(index - 1).startsWith("NN") && !parents.get(index - 1).equals("RB") && !parents.get(index - 1).startsWith("J") && !parents.get(index - 1).equalsIgnoreCase("MD")){
                if ((parentList.get(0).equals("VBG") && englishWordList.get(0).equalsIgnoreCase("being")) ||
                        (parentList.get(0).equals("VBD") && (englishWordList.get(0).equalsIgnoreCase("was") || englishWordList.get(0).equalsIgnoreCase("were"))) ||
                        (parentList.get(0).equals("VBN") && englishWordList.get(0).equalsIgnoreCase("been")) ||
                        (parentList.get(0).equals("VB") && englishWordList.get(0).equalsIgnoreCase("be")) ||
                        ((parentList.get(0).equals("VBZ") || parentList.get(0).equals("VBP")) && (englishWordList.get(0).equalsIgnoreCase("is") || englishWordList.get(0).equalsIgnoreCase("are") || englishWordList.get(0).equalsIgnoreCase("'s") || englishWordList.get(0).equalsIgnoreCase("'re")))){
                    root = "ol";
                } else {
                    return "*NONE*";
                }
            } else {
                return "*NONE*";
            }
        } else {
            root = getPossibleRoot(translations, parentList.get(0));
            if (root == null){
                if (translations.translationCount() > 0){
                    return translations.getTranslation(0).getTranslation();
                } else {
                    return "";
                }
            }
        }
        String lastWordForm = root.split(" ")[root.split(" ").length - 1];
        withDigits = searchAndAddNumber(lastWordForm);
        TxtWord lastWord = (TxtWord) txtDictionary.getWord(lastWordForm);
        if (lastWord == null){
            return translations.getTranslation(0).getTranslation();
        }
        String prefix = prefix(root);
        switch (parentList.get(0)){
            case "JJ":
                translator = new TurkishJJTranslator(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord);
                break;
            case "JJR":
                translator = new TurkishJJTranslator(parseNode, parentList, englishWordList, "daha ", lastWordForm, lastWord);
                break;
            case "JJS":
                translator = new TurkishJJTranslator(parseNode, parentList, englishWordList, "en ", lastWordForm, lastWord);
                break;
            case "VBG":
                translator = new TurkishVBGTranslator(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord);
                break;
            case "NN":
                translator = new TurkishNNTranslator(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord, txtDictionary);
                break;
            case "CD":
                translator = new TurkishCDTranslator(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord, withDigits, txtDictionary);
                break;
            case "NNP":
            case "NNPS":
            case "$":
                translator = new TurkishNNPTranslator(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord, txtDictionary);
                break;
            case "VBD":
                translator = new TurkishVBDTranslator(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord);
                break;
            case "VBN":
                translator = new TurkishVBNTranslator(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord);
                break;
            case "NNS":
                ParseNodeDrawable previous = (ParseNodeDrawable) parseNode.previousSibling();
                if ((previous != null && (previous.getData().getName().equals("CD") || previous.getData().getName().equals("QP"))) || (previous != null && previous.getData().getName().equals("DT") && !englishWords.get(index - 1).equalsIgnoreCase("those") && !englishWords.get(index - 1).equalsIgnoreCase("these") && !englishWords.get(index - 1).equalsIgnoreCase("all") && !englishWords.get(index - 1).equalsIgnoreCase("some") && !englishWords.get(index - 1).equalsIgnoreCase("the"))){
                    translator = new TurkishNNTranslator(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord, txtDictionary);
                } else {
                    translator = new TurkishNNSTranslator(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord, txtDictionary);
                }
                break;
            case "VB":
                translator = new TurkishVBTranslator(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord);
                break;
            case "VBZ":
            case "VBP":
                translator = new TurkishVBZTranslator(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord);
                break;
            case "RB":
                if (englishWordList.get(0).equalsIgnoreCase("not") || englishWordList.get(0).equalsIgnoreCase("n't")){
                    if (index > 0 && !parents.get(index - 1).startsWith("V") && !parents.get(index - 1).equalsIgnoreCase("MD")){
                        translator = new TurkishRBTranslator(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord);
                    } else {
                        result = "*NONE*";
                    }
                }
                break;
            case "RBR":
                if (englishWordList.get(0).equalsIgnoreCase("more")){
                    return "daha";
                } else {
                    return "daha " + lastWordForm;
                }
            case "RBS":
                if (englishWordList.get(0).equalsIgnoreCase("most")){
                    return "en";
                } else {
                    return "en " + lastWordForm;
                }
        }
        if (translator != null){
            result = translator.translate();
        }
        if (result == null){
            return translations.getTranslation(0).getTranslation();
        } else {
            return result;
        }
    }

}
