package AnnotatedTree;

import AnnotatedSentence.*;
import ParseTree.ParseNode;
import ParseTree.ParseTree;
import ParseTree.Symbol;
import Corpus.FileDescription;
import AnnotatedTree.Processor.Condition.*;
import AnnotatedTree.Processor.NodeDrawableCollector;
import PropBank.Argument;
import PropBank.Frameset;
import PropBank.FramesetList;
import WordNet.WordNet;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

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

    private void readFromLine(String line) throws ParenthesisInLayerException{
        if (line.contains("(") && line.contains(")")){
            line = line.substring(line.indexOf("(") + 1, line.lastIndexOf(")")).trim();
            root = new ParseNodeDrawable(null, line, false, 0);
            updateTraversalIndexes();
        } else {
            root = null;
        }
    }

    private void readFromFile(String currentPath){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileDescription.getFileName(currentPath)), StandardCharsets.UTF_8));
            String line = br.readLine();
            readFromLine(line);
            if (root == null){
                System.out.println("File " + fileDescription.getFileName(currentPath) + " is not a valid parse tree file");
            }
            br.close();
        } catch (IOException e) {
            root = null;
        } catch (ParenthesisInLayerException e) {
            System.out.println(e.toString() + " in file " + fileDescription.getFileName(currentPath));
            root = null;
        }
    }

    public ParseTreeDrawable(String line){
        try {
            readFromLine(line);
        } catch (ParenthesisInLayerException e) {
            System.out.println(e.toString());
            root = null;
        }
    }

    public ParseTreeDrawable(FileInputStream file){
        try {
            name = file.getFD().toString();
            BufferedReader br = new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8));
            String line = br.readLine();
            readFromLine(line);
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

    public HashSet<Frameset> getPredicateSynSets(FramesetList framesetList){
        HashSet<Frameset> synSets = new HashSet<>();
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) root, new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable leafNode : leafList){
            Argument argument = leafNode.getLayerInfo().getArgument();
            if (argument != null && argument.getArgumentType().equals("PREDICATE")){
                if (framesetList.frameExists(leafNode.getLayerInfo().getArgument().getId())){
                    synSets.add(framesetList.getFrameSet(leafNode.getLayerInfo().getArgument().getId()));
                }
            }
        }
        return synSets;
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

    public AnnotatedSentence generateAnnotatedSentence(String language){
        AnnotatedSentence sentence = new AnnotatedSentence();
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable)root, new IsEnglishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            AnnotatedWord newWord = new AnnotatedWord("{" + language + "=" + parseNode.getData().getName() + "}{posTag="
                    + parseNode.getParent().getData().getName() + "}");
            sentence.addWord(newWord);
        }
        return sentence;
    }

    public ParseTree generateParseTree(boolean surfaceForm){
        ParseTree result = new ParseTree(new ParseNode(root.getData()));
        ((ParseNodeDrawable) root).generateParseNode(result.getRoot(), surfaceForm);
        return result;
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

    public ArrayList<String> extractTags(){
        ArrayList<String> tagList = new ArrayList<>();
        ((ParseNodeDrawable)root).extractTags(tagList);
        return tagList;
    }

    public ArrayList<Integer> extractNumberOfChildren(){
        ArrayList<Integer> numberOfChildrenList = new ArrayList<>();
        ((ParseNodeDrawable)root).extractNumberOfChildren(numberOfChildrenList);
        return numberOfChildrenList;
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

}
