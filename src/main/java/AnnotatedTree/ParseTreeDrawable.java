package AnnotatedTree;

import AnnotatedSentence.*;
import ParseTree.ParseNode;
import ParseTree.ParseTree;
import ParseTree.Symbol;
import Corpus.FileDescription;
import Translation.AutomaticTranslationDictionary;
import Dictionary.EnglishWordComparator;
import Dictionary.Word;
import AnnotatedTree.Processor.Condition.*;
import AnnotatedTree.Processor.NodeDrawableCollector;
import AnnotatedTree.ReorderMap.ReorderMap;
import Translation.ScoredSentence;
import WordNet.WordNet;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class ParseTreeDrawable extends ParseTree {
    private FileDescription fileDescription;
    private int maxInOrderTraversalIndex;
    private String name;

    public ParseTreeDrawable(String path, String rawFileName){
        this(new FileDescription(path, rawFileName));
    }

    public ParseTreeDrawable(String path, String extension, int index){
        this(new FileDescription(path, extension, index));
    }

    public ParseTreeDrawable(String path, FileDescription fileDescription){
        this(new FileDescription(path, fileDescription.getExtension(), fileDescription.getIndex()));
    }

    public ParseTreeDrawable(FileDescription fileDescription){
        this.fileDescription = fileDescription;
        readFromFile(fileDescription.getPath());
    }

    public void setFileDescription(FileDescription fileDescription){
        this.fileDescription = fileDescription;
    }

    public FileDescription getFileDescription(){
        return fileDescription;
    }

    public void copyInfo(ParseTreeDrawable parseTree){
        this.fileDescription = parseTree.fileDescription;
    }

    public void reload(){
        readFromFile(fileDescription.getPath());
    }

    public void setRoot(ParseNode newRootNode){
        root = newRootNode;
    }

    private void readFromFile(String currentPath){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileDescription.getFileName(currentPath)), StandardCharsets.UTF_8));
            String line = br.readLine();
            if (line.contains("(") && line.contains(")")){
                line = line.substring(line.indexOf("(") + 1, line.lastIndexOf(")")).trim();
                root = new ParseNodeDrawable(null, line, false, 0);
                updateTraversalIndexes();
            } else {
                System.out.println("File " + fileDescription.getFileName(currentPath) + " is not a valid parse tree file");
                root = null;
            }
            br.close();
        } catch (IOException e) {
            root = null;
        } catch (ParenthesisInLayerException e) {
            System.out.println(e.toString() + " in file " + fileDescription.getFileName(currentPath));
            root = null;
        }
    }

    public ParseTreeDrawable(FileInputStream file){
        try {
            name = file.getFD().toString();
            BufferedReader br = new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8));
            String line = br.readLine();
            if (line != null && line.contains("(") && line.contains(")")){
                line = line.substring(line.indexOf("(") + 1, line.lastIndexOf(")")).trim();
                root = new ParseNodeDrawable(null, line, false, 0);
                updateTraversalIndexes();
            } else {
                root = null;
            }
            br.close();
        } catch (IOException e) {
            root = null;
        } catch (ParenthesisInLayerException e) {
            System.out.println(e.toString());
            root = null;
        }
    }

    private void updateTraversalIndexes(){
        ((ParseNodeDrawable)root).inOrderTraversal(0);
        ((ParseNodeDrawable)root).leafTraversal(0);
        maxInOrderTraversalIndex = ((ParseNodeDrawable) root).maxInOrderTraversal();
    }

    public int getMaxInOrderTraversalIndex(){
        return maxInOrderTraversalIndex;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void nextTree(int count){
        if (fileDescription.nextFileExists(count)){
            fileDescription.addToIndex(count);
            reload();
        }
    }

    public void previousTree(int count){
        if (fileDescription.previousFileExists(count)){
            fileDescription.addToIndex(-count);
            reload();
        }
    }

    public void save(){
        BufferedWriter fw;
        try {
            fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDescription.getFileName()), "UTF-8"));
            fw.write("( " + this.toString() + " )\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String toSvg(ViewLayerType viewLayer) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\">"
                + ((ParseNodeDrawable)root).toSvgFormat(viewLayer) + "</svg>";
    }

    public void saveAsSvg(ViewLayerType viewLayer){
        BufferedWriter fw;
        try {
            fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDescription.getFileName(TreeBankDrawable.TREE_IMAGES) + ".svg"), "UTF-8"));
            fw.write(this.toSvg(viewLayer));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveWithPath(String newPath){
        BufferedWriter fw;
        try {
            fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileDescription.getFileName(newPath)), "UTF-8"));
            fw.write("( " + this.toString() + " )\n");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int glossAgreementCount(ParseTree parseTree, ViewLayerType viewLayerType){
        return ((ParseNodeDrawable)root).glossAgreementCount((ParseNodeDrawable) parseTree.getRoot(), viewLayerType);
    }

    public int structureAgreementCount(ParseTree parseTree){
        return ((ParseNodeDrawable)root).structureAgreementCount((ParseNodeDrawable)parseTree.getRoot());
    }

    public ArrayList<ParseNodeDrawable> satisfy(ParseTreeSearchable tree){
        return ((ParseNodeDrawable)root).satisfy(tree);
    }

    public void updatePosTags(){
        ((ParseNodeDrawable)root).updatePosTags();
    }

    public void addReorder(ParseTreeDrawable toTree, ReorderMap reorderMap){
        ((ParseNodeDrawable)root).addReorder((ParseNodeDrawable)toTree.root, reorderMap);
    }

    public int score(ParseTreeDrawable correctTree){
        return ((ParseNodeDrawable)root).score((ParseNodeDrawable)correctTree.root);
    }

    public boolean isPermutation(ParseTreeDrawable tree){
        return ((ParseNodeDrawable)root).isPermutation((ParseNodeDrawable)tree.root);
    }

    public int maxDepth(){
        return ((ParseNodeDrawable) root).maxDepth();
    }

    public void moveLeft(ParseNode node){
        if (root != node){
            root.moveLeft(node);
            updateTraversalIndexes();
        }
    }

    public void moveRight(ParseNode node){
        if (root != node){
            root.moveRight(node);
            updateTraversalIndexes();
        }
    }

    public void divideIntoWords(ParseNodeDrawable parseNode){
        try {
            ArrayList<LayerInfo> layers = parseNode.getLayerInfo().divideIntoWords();
            parseNode.getParent().removeChild(parseNode);
            for (LayerInfo layerInfo : layers){
                Symbol symbol;
                if (layerInfo.layerExists(ViewLayerType.INFLECTIONAL_GROUP)){
                    symbol = new Symbol(layerInfo.getMorphologicalParseAt(0).getTreePos());
                } else {
                    symbol = new Symbol("-XXX-");
                }
                ParseNodeDrawable child = new ParseNodeDrawable(symbol);
                parseNode.getParent().addChild(child);
                ParseNodeDrawable grandChild = new ParseNodeDrawable(child, layerInfo.getLayerDescription(), true, parseNode.getDepth() + 1);
                child.addChild(grandChild);
                updateTraversalIndexes();
                ((ParseNodeDrawable) root).updateDepths(0);
            }
        } catch (LayerNotExistsException | ParenthesisInLayerException | WordNotExistsException e) {
            e.printStackTrace();
        }
    }

    public HashMap<ParseNode, ParseNodeDrawable> mapTree(ParseTreeDrawable parseTree){
        HashMap<ParseNode, ParseNodeDrawable> nodeMap = new HashMap<>();
        ((ParseNodeDrawable)root).mapTree((ParseNodeDrawable) parseTree.getRoot(), nodeMap);
        return nodeMap;
    }

    public void moveNode(ParseNode fromNode, ParseNode toNode){
        if (root != fromNode){
            ParseNode parent = fromNode.getParent();
            parent.removeChild(fromNode);
            toNode.addChild(fromNode);
            updateTraversalIndexes();
            ((ParseNodeDrawable) root).updateDepths(0);
        }
    }

    public void moveNode(ParseNode fromNode, ParseNode toNode, int childIndex){
        if (root != fromNode){
            ParseNode parent = fromNode.getParent();
            parent.removeChild(fromNode);
            toNode.addChild(childIndex, fromNode);
            updateTraversalIndexes();
            ((ParseNodeDrawable) root).updateDepths(0);
        }
    }

    public void combineWords(ParseNodeDrawable parent, ParseNodeDrawable child){
        while (parent.numberOfChildren() > 0){
            parent.removeChild(parent.firstChild());
        }
        parent.addChild(child);
        updateTraversalIndexes();
        ((ParseNodeDrawable) root).updateDepths(0);
    }

    public boolean layerExists(ViewLayerType viewLayerType){
        return ((ParseNodeDrawable)(root)).layerExists(viewLayerType);
    }

    public boolean layerAll(ViewLayerType viewLayerType){
        return ((ParseNodeDrawable)(root)).layerAll(viewLayerType);
    }

    public AutomaticTranslationDictionary translate(ViewLayerType fromLayer, ViewLayerType toLayer){
        AutomaticTranslationDictionary dictionary = new AutomaticTranslationDictionary(new EnglishWordComparator());
        addTranslations(dictionary, fromLayer, toLayer);
        return dictionary;
    }

    public void addTranslations(AutomaticTranslationDictionary dictionary, ViewLayerType fromLayer, ViewLayerType toLayer){
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable)this.getRoot(), new IsLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable leafNode: leafList){
            if (leafNode.getLayerData(fromLayer) != null && leafNode.getLayerData(toLayer) != null){
                if (leafNode.getLayerData(fromLayer).equals("*NONE*"))
                    dictionary.addWord(new Word(leafNode.getLayerInfo().getRobustLayerData(fromLayer)), new Word(leafNode.getLayerInfo().getRobustLayerData(toLayer)));
                else
                    dictionary.addWord(new Word(leafNode.getLayerInfo().getRobustLayerData(fromLayer).toLowerCase()), new Word(leafNode.getLayerInfo().getRobustLayerData(toLayer)));
            }
        }
    }

    public boolean updateConnectedPredicate(String previousId, String currentId){
        boolean modified = false;
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable)root, new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            if (parseNode.getLayerInfo().getArgument() != null && parseNode.getLayerInfo().getArgument().getId() != null && parseNode.getLayerInfo().getArgument().getId().equals(previousId)){
                parseNode.getLayerInfo().setLayerData(ViewLayerType.PROPBANK, parseNode.getLayerInfo().getArgument().getArgumentType() + "$" + currentId);
                modified = true;
            }
        }
        return modified;
    }

    public ParseNodeDrawable nextLeafNode(ParseNodeDrawable parseNode){
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable)root, new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (int i = 0; i < leafList.size() - 1; i++){
            if (leafList.get(i).equals(parseNode)){
                return leafList.get(i + 1);
            }
        }
        return null;
    }

    public ParseNodeDrawable previousLeafNode(ParseNodeDrawable parseNode){
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable)root, new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (int i = 1; i < leafList.size(); i++){
            if (leafList.get(i).equals(parseNode)){
                return leafList.get(i - 1);
            }
        }
        return null;
    }

    public void setShallowParseLayer(ChunkType chunkType){
        if (root != null){
            ((ParseNodeDrawable)root).setShallowParseLayer(chunkType);
        }
    }

    public void clearLayer(ViewLayerType layerType){
        if (root != null){
            ((ParseNodeDrawable)root).clearLayer(layerType);
        }
    }

    public AnnotatedSentence generateAnnotatedSentence(){
        AnnotatedSentence sentence = new AnnotatedSentence();
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable)root, new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            LayerInfo layers = parseNode.getLayerInfo();
            try {
                for (int i = 0; i < layers.getNumberOfWords(); i++){
                    sentence.addWord(layers.toAnnotatedWord(i));
                }
            } catch (LayerNotExistsException e) {
                e.printStackTrace();
            }
        }
        return sentence;
    }

    public ArrayList<ParseNodeDrawable> extractNodesWithVerbs(WordNet wordNet){
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) root, new IsVerbNode(wordNet));
        return nodeDrawableCollector.collect();
    }

    public ArrayList<ParseNodeDrawable> extractNodesWithPredicateVerbs(WordNet wordNet){
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) root, new IsPredicateVerbNode(wordNet));
        return nodeDrawableCollector.collect();
    }

    public void extractVerbal(){
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) root, new IsVPNode());
        ArrayList<ParseNodeDrawable> nodeList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable node:nodeList){
            if (node.extractVerbal()){
                return;
            }
        }
    }

    public int getSubItemAt(int x, int y){
        return ((ParseNodeDrawable)root).getSubItemAt(x, y);
    }

    public ParseNodeDrawable getLeafNodeAt(int x, int y){
        return ((ParseNodeDrawable)root).getLeafNodeAt(x, y);
    }

    public ParseNodeDrawable getNodeAt(int x, int y){
        return ((ParseNodeDrawable)root).getNodeAt(x, y);
    }

    public ParseNodeDrawable getLeafWithIndex(int index){
        return ((ParseNodeDrawable)root).getLeafWithIndex(index);
    }

    public void paint(Graphics g, int nodeWidth, int nodeHeight, ViewLayerType viewLayer){
        ((ParseNodeDrawable)root).paint(g, nodeWidth, nodeHeight, maxDepth(), viewLayer);
        if (viewLayer == ViewLayerType.INFLECTIONAL_GROUP){
            ((ParseNodeDrawable)root).drawDependency(g, this);
        }
    }

    public ArrayList<ScoredSentence> allPermutations(ReorderMap reorderMap){
        if (root != null){
            return ((ParseNodeDrawable)root).allPermutations(reorderMap);
        } else {
            return null;
        }
    }

    public void mlTranslate(ReorderMap reorderMap){
        ((ParseNodeDrawable)root).mlTranslate(reorderMap);
    }

}
