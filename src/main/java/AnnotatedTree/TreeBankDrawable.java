package AnnotatedTree;

import AnnotatedSentence.AnnotatedCorpus;
import AnnotatedSentence.AnnotatedSentence;
import AnnotatedSentence.LayerNotExistsException;
import AnnotatedSentence.ViewLayerType;
import ParseTree.ParseNode;
import ParseTree.ParseTree;
import ParseTree.TreeBank;
import DataStructure.CounterHashMap;
import MorphologicalAnalysis.MorphologicalParse;
import AnnotatedTree.Processor.Condition.IsTurkishLeafNode;
import AnnotatedTree.Processor.ConvertToTurkishParseTree;
import AnnotatedTree.Processor.NodeDrawableCollector;
import Util.Interval;
import WordNet.WordNet;
import Corpus.*;
import Dictionary.*;

import java.io.*;
import java.util.*;

public class TreeBankDrawable extends TreeBank {
    static final public String ENGLISH_PATH = "../English/";
    static final public String TURKISH_PATH = "../Turkish/";
    static final public String TURKISH_PARSE_PATH = "../Turkish-Parse/";
    static final public String TURKISH_PHRASE_PATH = "../Turkish-Phrase/";
    static final public String TREE_IMAGES = "../Tree-Images/";

    public TreeBankDrawable(ArrayList<ParseTree> parseTrees){
        this.parseTrees = parseTrees;
    }

