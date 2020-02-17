package AnnotatedTree;

import AnnotatedSentence.*;
import AnnotatedSentence.AnnotatedSentence;
import AnnotatedTree.Processor.ConvertToTurkishParseTree;
import ContextFreeGrammar.*;
import Dictionary.*;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import AnnotatedTree.Processor.Condition.*;
import AnnotatedTree.Processor.LeafConverter.LeafToLanguageConverter;
import AnnotatedTree.Processor.LeafConverter.LeafToTurkish;
import AnnotatedTree.Processor.NodeDrawableCollector;
import AnnotatedTree.Processor.TreeToStringConverter;
import MorphologicalAnalysis.MorphologicalParse;
import MorphologicalAnalysis.MorphologicalTag;
import MorphologicalDisambiguation.RootWordStatistics;
import ParseTree.ParseNode;
import ParseTree.Symbol;
import ParseTree.TreeBank;
import ProbabilisticContextFreeGrammar.ProbabilisticContextFreeGrammar;
import Corpus.*;
import PropBank.*;
import AnnotatedTree.Util.DoneFileFilter;
import Util.Interval;
import WordNet.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

public class TestTreeBank {

    public static void interlingualMultipleCandidates(int count){
        ParallelTreeBankDrawable treebank = new ParallelTreeBankDrawable(new File("../../Penn-Treebank/English"), new File("../../Penn-Treebank/Turkish"));
        WordNet turkishWordNet = new WordNet();
        WordNet englishWordNet = new WordNet("english_wordnet_version_31.xml");
        try {
            PrintWriter pw = new PrintWriter("output-" + count + "-control.txt");
            for (int i = 0; i < treebank.size(); i++) {
                ParseTreeDrawable turkishParseTree = treebank.toTree(i);
                NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) turkishParseTree.getRoot(), new IsLeafNode());
                ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
                for (ParseNodeDrawable parseNode : leafList) {
                    LayerInfo layerInfo = parseNode.getLayerInfo();
                    if (layerInfo.layerExists(ViewLayerType.ENGLISH_SEMANTICS) && layerInfo.layerExists(ViewLayerType.SEMANTICS)) {
                        if (layerInfo.getNumberOfMeanings() == count) {
                            String turkishText = "";
                            for (int j = 0; j < count; j++){
                                SynSet turkish = turkishWordNet.getSynSetWithId(layerInfo.getSemanticAt(j));
                                if (turkish != null){
                                    turkishText = turkishText + "\t" + turkish.getId() + "\t" + turkish.getSynonym() + "\t" + turkish.getDefinition();
                                }
                            }
                            SynSet english = englishWordNet.getSynSetWithId(layerInfo.getLayerData(ViewLayerType.ENGLISH_SEMANTICS));
                            if (english != null) {
                                pw.println(layerInfo.getLayerData(ViewLayerType.ENGLISH_WORD) + "\t" + layerInfo.getLayerData(ViewLayerType.TURKISH_WORD) + "\t" + turkishParseTree.getFileDescription().getRawFileName() + "\t" + english.getId() + "\t" + english.getSynonym() + "\t" + english.getDefinition() + "\t" + turkishText);
                            }
                        }
                    }
                }
            }
            pw.close();
        } catch (FileNotFoundException | WordNotExistsException | LayerNotExistsException e) {
        }
    }

    public static void interlingualCandidates(int count){
        ParallelTreeBankDrawable treebank = new ParallelTreeBankDrawable(new File("../../Penn-Treebank/English"), new File("../../Penn-Treebank/Turkish"));
        WordNet turkishWordNet = new WordNet();
        WordNet englishWordNet = new WordNet("english_wordnet_version_31.xml");
        try {
            PrintWriter pw = new PrintWriter("synonym-" + count + ".txt");
            for (int i = 0; i < treebank.size(); i++) {
                ParseTreeDrawable turkishParseTree = treebank.toTree(i);
                NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) turkishParseTree.getRoot(), new IsLeafNode());
                ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
                for (ParseNodeDrawable parseNode : leafList) {
                    LayerInfo layerInfo = parseNode.getLayerInfo();
                    if (layerInfo.layerExists(ViewLayerType.ENGLISH_SEMANTICS) && layerInfo.layerExists(ViewLayerType.SEMANTICS)) {
                        if (layerInfo.getNumberOfMeanings() == 1 && layerInfo.getNumberOfWords() == count) {
                            SynSet turkish = turkishWordNet.getSynSetWithId(layerInfo.getLayerData(ViewLayerType.SEMANTICS));
                            SynSet english = englishWordNet.getSynSetWithId(layerInfo.getLayerData(ViewLayerType.ENGLISH_SEMANTICS));
                            if (turkish != null && english != null) {
                                pw.println(layerInfo.getLayerData(ViewLayerType.ENGLISH_WORD) + "\t" + layerInfo.getLayerData(ViewLayerType.TURKISH_WORD) + "\t" + turkishParseTree.getFileDescription().getRawFileName() + "\t" + turkish.getId() + "\t" + turkish.getSynonym() + "\t" + turkish.getDefinition() + "\t" + english.getId() + "\t" + english.getSynonym() + "\t" + english.getDefinition());
                            }
                        }
                    }
                }
            }
            pw.close();
        } catch (FileNotFoundException | LayerNotExistsException e) {
            e.printStackTrace();
        }
    }

    public static void newSynSetCandidates(int count){
        ParallelTreeBankDrawable treebank = new ParallelTreeBankDrawable(new File("../../Penn-Treebank/English"), new File("../../Penn-Treebank/Turkish"));
        WordNet english = new WordNet("english_wordnet_version_31.xml");
        try {
            PrintWriter pw = new PrintWriter("synset-" + count + ".txt");
            for (int i = 0; i < treebank.size(); i++) {
                ParseTreeDrawable turkishParseTree = treebank.toTree(i);
                Sentence treeSentence = turkishParseTree.generateAnnotatedSentence();
                for (int j = 0; j < treeSentence.wordCount() - count + 1; j++) {
                    AnnotatedWord annotatedWord = (AnnotatedWord) treeSentence.getWord(j);
                    String turkishWord = annotatedWord.getName();
                    if (annotatedWord.getSemantic() != null && annotatedWord.getSemantic().equals("TUR10-0000000")){
                        for (int k = 1; k <= count - 1; k++){
                            AnnotatedWord annotatedWordToCompare = (AnnotatedWord) treeSentence.getWord(j + k);
                            turkishWord = turkishWord + " " + annotatedWordToCompare.getName();
                        }
                        ArrayList<SynSet> synSets = english.getSynSetsWithLiteral(turkishWord);
                        if (synSets.size() > 0) {
                            pw.println(turkishWord + "\t" + turkishParseTree.getFileDescription().getRawFileName() + "\t" + synSets.get(0).getId() + "\t" + synSets.get(0).getSynonym());
                        }
                    }
                }
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void multiWordCandidates(int count){
        ParallelTreeBankDrawable treebank = new ParallelTreeBankDrawable(new File("../../Penn-Treebank/English"), new File("../../Penn-Treebank/Turkish"));
        WordNet turkishWordNet = new WordNet();
        try {
            PrintWriter pw = new PrintWriter("multiword-" + count + ".txt");
            for (int i = 0; i < treebank.size(); i++) {
                ParseTreeDrawable turkishParseTree = treebank.toTree(i);
                Sentence treeSentence = turkishParseTree.generateAnnotatedSentence();
                for (int j = 0; j < treeSentence.wordCount() - count + 1; j++) {
                    AnnotatedWord annotatedWord = (AnnotatedWord) treeSentence.getWord(j);
                    String turkishWord = annotatedWord.getName();
                    if (annotatedWord.getSemantic() != null && !annotatedWord.getSemantic().startsWith("TUR10-00000")){
                        boolean found = true;
                        for (int k = 1; k <= count - 1; k++){
                            AnnotatedWord annotatedWordToCompare = (AnnotatedWord) treeSentence.getWord(j + k);
                            turkishWord = turkishWord + " " + annotatedWordToCompare.getName();
                            if (annotatedWordToCompare.getSemantic() == null || !annotatedWord.getSemantic().equals(annotatedWordToCompare.getSemantic())){
                                found = false;
                                break;
                            }
                        }
                        if (found) {
                            SynSet turkish = turkishWordNet.getSynSetWithId(annotatedWord.getSemantic());
                            if (turkish != null) {
                                pw.println(turkish.getId() + "\t" + turkishWord + "\t" + turkishParseTree.getFileDescription().getRawFileName() + "\t" + turkish.getSynonym() + "\t" + turkish.getDefinition());
                            }
                        }
                    }
                }
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void missingCandidates(){
        ParallelTreeBankDrawable treebank = new ParallelTreeBankDrawable(new File("../../Penn-Treebank/English"), new File("../../Penn-Treebank/Turkish"));
        try {
            PrintWriter pw = new PrintWriter("missing.txt");
            for (int i = 0; i < treebank.size(); i++) {
                ParseTreeDrawable turkishParseTree = treebank.toTree(i);
                NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) turkishParseTree.getRoot(), new IsLeafNode());
                ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
                for (ParseNodeDrawable parseNode : leafList) {
                    LayerInfo layerInfo = parseNode.getLayerInfo();
                    if (layerInfo.layerExists(ViewLayerType.ENGLISH_SEMANTICS) && !layerInfo.layerExists(ViewLayerType.SEMANTICS)) {
                        pw.println(layerInfo.getLayerData(ViewLayerType.ENGLISH_WORD) + "\t" + layerInfo.getLayerData(ViewLayerType.TURKISH_WORD) + "\t" + turkishParseTree.getFileDescription().getRawFileName());
                    }
                }
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void mapTrees(){
        ParallelTreeBankDrawable parallelTreebank = new ParallelTreeBankDrawable(new File("../Penn-Treebank/English"), new File("../Penn-Treebank/Turkish"));
        for (int i = 0; i < parallelTreebank.size(); i++){
            ParseTreeDrawable english = parallelTreebank.fromTree(i);
            ParseTreeDrawable turkish = parallelTreebank.toTree(i);
            HashMap<ParseNode, ParseNodeDrawable> nodeMap = english.mapTree(turkish);
            if (english.nodeCount() != nodeMap.size()){
                System.out.println(turkish.getFileDescription().getFileName() + "->" + english.nodeCount() + "->" + nodeMap.size());
            }
        }
    }

    public static void countWords(){
        TreeBank treeBank = new TreeBank(new File(TreeBankDrawable.ENGLISH_PATH), ".train", 5246, 8659);
        System.out.println(treeBank.wordCount(true));
    }

    public static void printSentences(String path){
        TreeBank treeBank = new TreeBank(new File(path), ".");
        for (int i = 0; i < treeBank.size(); i++){
            System.out.println(treeBank.get(i).toSentence());
        }
    }

    public static void printFileNames(String folder, String path){
        File[] listOfFiles = new File(path).listFiles();
        Arrays.sort(listOfFiles);
        for (File file : listOfFiles){
            System.out.println(folder + "$" + file.getName());
        }
    }

    public static void clearLayer(ViewLayerType layerType){
        TreeBankDrawable treeBank;
        treeBank = new TreeBankDrawable("../Turkish", "dev", 0, 28);
        treeBank.clearLayer(layerType);
        treeBank = new TreeBankDrawable("../Turkish", "dev", 30, 359);
        treeBank.clearLayer(layerType);
        treeBank = new TreeBankDrawable("../Turkish", "test", 0, 539);
        treeBank.clearLayer(layerType);
        treeBank = new TreeBankDrawable("../Turkish", "train", 0, 8659);
        treeBank.clearLayer(layerType);
    }

    public static void compareTreeWithPhrase(TreeBank treeBank){
        int total = 0, found = 0;
        for (int i = 0; i < treeBank.size(); i++){
            ParseTreeDrawable parseTree = (ParseTreeDrawable) treeBank.get(i);
            Sentence treeSentence = parseTree.generateAnnotatedSentence();
            Sentence phraseSentence = new Sentence(new File(TreeBankDrawable.TURKISH_PHRASE_PATH + parseTree.getFileDescription().getRawFileName()));
            for (int j = 0; j < phraseSentence.wordCount(); j++){
                for (int k = 0; k < treeSentence.wordCount(); k++){
                    if (phraseSentence.getWord(j).getName().equalsIgnoreCase(treeSentence.getWord(k).getName())){
                        phraseSentence.replaceWord(j, treeSentence.getWord(k));
                        found++;
                        break;
                    }
                }
            }
            phraseSentence.writeToFile(new File(TreeBankDrawable.TURKISH_PHRASE_PATH + parseTree.getFileDescription().getRawFileName()));
            total += phraseSentence.wordCount();
            System.out.println(phraseSentence.toString());
        }
        System.out.println(found + "/" + total);
    }

    public static void semanticAnnotationControl(){
        WordNet turkish = new WordNet();
        TreeBankDrawable treeBank = new TreeBankDrawable(new File("../Penn-Treebank/Turkish"));
        AnnotatedCorpus corpus = new AnnotatedCorpus(new File("../Penn-Treebank/Turkish-Phrase"), ".");
        try {
            PrintWriter pw = new PrintWriter("output.txt");
            for (int i = 0; i < treeBank.size(); i++){
                NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) treeBank.get(i).getRoot(), new IsTurkishLeafNode());
                ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
                for (ParseNodeDrawable leafNode : leafList){
                    LayerInfo layerInfo = leafNode.getLayerInfo();
                    if (layerInfo.getLayerData(ViewLayerType.SEMANTICS) != null){
                        TreeToStringConverter treeToStringConverter = new TreeToStringConverter(treeBank.get(i), new LeafToTurkish());
                        String sentence = treeToStringConverter.convert();
                        String id = layerInfo.getLayerData(ViewLayerType.SEMANTICS);
                        SynSet synSet = turkish.getSynSetWithId(id);
                        if (synSet != null){
                            String rootWords = "";
                            try {
                                rootWords = layerInfo.getMorphologicalParseAt(0).getWord().getName();
                                for (int j = 1; j < layerInfo.getNumberOfWords(); j++){
                                    rootWords = rootWords + " " + layerInfo.getMorphologicalParseAt(j).getWord().getName();
                                }
                            } catch (LayerNotExistsException | WordNotExistsException e) {
                                e.printStackTrace();
                            }
                            pw.println("Turkish\t" + treeBank.get(i).getFileDescription().getRawFileName() + "\t" + rootWords + "\t" + id + "\t" + synSet.getSynonym().toString() + "\t" + synSet.getDefinition() + "\t" + sentence);
                        }
                    }
                }
            }
            for (int i = 0; i < corpus.sentenceCount(); i++){
                AnnotatedSentence sentence = (AnnotatedSentence) corpus.getSentence(i);
                for (int j = 0; j < sentence.wordCount(); j++){
                    AnnotatedWord word = (AnnotatedWord) sentence.getWord(j);
                    String id = word.getSemantic();
                    if (id != null && turkish.getSynSetWithId(id) != null){
                        SynSet synSet = turkish.getSynSetWithId(id);
                        pw.println("Turkish-Phrase\t" + sentence.getFileName() + "\t" + word.getParse().getWord().getName() + "\t" + id + "\t" + synSet.getSynonym().toString() + "\t" + synSet.getDefinition() + "\t" + sentence.toWords());
                    }
                }
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void propBankAnnotationCheckAndUpdate() {
        TreeBankDrawable treeBank = new TreeBankDrawable(new File("../../Penn-Treebank/Turkish"));
        for (int i = 0; i < treeBank.size(); i++){
            NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) treeBank.get(i).getRoot(), new IsTurkishLeafNode());
            ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
            for (ParseNodeDrawable leafNode : leafList){
                LayerInfo layerInfo = leafNode.getLayerInfo();
                if (layerInfo.getNumberOfMeanings() == 1 && layerInfo.getArgument() != null && layerInfo.getArgument().getArgumentType().equals("PREDICATE") && !layerInfo.getArgument().getId().equals(layerInfo.getLayerData(ViewLayerType.SEMANTICS))) {
                    String oldId = layerInfo.getArgument().getId();
                    String newId = layerInfo.getLayerData(ViewLayerType.SEMANTICS);
                    if (treeBank.get(i).updateConnectedPredicate(oldId, newId)){
                        treeBank.get(i).save();
                    }
                }
            }
        }
    }

    public static void propbankAnnotationControl(){
        WordNet turkish = new WordNet();
        SynSet synSet;
        TreeBankDrawable treeBank = new TreeBankDrawable(new File("../../Penn-Treebank/Turkish"));
        try {
            PrintWriter pw = new PrintWriter("output.txt");
            for (int i = 0; i < treeBank.size(); i++){
                TreeToStringConverter treeToStringConverter = new TreeToStringConverter(treeBank.get(i), new LeafToTurkish());
                String sentence = treeToStringConverter.convert();
                NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) treeBank.get(i).getRoot(), new IsTurkishLeafNode());
                ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
                String phrase = "";
                String previousAnnotation = "";
                for (ParseNodeDrawable leafNode : leafList){
                    LayerInfo layerInfo = leafNode.getLayerInfo();
                    if (layerInfo.getLayerData(ViewLayerType.PROPBANK) != null){
                        if (layerInfo.getLayerData(ViewLayerType.PROPBANK).equals(previousAnnotation)){
                            phrase = phrase + " " + layerInfo.getLayerData(ViewLayerType.TURKISH_WORD);
                        } else {
                            if (phrase.length() > 0 && previousAnnotation.contains("$") && previousAnnotation.split("\\$").length > 1){
                                synSet = turkish.getSynSetWithId(previousAnnotation.split("\\$")[1]);
                                if (synSet != null){
                                    pw.println(treeBank.get(i).getFileDescription().getRawFileName() + "\t" + phrase + "\t" + previousAnnotation + "\t" + synSet.getSynonym() + "\t" + synSet.getDefinition() + "\t" + sentence);
                                }
                            }
                            if (!layerInfo.getLayerData(ViewLayerType.PROPBANK).equals("NONE")){
                                phrase = layerInfo.getLayerData(ViewLayerType.TURKISH_WORD);
                                previousAnnotation = layerInfo.getLayerData(ViewLayerType.PROPBANK);
                            } else {
                                phrase = "";
                                previousAnnotation = "";
                            }
                        }
                    } else {
                        if (phrase.length() > 0){
                            if (previousAnnotation.contains("$") && previousAnnotation.split("\\$").length > 1){
                                synSet = turkish.getSynSetWithId(previousAnnotation.split("\\$")[1]);
                                if (synSet != null){
                                    pw.println(treeBank.get(i).getFileDescription().getRawFileName() + "\t" + phrase + "\t" + previousAnnotation + "\t" + synSet.getSynonym() + "\t" + synSet.getDefinition() + "\t" + sentence);
                                }
                            }
                            phrase = "";
                            previousAnnotation = "";
                        }
                    }
                }
                if (phrase.length() > 0 && previousAnnotation.contains("$") && previousAnnotation.split("\\$").length > 1){
                    synSet = turkish.getSynSetWithId(previousAnnotation.split("\\$")[1]);
                    if (synSet != null){
                        pw.println(treeBank.get(i).getFileDescription().getRawFileName() + "\t" + phrase + "\t" + previousAnnotation + "\t" + synSet.getSynonym() + "\t" + synSet.getDefinition() + "\t" + sentence);
                    }
                }
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void englishSemanticVsPropbank(){
        PredicateList predicateList = new PredicateList();
        WordNet english = new WordNet("Data/Wordnet/english_wordnet_version_31.xml");
        ParallelTreeBankDrawable treeBank = null;
        for (int k = 0; k < 2; k++){
            switch (k){
                case 0:
                    treeBank = new ParallelTreeBankDrawable(new File("../Penn-Treebank/English"), new File("../Penn-Treebank/Turkish"));
                    break;
                case 1:
                    treeBank = new ParallelTreeBankDrawable(new File("../Penn-Treebank-20/English"), new File("../Penn-Treebank-20/Turkish"));
                    break;
            }
            for (int i = 0; i < treeBank.size(); i++){
                NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) treeBank.toTree(i).getRoot(), new IsEnglishLeafNode());
                ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
                for (ParseNodeDrawable leafNode : leafList){
                    if (leafNode.getLayerData(ViewLayerType.ENGLISH_PROPBANK) != null && leafNode.getLayerData(ViewLayerType.ENGLISH_SEMANTICS) != null){
                        for (int j = 0; j < leafNode.getLayerInfo().getLayerSize(ViewLayerType.ENGLISH_PROPBANK); j++){
                            try {
                                Argument argument = leafNode.getLayerInfo().getArgumentAt(j);
                                if (argument.getArgumentType().equalsIgnoreCase("PREDICATE")){
                                    SynSet synSet = english.getSynSetWithId(leafNode.getLayerData(ViewLayerType.ENGLISH_SEMANTICS));
                                    String argumentId = argument.getId();
                                    String lemma = argumentId.substring(0, argumentId.indexOf("_"));
                                    String roleId = argumentId.replaceAll("_", ".");
                                    Predicate predicate = predicateList.getPredicate(lemma);
                                    if (predicate != null && roleId != null){
                                        RoleSet roleSet = predicate.getRoleSet(roleId);
                                        if (roleSet != null){
                                            System.out.println(argumentId + "\t" + roleSet.getName() + "\t" + leafNode.getLayerData(ViewLayerType.ENGLISH_SEMANTICS) + "\t" + synSet.getDefinition() + "\t" + (k + 3) * 5 + "\t" + treeBank.toTree(i).getFileDescription().getRawFileName() + "\t" + treeBank.fromTree(i).toSentence());
                                        }
                                    }
                                }
                            } catch (LayerNotExistsException | WordNotExistsException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    public static void propbankVerbList(){
        TreeBankDrawable treeBank;
        WordNet turkish = new WordNet();
        treeBank = new TreeBankDrawable(new File("../Turkish"));
        try {
            FileWriter fw = new FileWriter(new File("verbs.txt"));
            for (int i = 0; i < treeBank.size(); i++){
                NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) treeBank.get(i).getRoot(), new IsVerbNode(turkish));
                ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
                for (ParseNodeDrawable leafNode : leafList){
                    if (leafNode.getLayerData(ViewLayerType.SEMANTICS) != null){
                        fw.write(leafNode.getLayerData(ViewLayerType.TURKISH_WORD) + "\n");
                    }
                }
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void synSetConversion(){
        TreeBankDrawable treeBank;
        WordNet turkish = new WordNet();
        IdMapping mapping = new IdMapping("Data/Wordnet/mapping.txt");
        treeBank = new TreeBankDrawable(new File("../Penn-Treebank/Turkish"));
        for (int i = 0; i < treeBank.size(); i++){
            boolean changed = false;
            NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) treeBank.get(i).getRoot(), new IsTurkishLeafNode());
            ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
            for (ParseNodeDrawable leafNode : leafList){
                if (leafNode.getLayerInfo().layerExists(ViewLayerType.SEMANTICS)){
                    for (int j = 0; j < leafNode.getLayerInfo().getNumberOfMeanings(); j++){
                        try {
                            String id = leafNode.getLayerInfo().getSemanticAt(j);
                            if (turkish.getSynSetWithId(id) == null){
                                String mappedId = mapping.map(id);
                                if (mappedId != null){
                                    leafNode.getLayerInfo().setLayerData(ViewLayerType.SEMANTICS, leafNode.getLayerData(ViewLayerType.SEMANTICS).replace(id, mappedId));
                                    changed = true;
                                } else {
                                    System.out.println(id + " does not exist");
                                }
                            }
                        } catch (LayerNotExistsException | WordNotExistsException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if (changed){
                treeBank.get(i).save();
            }
        }
    }

    public static void frameSetConversion(){
        WordNet turkish = new WordNet();
        IdMapping mapping = new IdMapping("Data/Wordnet/mapping.txt");
        FramesetList xmlParser = new FramesetList();
        for (int i = 0; i < xmlParser.size(); i++){
            Frameset frameset = xmlParser.getFrameSet(i);
            if (turkish.getSynSetWithId(frameset.getId()) == null){
                String mappedId = mapping.map(frameset.getId());
                if (mappedId != null){
                    frameset.setId(mappedId);
                    frameset.saveAsXml();
                }
            }
        }
    }

    public static void semanticNotExists(){
        TreeBankDrawable treeBank;
        Corpus c = new Corpus();
        treeBank = new TreeBankDrawable(new File("../Penn-Treebank/Turkish"));
        for (int i = 0; i < treeBank.size(); i++){
            NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) treeBank.get(i).getRoot(), new IsTurkishLeafNode());
            ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
            for (ParseNodeDrawable leafNode : leafList){
                if (leafNode.getLayerData(ViewLayerType.INFLECTIONAL_GROUP) != null && (leafNode.getLayerData(ViewLayerType.SEMANTICS) == null || leafNode.getLayerData(ViewLayerType.SEMANTICS).isEmpty())){
                    c.addSentence(new Sentence(leafNode.getLayerData(ViewLayerType.TURKISH_WORD)));
                }
            }
        }
        Set<Word> wordList = c.getWordList();
        for (Word word : wordList){
            System.out.println(word + " " + c.getCount(word));
        }
    }

    public static void semanticLabelNotExists(){
        TreeBankDrawable treeBank;
        WordNet turkish = new WordNet();
        treeBank = new TreeBankDrawable(new File("../Penn-Treebank/Turkish"));
        for (int i = 0; i < treeBank.size(); i++){
            ParseTreeDrawable parseTree = treeBank.get(i);
            NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
            ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
            for (ParseNodeDrawable leafNode : leafList){
                if (leafNode.getLayerData(ViewLayerType.SEMANTICS) != null){
                    LayerInfo info = leafNode.getLayerInfo();
                    for (int j = 0; j < info.getNumberOfMeanings(); j++){
                        try {
                            if (turkish.getSynSetWithId(info.getSemanticAt(j)) == null){
                                System.out.println(parseTree.getName() + "->" + leafNode.getLayerData(ViewLayerType.TURKISH_WORD));
                            }
                        } catch (LayerNotExistsException e) {
                            e.printStackTrace();
                        } catch (WordNotExistsException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static void semanticLabelSetNotExists(){
        TreeBankDrawable treeBank;
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer();
        WordNet turkish = new WordNet();
        treeBank = new TreeBankDrawable(new File("../Penn-Treebank/Turkish"));
        for (int i = 0; i < treeBank.size(); i++){
            NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) treeBank.get(i).getRoot(), new IsTurkishLeafNode());
            ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
            for (ParseNodeDrawable leafNode : leafList){
                if (leafNode.getLayerData(ViewLayerType.INFLECTIONAL_GROUP) != null){
                    LayerInfo info = leafNode.getLayerInfo();
                    try {
                        for (int j = 0; j < info.getNumberOfWords(); j++){
                            ArrayList<SynSet> synsets = turkish.constructSynSets(info.getMorphologicalParseAt(j).getWord().getName(), info.getMorphologicalParseAt(j), info.getMetamorphicParseAt(j), fsm);
                            if (synsets.size() == 0){
                                System.out.println(treeBank.get(i).getName() + "--->" + info.getTurkishWordAt(j));
                            }
                        }
                    } catch (LayerNotExistsException | WordNotExistsException e) {
                    }
                }
            }
        }
    }

    public static void semanticDoubleLeaves(TreeBankDrawable treeBank){
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer();
        WordNet turkish = new WordNet();
        for (int i = 0; i < treeBank.size(); i++){
            NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) treeBank.get(i).getRoot(), new IsTurkishLeafNode());
            ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
            for (int j = 0; j < leafList.size() - 1; j++){
                ParseNodeDrawable previous = leafList.get(j);
                ParseNodeDrawable current = leafList.get(j + 1);
                try {
                    if (previous.getLayerData(ViewLayerType.INFLECTIONAL_GROUP) != null && current.getLayerData(ViewLayerType.INFLECTIONAL_GROUP) != null && previous.getLayerInfo().getNumberOfWords() == 1 && current.getLayerInfo().getNumberOfWords() == 1 && (previous.getLayerData(ViewLayerType.SEMANTICS) == null || !previous.getLayerData(ViewLayerType.SEMANTICS).equals(current.getLayerData(ViewLayerType.SEMANTICS)))){
                        ArrayList<SynSet> synsets = turkish.constructIdiomSynSets(previous.getLayerInfo().getMorphologicalParseAt(0), current.getLayerInfo().getMorphologicalParseAt(0), previous.getLayerInfo().getMetamorphicParseAt(0), current.getLayerInfo().getMetamorphicParseAt(0), fsm);
                        if (synsets.size() > 0){
                            System.out.println(treeBank.get(i).getFileDescription().getRawFileName());
                        }
                    }
                } catch (LayerNotExistsException | WordNotExistsException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void semanticProperWords(TreeBankDrawable treeBank){
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer();
        WordNet turkish = new WordNet();
        for (int i = 0; i < treeBank.size(); i++){
            NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) treeBank.get(i).getRoot(), new IsTurkishLeafNode());
            ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
            for (ParseNodeDrawable leafNode : leafList){
                if (leafNode.getLayerData(ViewLayerType.INFLECTIONAL_GROUP) != null && leafNode.getLayerData(ViewLayerType.SEMANTICS) != null && leafNode.getLayerData(ViewLayerType.SEMANTICS).equals("TUR10-0000000") && leafNode.getLayerData(ViewLayerType.TURKISH_WORD).length() >= 3 && turkish.getSynSetsWithLiteral(leafNode.getLayerData(ViewLayerType.TURKISH_WORD)).size() > 0){
                    System.out.println(treeBank.get(i).getFileDescription().getRawFileName());
                }
            }
        }
    }

    public static void semanticDoubleWords(TreeBankDrawable treeBank){
        FsmMorphologicalAnalyzer fsm = new FsmMorphologicalAnalyzer();
        WordNet turkish = new WordNet();
        for (int i = 0; i < treeBank.size(); i++){
            NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) treeBank.get(i).getRoot(), new IsTurkishLeafNode());
            ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
            for (ParseNodeDrawable leafNode : leafList){
                if (leafNode.getLayerData(ViewLayerType.INFLECTIONAL_GROUP) != null && (leafNode.getLayerData(ViewLayerType.SEMANTICS) == null || leafNode.getLayerData(ViewLayerType.SEMANTICS).contains("$"))){
                    LayerInfo info = leafNode.getLayerInfo();
                    try {
                        if (info.getNumberOfWords() == 2){
                            ArrayList<SynSet> synsets = turkish.constructIdiomSynSets(info.getMorphologicalParseAt(0), info.getMorphologicalParseAt(1), info.getMetamorphicParseAt(0), info.getMetamorphicParseAt(1), fsm);
                            if (synsets.size() > 0){
                                System.out.println(treeBank.get(i).getFileDescription().getRawFileName());
                            }
                        }
                    } catch (LayerNotExistsException | WordNotExistsException e) {
                    }
                }
            }
        }
    }

    public static void createCorpus(LeafToLanguageConverter leafToLanguageConverter, String fileName){
        TreeBankDrawable treeBank;
        Corpus mainCorpus, corpus;
        treeBank = new TreeBankDrawable(new File("../Turkish"));
        System.out.println("15 read");
        mainCorpus = treeBank.createCorpus(leafToLanguageConverter);
        treeBank = new TreeBankDrawable(new File("../Turkish20"));
        System.out.println("20 read");
        corpus = treeBank.createCorpus(leafToLanguageConverter);
        mainCorpus.combine(corpus);
        treeBank = new TreeBankDrawable(new File("../Turkish25"));
        System.out.println("25 read");
        corpus = treeBank.createCorpus(leafToLanguageConverter);
        mainCorpus.combine(corpus);
        treeBank = new TreeBankDrawable(new File("../Turkish30"));
        System.out.println("30 read");
        corpus = treeBank.createCorpus(leafToLanguageConverter);
        mainCorpus.combine(corpus);
        treeBank = new TreeBankDrawable(new File("../Turkish35"));
        System.out.println("35 read");
        corpus = treeBank.createCorpus(leafToLanguageConverter);
        mainCorpus.combine(corpus);
        treeBank = new TreeBankDrawable(new File("../Turkish40"));
        System.out.println("40 read");
        corpus = treeBank.createCorpus(leafToLanguageConverter);
        mainCorpus.combine(corpus);
        treeBank = new TreeBankDrawable(new File("../Turkish45"));
        System.out.println("45 read");
        corpus = treeBank.createCorpus(leafToLanguageConverter);
        mainCorpus.combine(corpus);
        treeBank = new TreeBankDrawable(new File("../Turkish50"));
        System.out.println("50 read");
        corpus = treeBank.createCorpus(leafToLanguageConverter);
        mainCorpus.combine(corpus);
        mainCorpus.writeToFile(fileName);
    }

    public static void constructPCfg(){
        TreeBank treeBank = new TreeBank(new File(TreeBankDrawable.TURKISH_PARSE_PATH));
        ProbabilisticContextFreeGrammar pcfg = new ProbabilisticContextFreeGrammar(treeBank);
        pcfg.writeToFile("turkish-pcfg.txt");
    }

    public static void constructCfg(){
        TreeBank treeBank = new TreeBank(new File(TreeBankDrawable.TURKISH_PARSE_PATH));
        ContextFreeGrammar cfg = new ContextFreeGrammar(treeBank);
        cfg.writeToFile("turkish-cfg.txt");
    }

    public static void savePhrases(){
        File[] listOfFiles = new File("../Penn-Treebank-25/English").listFiles();
        Arrays.sort(listOfFiles);
        try {
            List<String> files = Files.readAllLines(new File("../25-turkish.txt").toPath(), Charset.forName("UTF-8"));
            int i = 0;
            for (File file : listOfFiles){
                PrintWriter pw = new PrintWriter(new File("../Penn-Treebank-25/Turkish-Phrase/" + file.getName()));
                pw.println(files.get(i));
                pw.close();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void convertToTurkishParseTree(){
        TreeBankDrawable treeBank = new TreeBankDrawable(new File(TreeBankDrawable.TURKISH_PATH));
        System.out.println("Treebank Read");
        treeBank.convertToTurkishParseTree();
    }

    public static void interAnnotatorAgreement(){
        int total = 0, count = 0;
        Interval interval = new Interval();
        interval.add(1, 500);
        TreeBankDrawable treeBank1 = new TreeBankDrawable("../Penn-Treebank/Turkish", "train", interval);
        TreeBankDrawable treeBank2 = new TreeBankDrawable("../Penn-Treebank-Parallel/Turkish", "train", interval);
        for (int i = 0; i < 500; i++){
            ParseTreeDrawable parseTree1 = treeBank1.get(i);
            ParseTreeDrawable parseTree2 = treeBank2.get(i);
            NodeDrawableCollector nodeDrawableCollector1 = new NodeDrawableCollector((ParseNodeDrawable) parseTree1.getRoot(), new IsTurkishLeafNode());
            ArrayList<ParseNodeDrawable> leafList1 = nodeDrawableCollector1.collect();
            NodeDrawableCollector nodeDrawableCollector2 = new NodeDrawableCollector((ParseNodeDrawable) parseTree2.getRoot(), new IsTurkishLeafNode());
            ArrayList<ParseNodeDrawable> leafList2 = nodeDrawableCollector2.collect();
            for (int j = 0; j < leafList1.size(); j++){
                if (leafList1.get(j).getLayerData(ViewLayerType.SEMANTICS) != null && leafList2.get(j).getLayerData(ViewLayerType.SEMANTICS) != null){
                    if (leafList1.get(j).getLayerData(ViewLayerType.SEMANTICS).equalsIgnoreCase(leafList2.get(j).getLayerData(ViewLayerType.SEMANTICS))){
                        count++;
                    }
                    total++;
                }
            }
        }
        System.out.println(count + " " + total);
    }

    public static void copyTurkish(File folder){
        DoneFileFilter doneFileFilter = new DoneFileFilter(folder.getPath() + "/", ViewLayerType.TURKISH_WORD);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            Arrays.sort(listOfFiles);
            for (File file : listOfFiles){
                if (doneFileFilter.accept(file)){
                    System.out.println(file.getName());
                }
            }
        }
    }

    public static void extractStatistics(){
        TreeBankDrawable treeBank = new TreeBankDrawable(new File("../../Penn-Treebank/Turkish"));
        RootWordStatistics rootWordStatistics = treeBank.extractRootWordStatistics(new FsmMorphologicalAnalyzer());
        rootWordStatistics.saveStatistics("rootwordstatistics.bin");
    }

    public static void extractDictionary(String pathName, String outputFileName){
        TreeBankDrawable treeBank = new TreeBankDrawable(new File(pathName));
        TxtDictionary dictionary = treeBank.createDictionary();
        dictionary.saveAsTxt(outputFileName);
    }

    public static void main(String[] args){
        TreeBankDrawable treeBank = new TreeBankDrawable("../../Penn-Treebank/Turkish", "test", 345, 345);
        /*interlingualMultipleCandidates(2);
        interlingualMultipleCandidates(3);
        interlingualCandidates(1);
        interlingualCandidates(2);
        interlingualCandidates(3);
        multiWordCandidates(2);
        multiWordCandidates(3);
        missingCandidates();
        propbankAnnotationControl();*/
        //extractDictionary("../../Penn-Treebank/Turkish", "deneme2.txt");
    }

}
