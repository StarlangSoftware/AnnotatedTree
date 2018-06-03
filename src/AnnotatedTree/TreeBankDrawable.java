package AnnotatedTree;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedSentence.ViewLayerType;
import ParseTree.ParseNode;
import ParseTree.ParseTree;
import ParseTree.TreeBank;
import DataStructure.CounterHashMap;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import MorphologicalAnalysis.FsmParseList;
import MorphologicalDisambiguation.RootWordStatistics;
import Translation.AutomaticTranslationDictionary;
import MorphologicalAnalysis.MorphologicalParse;
import AnnotatedTree.Processor.Condition.IsTurkishLeafNode;
import AnnotatedTree.Processor.ConvertToTurkishParseTree;
import AnnotatedTree.Processor.LeafConverter.LeafToLanguageConverter;
import AnnotatedTree.Processor.NodeDrawableCollector;
import AnnotatedTree.Processor.TreeToStringConverter;
import Util.Interval;
import WordNet.WordNet;
import Corpus.*;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class TreeBankDrawable extends TreeBank {
    private int readCount;
    private JProgressBar progressBar;
    static final public String ENGLISH_PATH = "../English/";
    static final public String TURKISH_PATH = "../Turkish/";
    static final public String TURKISH_PARSE_PATH = "../Turkish-Parse/";
    static final public String TURKISH_PHRASE_PATH = "../Turkish-Phrase/";
    static final public String TREE_IMAGES = "../Tree-Images/";

    public TreeBankDrawable(ArrayList<ParseTree> parseTrees){
        this.parseTrees = parseTrees;
    }

    private class ReadTree extends SwingWorker {
        private File file;

        public ReadTree(File file){
            this.file = file;
        }

        protected Object doInBackground() throws Exception {
            try {
                ParseTreeDrawable parseTree = new ParseTreeDrawable(new FileInputStream(file.getAbsolutePath()));
                if (parseTree.getRoot() != null){
                    parseTree.setName(file.getName());
                    parseTree.setFileDescription(new FileDescription(file.getParent(), file.getName()));
                    parseTrees.add(parseTree);
                } else {
                    System.out.println("Parse Tree " + file.getName() + " can not be read");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void done() {
            readCount++;
            progressBar.setValue(readCount);
            if (progressBar.getValue() == progressBar.getMaximum()){
                progressBar.setVisible(false);
            }
        }

    }

    public TreeBankDrawable(File folder, final JProgressBar progressBar){
        parseTrees = Collections.synchronizedList(new ArrayList<>());
        this.progressBar = progressBar;
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            Arrays.sort(listOfFiles);
            readCount = 0;
            progressBar.setMaximum(listOfFiles.length);
            for (File file:listOfFiles){
                ReadTree task = new ReadTree(file);
                task.execute();
            }
        }
    }

    public TreeBankDrawable(File folder){
        parseTrees = new ArrayList<>();
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null){
            Arrays.sort(listOfFiles);
            for (File file:listOfFiles){
                try {
                    ParseTreeDrawable parseTree = new ParseTreeDrawable(new FileInputStream(file.getAbsolutePath()));
                    if (parseTree.getRoot() != null){
                        parseTree.setName(file.getName());
                        parseTree.setFileDescription(new FileDescription(file.getParent(), file.getName()));
                        parseTrees.add(parseTree);
                    } else {
                        System.out.println("Parse Tree " + file.getName() + " can not be read");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public TreeBankDrawable(File folder, String pattern){
        parseTrees = new ArrayList<>();
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null){
            Arrays.sort(listOfFiles);
            for (File file:listOfFiles){
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
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

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

    public TreeBankDrawable(String path, String extension, int from, int to){
        parseTrees = new ArrayList<>();
        for (int i = from; i <= to; i++){
            ParseTreeDrawable parseTree = new ParseTreeDrawable(new FileDescription(path, extension, i));
            if (parseTree.getRoot() != null){
                parseTrees.add(parseTree);
            }
        }
    }

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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public TreeBankDrawable(String fileName){
        String line, treeLine;
        int parenthesisCount = 0;
        parseTrees = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF8"));
            line = br.readLine();
            treeLine = "";
            while (line != null){
                if (!line.isEmpty()){
                    for (int i = 0; i < line.length(); i++){
                        if (line.charAt(i) == '('){
                            parenthesisCount++;
                        } else {
                            if (line.charAt(i) == ')'){
                                parenthesisCount--;
                            }
                        }
                    }
                    treeLine = treeLine + line;
                    if (parenthesisCount == 0){
                        ParseTreeDrawable tree = new ParseTreeDrawable(treeLine);
                        if (tree.getRoot() != null){
                            parseTrees.add(tree);
                        }
                        treeLine = "";
                    }
                }
                line = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ParseTree> getParseTrees(){
        return parseTrees;
    }

    public ParseTreeDrawable get(int index){
        return (ParseTreeDrawable) parseTrees.get(index);
    }

    public ParseTreeDrawable get(String fileName){
        for (ParseTree tree : parseTrees){
            if (((ParseTreeDrawable) tree).getFileDescription().getRawFileName().equals(fileName)){
                return (ParseTreeDrawable) tree;
            }
        }
        return null;
    }

    public void prepareTranslationDictionary(ViewLayerType fromLayer, ViewLayerType toLayer, String fileName){
        boolean firstTree = true;
        AutomaticTranslationDictionary dictionary = null, tmpDictionary;
        for (ParseTree tree:parseTrees){
            ParseTreeDrawable parseTree = (ParseTreeDrawable) tree;
            tmpDictionary = parseTree.translate(fromLayer, toLayer);
            if (firstTree){
                dictionary = tmpDictionary;
                firstTree = false;
            } else {
                if (dictionary != null) {
                    dictionary.mergeWith(tmpDictionary);
                }
            }
        }
        if (dictionary != null) {
            dictionary.saveAsXml(fileName);
        }
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
            try {
                convertToTurkishParseTree.convert();
            } catch (ParenthesisInLayerException e) {
                e.printStackTrace();
            }
            parseTree.save(TreeBankDrawable.TURKISH_PARSE_PATH + parseTree.getName());
        }
    }

    public Corpus createCorpus(LeafToLanguageConverter leafToLanguageConverter){
        Corpus corpus = new Corpus();
        for (ParseTree tree:parseTrees){
            ParseTreeDrawable parseTree = (ParseTreeDrawable) tree;
            TreeToStringConverter treeToStringConverter = new TreeToStringConverter(parseTree, leafToLanguageConverter);
            String sentence = treeToStringConverter.convert();
            if (!sentence.isEmpty()){
                corpus.addSentence(new Sentence(sentence));
            } else {
                System.out.println("Parse Tree " + parseTree.getName() + " is not translated");
            }
        }
        return corpus;
    }

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
                } catch (LayerNotExistsException | WordNotExistsException e) {
                    e.printStackTrace();
                }
            }
        }
        return counts;
    }

    public void clearLayer(ViewLayerType layerType){
        for (ParseTree tree:parseTrees){
            ParseTreeDrawable parseTree = (ParseTreeDrawable) tree;
            parseTree.clearLayer(layerType);
            parseTree.save();
        }
    }

    public ArrayList<ParseTreeDrawable> extractTreesWithPredicates(WordNet wordNet){
        ArrayList<ParseTreeDrawable> treeList = new ArrayList<>();
        for (ParseTree tree:parseTrees){
            ParseTreeDrawable parseTree = (ParseTreeDrawable) tree;
            if (parseTree.extractNodesWithPredicateVerbs(wordNet).size() > 0){
                treeList.add(parseTree);
            }
        }
        return treeList;
    }

    public void removeTree(int index){
        parseTrees.remove(index);
    }

    public void sort(){
        Collections.sort(parseTrees, new ParseTreeComparator());
    }

    public RootWordStatistics extractRootWordStatistics(FsmMorphologicalAnalyzer fsm){
        RootWordStatistics statistics = new RootWordStatistics();
        CounterHashMap<String> rootWordStatistics;
        for (ParseTree tree:getParseTrees()){
            ParseTreeDrawable parseTree = (ParseTreeDrawable) tree;
            NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
            ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
            for (ParseNode node : leafList){
                ParseNodeDrawable leafNode = (ParseNodeDrawable) node;
                try {
                    for (int i = 0; i < leafNode.getLayerInfo().getNumberOfWords(); i++){
                        FsmParseList parseList = fsm.morphologicalAnalysis(leafNode.getLayerInfo().getTurkishWordAt(i));
                        if (parseList.size() > 0){
                            String rootWords = parseList.rootWords();
                            if (rootWords.contains("$")){
                                if (!statistics.containsKey(rootWords)){
                                    rootWordStatistics = new CounterHashMap<>();
                                } else {
                                    rootWordStatistics = statistics.get(rootWords);
                                }
                                MorphologicalParse parse = leafNode.getLayerInfo().getMorphologicalParseAt(i);
                                rootWordStatistics.put(parse.getWord().getName());
                                statistics.put(rootWords, rootWordStatistics);
                            }
                        }
                    }
                } catch (LayerNotExistsException | WordNotExistsException e) {
                    e.printStackTrace();
                }
            }
        }
        return statistics;
    }

}