    /**
     * A constructor of {@link TreeBankDrawable} class which reads all {@link ParseTreeDrawable} files inside the given
     * folder. For each file inside that folder, the constructor creates a ParseTreeDrawable and puts in inside the list
     * parseTrees.
     * @param folder Folder where all parseTrees reside.
     */
    public TreeBankDrawable(File folder){
        parseTrees = new ArrayList<>();
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null){
            Arrays.sort(listOfFiles);
            for (File file:listOfFiles){
                if (file.isDirectory()){
                    continue;
                }
                try {
                    ParseTreeDrawable parseTree = new ParseTreeDrawable(new FileInputStream(file.getAbsolutePath()));
                    if (parseTree.getRoot() != null){
                        parseTree.setName(file.getName());
                        parseTree.setFileDescription(new FileDescription(file.getParent(), file.getName()));
                        parseTrees.add(parseTree);
                    } else {
                        System.out.println("Parse Tree " + file.getName() + " can not be read");
                    }
                } catch (FileNotFoundException ignored) {
                }
            }
        }
    }

    /**
     * A constructor of {@link TreeBankDrawable} class which reads all {@link ParseTreeDrawable} files with the file
     * name satisfying the given pattern inside the given folder. For each file inside that folder, the constructor
     * creates a ParseTreeDrawable and puts in inside the list parseTrees.
     * @param folder Folder where all parseTrees reside.
     * @param pattern File pattern such as "." ".train" ".test".
     */
    public TreeBankDrawable(File folder, String pattern){
        parseTrees = new ArrayList<>();
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null){
            Arrays.sort(listOfFiles);
            for (File file:listOfFiles){
                if (file.isDirectory()){
                    continue;
                }
                String fileName = file.getName();
                if (!fileName.contains(pattern))
                    continue;
                try {
                    ParseTreeDrawable parseTree = new ParseTreeDrawable(new FileInputStream(file.getAbsolutePath()));
                    if (parseTree.getRoot() != null){
                        parseTree.setName(file.getName());
                        parseTree.setFileDescription(new FileDescription(file.getParent(), file.getName()));
                        parseTrees.add(parseTree);
                    } else {
                        System.out.println("Parse Tree " + file.getName() + " can not be read");
                    }
                } catch (FileNotFoundException ignored) {
                }
            }
        }
    }

    /**
     * A constructor of {@link TreeBankDrawable} class which reads the files numbered in the given interval with the
     * file name having thr given extension inside the given folder. For each file inside that folder, the constructor
     * creates a ParseTreeDrawable and puts in inside the list parseTrees.
     * @param path Folder where all parseTrees reside.
     * @param extension File pattern such as "train" "test".
     * @param interval Starting  and ending index for the ParseTrees read.
     */
    public TreeBankDrawable(String path, String extension, Interval interval){
        parseTrees = new ArrayList<>();
        for (int i = 0; i < interval.size(); i++){
            for (int j = interval.getFirst(i); j <= interval.getLast(i); j++){
                ParseTreeDrawable parseTree = new ParseTreeDrawable(new FileDescription(path, extension, j));
                if (parseTree.getRoot() != null){
                    parseTrees.add(parseTree);
                }
            }
        }
    }

    /**
     * A constructor of {@link TreeBankDrawable} class which reads the files numbered from from to to with the file name
     * having thr given extension inside the given folder. For each file inside that folder, the constructor
     * creates a ParseTreeDrawable and puts in inside the list parseTrees.
     * @param path Folder where all parseTrees reside.
     * @param extension File pattern such as "train" "test".
     * @param from Starting index for the ParseTrees read.
     * @param to Ending index for the ParseTrees read.
     */
    public TreeBankDrawable(String path, String extension, int from, int to){
        parseTrees = new ArrayList<>();
        for (int i = from; i <= to; i++){
            ParseTreeDrawable parseTree = new ParseTreeDrawable(new FileDescription(path, extension, i));
            if (parseTree.getRoot() != null){
                parseTrees.add(parseTree);
            }
        }
    }

    /**
     * A constructor of {@link TreeBankDrawable} class which reads the files numbered from from to to with the file name
     * satisfying the given pattern inside the given folder. For each file inside that folder, the constructor
     * creates a ParseTreeDrawable and puts in inside the list parseTrees.
     * @param folder Folder where all parseTrees reside.
     * @param pattern File pattern such as "." ".train" ".test".
     * @param from Starting index for the ParseTrees read.
     * @param to Ending index for the ParseTrees read.
     */
    public TreeBankDrawable(File folder, String pattern, int from, int to){
        parseTrees = new ArrayList<>();
        for (int i = from; i <= to; i++){
            try {
                ParseTreeDrawable parseTree = new ParseTreeDrawable(new FileInputStream(folder.getAbsolutePath() + "/" + String.format("%04d", i) + pattern));
                if (parseTree.getRoot() != null){
                    parseTrees.add(parseTree);
                } else {
                    System.out.println("Parse Tree " + String.format("%04d", i) + pattern + " can not be read");
                }
            } catch (FileNotFoundException ignored) {
            }
        }
    }

    /**
     * Accessor for the parseTrees attribute
     * @return ParseTrees attribute
     */
    public List<ParseTree> getParseTrees(){
        return parseTrees;
    }

    /**
     * Accessor for a specific tree with the given position in the array.
     * @param index Index of the parseTree.
     * @return Tree that is in the position index
     */
    public ParseTreeDrawable get(int index){
        return (ParseTreeDrawable) parseTrees.get(index);
    }

    /**
     * Accessor for a specific tree with the given file name.
     * @param fileName File name of the tree
     * @return Tree that has the given file name
     */
    public ParseTreeDrawable get(String fileName){
        for (ParseTree tree : parseTrees){
            if (((ParseTreeDrawable) tree).getFileDescription().getRawFileName().equals(fileName)){
                return (ParseTreeDrawable) tree;
            }
        }
        return null;
    }

    public void convertToTurkishParseTree(){
        for (ParseTree tree:parseTrees){
            ParseTreeDrawable parseTree = (ParseTreeDrawable) tree;
            if (!parseTree.layerExists(ViewLayerType.META_MORPHEME)){
                continue;
            }
            if (!parseTree.layerAll(ViewLayerType.TURKISH_WORD)){
                continue;
            }
            ConvertToTurkishParseTree convertToTurkishParseTree = new ConvertToTurkishParseTree(parseTree);
            convertToTurkishParseTree.convert();
            parseTree.save(TreeBankDrawable.TURKISH_PARSE_PATH + parseTree.getName());
        }
    }

    public AnnotatedCorpus createAnnotatedCorpus(){
        AnnotatedCorpus corpus = new AnnotatedCorpus();
        for (ParseTree tree:parseTrees){
            ParseTreeDrawable parseTree = (ParseTreeDrawable) tree;
            AnnotatedSentence sentence = parseTree.generateAnnotatedSentence();
            corpus.addSentence(sentence);
        }
        return corpus;
    }

    /**
     * Create a TxtDictionary from the root nodes of the morphological parses of the words in the trees.
     * @return TxtDictionary from the root nodes of the morphological parses of the words in the trees.
     */
    public TxtDictionary createDictionary() {
        TxtDictionary dictionary = new TxtDictionary(new TurkishWordComparator());
        for (ParseTree tree:parseTrees){
            ParseTreeDrawable parseTree = (ParseTreeDrawable) tree;
            NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
            ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
            for (ParseNode node : leafList){
                ParseNodeDrawable leafNode = (ParseNodeDrawable) node;
                LayerInfo layerInfo = leafNode.getLayerInfo();
                if (layerInfo.layerExists(ViewLayerType.INFLECTIONAL_GROUP)){
                    try{
                        for (int i = 0; i < layerInfo.getNumberOfWords(); i++){
                            MorphologicalParse morphologicalParse = layerInfo.getMorphologicalParseAt(i);
                            String pos = morphologicalParse.getRootPos();
                            String name = morphologicalParse.getWord().getName();
                            switch (pos){
                                case "NOUN":
                                    if (morphologicalParse.isProperNoun()){
                                        dictionary.addProperNoun(name);
                                    } else {
                                        dictionary.addNoun(name);
                                    }
                                    break;
                                case "VERB":
                                    dictionary.addVerb(name);
                                    break;
                                case "ADJ":
                                    dictionary.addAdjective(name);
                                    break;
                                case "ADV":
                                    dictionary.addAdverb(name);
                                    break;
                            }
                        }
                    } catch (LayerNotExistsException | WordNotExistsException ignored){
                    }
                }
            }
        }
        return dictionary;
    }

    /**
     * Returns list of trees that contain at least one verb
     * @param wordNet Wordnet used for checking the pos tag of the synset.
     * @return List of trees that contai at least one verb.
     */
    public ArrayList<ParseNodeDrawable> extractVerbs(WordNet wordNet){
        ArrayList<ParseNodeDrawable> nodeList = new ArrayList<>();
        for (ParseTree tree:parseTrees){
            ParseTreeDrawable parseTree = (ParseTreeDrawable) tree;
            nodeList.addAll(parseTree.extractNodesWithVerbs(wordNet));
        }
        return nodeList;
    }

    public HashMap<String, Integer> extractRootWordCounts(){
        CounterHashMap<String> counts = new CounterHashMap<>();
        for (ParseTree tree:parseTrees){
            ParseTreeDrawable parseTree = (ParseTreeDrawable) tree;
            NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
            ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
            for (ParseNode node : leafList){
                ParseNodeDrawable leafNode = (ParseNodeDrawable) node;
                try {
                    for (int i = 0; i < leafNode.getLayerInfo().getNumberOfWords(); i++){
                        MorphologicalParse parse = leafNode.getLayerInfo().getMorphologicalParseAt(i);
                        counts.put(parse.getWord().getName());
                    }
                } catch (LayerNotExistsException | WordNotExistsException ignored) {
                }
            }
        }
        return counts;
    }

    /**
     * Clears the given layer for all nodes in all trees
     * @param layerType Layer name
     */
    public void clearLayer(ViewLayerType layerType){
        for (ParseTree tree:parseTrees){
            ParseTreeDrawable parseTree = (ParseTreeDrawable) tree;
            parseTree.clearLayer(layerType);
            parseTree.save();
        }
    }

    /**
     * Returns list of trees that contain at least one verb which is annotated as 'PREDICATE'.
     * @param wordNet Wordnet used for checking the pos tag of the synset.
     * @return List of trees that contain at least one verb which is annotated as 'PREDICATE'.
     */
    public ArrayList<ParseTreeDrawable> extractTreesWithPredicates(WordNet wordNet){
        ArrayList<ParseTreeDrawable> treeList = new ArrayList<>();
        for (ParseTree tree:parseTrees){
            ParseTreeDrawable parseTree = (ParseTreeDrawable) tree;
            if (!parseTree.extractNodesWithPredicateVerbs(wordNet).isEmpty()){
                treeList.add(parseTree);
            }
        }
        return treeList;
    }

    /**
     * Sorts the tres in the treebanks according to their filenames.
     */
    public void sort(){
        parseTrees.sort(new ParseTreeComparator());
    }

}
