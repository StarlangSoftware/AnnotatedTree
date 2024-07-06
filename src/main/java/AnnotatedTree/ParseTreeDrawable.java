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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class ParseTreeDrawable extends ParseTree {
    private FileDescription fileDescription;
    private int maxInOrderTraversalIndex;

    /**
     * Constructor for the ParseTreeDrawable. Sets the file description and reads the tree from the file description.
     * @param path Path of the tree
     * @param rawFileName File name of the tree such as 0123.train.
     */
    public ParseTreeDrawable(String path, String rawFileName){
        this(new FileDescription(path, rawFileName));
    }

    /**
     * Another constructor for the ParseTreeDrawable. Sets the file description and reads the tree from the file
     * description.
     * @param path Path of the tree
     * @param extension Extension of the file such as train, test or dev.
     * @param index Index of the file such as 1235.
     */
    public ParseTreeDrawable(String path, String extension, int index){
        this(new FileDescription(path, extension, index));
    }

    /**
     * Another constructor for the ParseTreeDrawable. Sets the file description and reads the tree from the file
     * description.
     * @param path Path of the tree
     * @param fileDescription File description that contains the path, index and extension information.
     */
    public ParseTreeDrawable(String path, FileDescription fileDescription){
        this(new FileDescription(path, fileDescription.getExtension(), fileDescription.getIndex()));
    }

    /**
     * Another constructor for the ParseTreeDrawable. Sets the file description and reads the tree from the file
     * description.
     * @param fileDescription File description that contains the path, index and extension information.
     */
    public ParseTreeDrawable(FileDescription fileDescription){
        this.fileDescription = fileDescription;
        readFromFile(fileDescription.getPath());
    }

    /**
     * Mutator method for the fileDescription attribute.
     * @param fileDescription New fileDescription value.
     */
    public void setFileDescription(FileDescription fileDescription){
        this.fileDescription = fileDescription;
    }

    /**
     * Accessor method for the fileDescription attribute.
     * @return FileDescription attribute.
     */
    public FileDescription getFileDescription(){
        return fileDescription;
    }

    /**
     * Copies the file description information from the given parse tree.
     * @param parseTree Parse tree whose file description information will be copied.
     */
    public void copyInfo(ParseTreeDrawable parseTree){
        this.fileDescription = parseTree.fileDescription;
    }

    /**
     * Reloads the tree from the input file.
     */
    public void reload(){
        readFromFile(fileDescription.getPath());
    }

    /**
     * Mutator for the root attribute.
     * @param newRootNode New root node.
     */
    public void setRoot(ParseNode newRootNode){
        root = newRootNode;
    }

    /**
     * Reads the parse tree from the given line. It sets the root node which calls ParseNodeDrawable constructor
     * recursively.
     * @param line Line containing the definition of the tree.
     */
    private void readFromLine(String line) throws ParenthesisInLayerException{
        if (line.contains("(") && line.contains(")")){
            line = line.substring(line.indexOf("(") + 1, line.lastIndexOf(")")).trim();
            root = new ParseNodeDrawable(null, line, false, 0);
            updateTraversalIndexes();
        } else {
            root = null;
        }
    }

    /**
     * Reads the parse tree from the given file description with path replaced with the currentPath. It sets the root
     * node which calls ParseNodeDrawable constructor recursively.
     * @param currentPath Path of the tree
     */
    private void readFromFile(String currentPath){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(fileDescription.getFileName(currentPath))), StandardCharsets.UTF_8));
            String line = br.readLine();
            readFromLine(line);
            if (root == null){
                System.out.println("File " + fileDescription.getFileName(currentPath) + " is not a valid parse tree file");
            }
            br.close();
        } catch (IOException e) {
            root = null;
        } catch (ParenthesisInLayerException e) {
            System.out.println(e + " in file " + fileDescription.getFileName(currentPath));
            root = null;
        }
    }

    /**
     * Another constructor of the ParseTree. The method takes the line as input and constructs
     * the whole tree by calling the ParseNode constructor recursively.
     * @param line Line for a ParseTree
     */
    public ParseTreeDrawable(String line){
        try {
            readFromLine(line);
        } catch (ParenthesisInLayerException e) {
            System.out.println(e);
            root = null;
        }
    }

    /**
     * Another constructor of the ParseTree. The method takes the file containing a single line as input and constructs
     * the whole tree by calling the ParseNode constructor recursively.
     * @param file File containing a single line for a ParseTree
     */
    public ParseTreeDrawable(FileInputStream file){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8));
            String line = br.readLine();
            readFromLine(line);
            br.close();
        } catch (IOException e) {
            root = null;
        } catch (ParenthesisInLayerException e) {
            System.out.println(e);
            root = null;
        }
    }

    /**
     * Sets the inOrderTraversalIndex attribute of all nodes in tree. InOrderTraversalIndex shows the index of the
     * node according to the inorder traversal. Sets also the leafIndex attribute. LeafIndex shows the index of the
     * leaf node according to the inorder traversal without considering non-leaf nodes.
     */
    private void updateTraversalIndexes(){
        ((ParseNodeDrawable)root).inOrderTraversal(0);
        ((ParseNodeDrawable)root).leafTraversal(0);
        maxInOrderTraversalIndex = ((ParseNodeDrawable) root).maxInOrderTraversal();
    }

    /**
     * Accessor for the maxInOrderTraversalIndex attribute
     * @return maxInOrderTraversalIndex attribute.
     */
    public int getMaxInOrderTraversalIndex(){
        return maxInOrderTraversalIndex;
    }

    /**
     * Loads the next tree according to the index of the parse tree. For example, if the current
     * tree fileName is 0123.train, after the call of nextTree(3), the method will load 0126.train. If the next tree
     * does not exist, nothing will happen.
     * @param count Number of trees to go forward
     */
    public void nextTree(int count){
        if (fileDescription.nextFileExists(count)){
            fileDescription.addToIndex(count);
            reload();
        }
    }

    /**
     * Loads the previous tree according to the index of the parse tree. For example, if the current
     * tree fileName is 0123.train, after the call of previousTree(4), the method will load 0119.train. If the
     * previous tree does not exist, nothing will happen.
     * @param count Number of trees to go backward
     */
    public void previousTree(int count){
        if (fileDescription.previousFileExists(count)){
            fileDescription.addToIndex(-count);
            reload();
        }
    }

    /**
     * Saves current tree.
     */
    public void save(){
        BufferedWriter fw;
        try {
            fw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(fileDescription.getFileName())), StandardCharsets.UTF_8));
            fw.write("( " + this + " )\n");
            fw.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * Saves current tree to the newPath with other file properties staying the same.
     * @param newPath Path to which tree will be saved
     */
    public void saveWithPath(String newPath){
        BufferedWriter fw;
        try {
            fw = new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(Paths.get(fileDescription.getFileName(newPath))), StandardCharsets.UTF_8));
            fw.write("( " + this + " )\n");
            fw.close();
        } catch (IOException ignored) {
        }
    }

    /**
     * Returns the number of gloss agreements between this tree and the given tree. Two nodes agree in
     * glosses if they are both leaf nodes and their layer info are the same.
     * @param parseTree Parse tree to compare in gloss manner
     * @param viewLayerType Layer name to compare
     * @return The number of gloss agreements between this node and the given node recursively.
     */
    public int glossAgreementCount(ParseTree parseTree, ViewLayerType viewLayerType){
        return ((ParseNodeDrawable)root).glossAgreementCount((ParseNodeDrawable) parseTree.getRoot(), viewLayerType);
    }

    /**
     * Returns the number of structural agreement between this tree and the given tree. Two nodes agree in
     * structural manner if they have the same number of children and all of their children have the same tags in the
     * same order.
     * @param parseTree Parse tree to compare in structural manner
     * @return The number of structural agreement between this tree and the given tree.
     */
    public int structureAgreementCount(ParseTree parseTree){
        return ((ParseNodeDrawable)root).structureAgreementCount((ParseNodeDrawable)parseTree.getRoot());
    }

    /**
     * Returns all nodes in the current tree those satisfy the conditions in the given second tree.
     * @param tree Tree containing the search condition
     * @return All nodes in the current tree those satisfy the conditions in the given second tree.
     */
    public ArrayList<ParseNodeDrawable> satisfy(ParseTreeSearchable tree){
        return ((ParseNodeDrawable)root).satisfy(tree);
    }

    /**
     * Updates all pos tags in the leaf nodes according to the morphological tag in those leaves.
     * nodes.
     */
    public void updatePosTags(){
        ((ParseNodeDrawable)root).updatePosTags();
    }

    /**
     * Calculates the score of this tree compared to the given correctTree. If the children of
     * a node are the same as the children of the corresponding node in the correctTree (also in the same order), then
     * the score is 1, otherwise the score is 0. The total score is the sum of all scores of all nodes in the tree.
     * @param correctTree Tree to be compared with this tree.
     * @return Number of nodes matched in the subtree rooted with this node.
     */
    public int score(ParseTreeDrawable correctTree){
        return ((ParseNodeDrawable)root).score((ParseNodeDrawable)correctTree.root);
    }

    /**
     * Checks if the children of the root node is a permutation of the children of the root node of the given tree.
     * @param tree Parse tree to be compared.
     * @return True if the children of the root node is a permutation of the children of the root node of the given tree,
     * false otherwise.
     */
    public boolean isPermutation(ParseTreeDrawable tree){
        return ((ParseNodeDrawable)root).isPermutation((ParseNodeDrawable)tree.root);
    }

    /**
     * Calculates the maximum depth of the tree.
     * @return The maximum depth of the tree.
     */
    public int maxDepth(){
        return ((ParseNodeDrawable) root).maxDepth();
    }

    /**
     * Swaps the given child node of this node with the previous sibling of that given node. If the given node is the
     * leftmost child, it swaps with the last node.
     * @param node Node to be swapped.
     */
    public void moveLeft(ParseNode node){
        if (root != node){
            root.moveLeft(node);
            updateTraversalIndexes();
        }
    }

    /**
     * Swaps the given child node of this node with the next sibling of that given node. If the given node is the
     * rightmost child, it swaps with the first node.
     * @param node Node to be swapped.
     */
    public void moveRight(ParseNode node){
        if (root != node){
            root.moveRight(node);
            updateTraversalIndexes();
        }
    }

    /**
     * Divides the given node into multiple parse nodes if it contains more than one word. The parent node will be
     * the same for the new nodes, original node is deleted from the children, the pos tags of the new parse nodes will
     * be determined according to their morphological parses.
     * @param parseNode Parse node to be divided
     */
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
        } catch (LayerNotExistsException | ParenthesisInLayerException | WordNotExistsException ignored) {
        }
    }

    public HashMap<ParseNode, ParseNodeDrawable> mapTree(ParseTreeDrawable parseTree){
        HashMap<ParseNode, ParseNodeDrawable> nodeMap = new HashMap<>();
        ((ParseNodeDrawable)root).mapTree((ParseNodeDrawable) parseTree.getRoot(), nodeMap);
        return nodeMap;
    }

    /**
     * Moves the subtree rooted at fromNode as a child to the node toNode.
     * @param fromNode Subtree root node to be moved.
     * @param toNode Node to which a new subtree will be added.
     */
    public void moveNode(ParseNode fromNode, ParseNode toNode){
        if (root != fromNode){
            ParseNode parent = fromNode.getParent();
            parent.removeChild(fromNode);
            toNode.addChild(fromNode);
            updateTraversalIndexes();
            ((ParseNodeDrawable) root).updateDepths(0);
        }
    }

    /**
     * Moves the subtree rooted at fromNode as a child to the node toNode at position childIndex.
     * @param fromNode Subtree root node to be moved.
     * @param toNode Node to which a new subtree will be added.
     * @param childIndex New child index of the toNode.
     */
    public void moveNode(ParseNode fromNode, ParseNode toNode, int childIndex){
        if (root != fromNode){
            ParseNode parent = fromNode.getParent();
            parent.removeChild(fromNode);
            toNode.addChild(childIndex, fromNode);
            updateTraversalIndexes();
            ((ParseNodeDrawable) root).updateDepths(0);
        }
    }

    /**
     * Removed the first child of the parent node and adds the given child node as a child to that node.
     * @param parent Parent node.
     * @param child New child node to be added.
     */
    public void combineWords(ParseNodeDrawable parent, ParseNodeDrawable child){
        while (parent.numberOfChildren() > 0){
            parent.removeChild(parent.firstChild());
        }
        parent.addChild(child);
        updateTraversalIndexes();
        ((ParseNodeDrawable) root).updateDepths(0);
    }

    /**
     * The method checks if all nodes in the tree has the annotation in the given layer.
     * @param viewLayerType Layer name
     * @return True if all nodes in the tree has the annotation in the given layer, false otherwise.
     */
    public boolean layerExists(ViewLayerType viewLayerType){
        return ((ParseNodeDrawable)(root)).layerExists(viewLayerType);
    }

    /**
     * Checks if all nodes in the tree has annotation with the given layer.
     * @param viewLayerType Layer name
     * @return True if all nodes in the tree has annotation with the given layer, false otherwise.
     */
    public boolean layerAll(ViewLayerType viewLayerType){
        return ((ParseNodeDrawable)(root)).layerAll(viewLayerType);
    }

    /**
     * Returns the framesets that are used to annotate the leaf nodes in the current tree with "PREDICATE".
     * @param framesetList Framenet which contains the framesets.
     * @return The framesets that are used to annotate the leaf nodes in the current tree with "PREDICATE".
     */
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

    /**
     * Replaces id's of predicates, which have previousId as synset id, with currentId. Replaces also predicate id's of
     * frame elements, which have predicate id's previousId, with currentId.
     * @param previousId Previous id of the synset.
     * @param currentId Replacement id.
     * @return Returns true, if any replacement has been done; false otherwise.
     */
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

    /**
     * Returns the leaf node that comes one after the given parse node according to the inorder traversal.
     * @param parseNode Input parse node.
     * @return The leaf node that comes one after the given parse node according to the inorder traversal.
     */
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

    /**
     * Returns the leaf node that comes one before the given parse node according to the inorder traversal.
     * @param parseNode Input parse node.
     * @return The leaf node that comes one before the given parse node according to the inorder traversal.
     */
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

    /**
     * Sets the shallow parse layer of all nodes in the tree according to the given chunking type.
     * @param chunkType Type of the chunking used to annotate.
     */
    public void setShallowParseLayer(ChunkType chunkType){
        if (root != null){
            ((ParseNodeDrawable)root).setShallowParseLayer(chunkType);
        }
    }

    /**
     * Clears the given layer for all nodes in the tree
     * @param layerType Layer name
     */
    public void clearLayer(ViewLayerType layerType){
        if (root != null){
            ((ParseNodeDrawable)root).clearLayer(layerType);
        }
    }

    /**
     * Constructs an AnnotatedSentence object from the Turkish tree. Collects all leaf nodes, then for each leaf node
     * converts layer info of all words at that node to AnnotatedWords. Layers are converted to the counterparts in the
     * AnnotatedWord.
     * @return AnnotatedSentence counterpart of the Turkish tree
     */
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
            } catch (LayerNotExistsException ignored) {
            }
        }
        return sentence;
    }

    /**
     * Constructs an AnnotatedSentence object from the English tree. Collects all leaf nodes, then for each leaf node
     * converts the word with its parents pos tag to AnnotatedWord.
     * @param language English or Persian.
     * @return AnnotatedSentence counterpart of the English tree
     */
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

    /**
     * Recursive method that generates a new parse tree by replacing the tag information of the all parse nodes (with all
     * its descendants) with respect to the morphological annotation of all parse nodes (with all its descendants)
     * of the current parse tree.
     * @param surfaceForm If true, tag will be replaced with the surface form annotation.
     * @return A new parse tree by replacing the tag information of the all parse nodes with respect to the
     * morphological annotation of all parse nodes of the current parse tree.
     */
    public ParseTree generateParseTree(boolean surfaceForm){
        ParseTree result = new ParseTree(new ParseNode(root.getData()));
        ((ParseNodeDrawable) root).generateParseNode(result.getRoot(), surfaceForm);
        return result;
    }

    /**
     * Returns list of nodes that contain verbs.
     * @param wordNet Wordnet used for checking the pos tag of the synset.
     * @return List of nodes that contain verbs.
     */
    public ArrayList<ParseNodeDrawable> extractNodesWithVerbs(WordNet wordNet){
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) root, new IsVerbNode(wordNet));
        return nodeDrawableCollector.collect();
    }

    /**
     * Returns list of nodes that contain verbs which are annotated as 'PREDICATE'.
     * @param wordNet Wordnet used for checking the pos tag of the synset.
     * @return List of nodes that contain verbs which are annotated as 'PREDICATE'.
     */
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

    /**
     * Returns tag symbols of all nodes in the tree.
     * @return List of tag symbols of all nodes in the tree.
     */
    public ArrayList<String> extractTags(){
        ArrayList<String> tagList = new ArrayList<>();
        ((ParseNodeDrawable)root).extractTags(tagList);
        return tagList;
    }

    /**
     * Returns number of children of all nodes in the tree.
     * @return A list of number of children of all nodes in the tree.
     */
    public ArrayList<Integer> extractNumberOfChildren(){
        ArrayList<Integer> numberOfChildrenList = new ArrayList<>();
        ((ParseNodeDrawable)root).extractNumberOfChildren(numberOfChildrenList);
        return numberOfChildrenList;
    }

    /**
     * Returns the index of the layer data in the given x and y coordinates in the panel that displays the annotated
     * tree.
     * @param x x coordinate
     * @param y y coordinate
     * @return Index of the layer data in the given x and y coordinates in the panel that displays the annotated tree.
     */
    public int getSubItemAt(int x, int y){
        return ((ParseNodeDrawable)root).getSubItemAt(x, y);
    }

    /**
     * Returns the leaf node in the given x and y coordinates in the panel that displays the annotated tree.
     * @param x x coordinate
     * @param y y coordinate
     * @return The leaf node in the given x and y coordinates in the panel that displays the annotated tree.
     */
    public ParseNodeDrawable getLeafNodeAt(int x, int y){
        return ((ParseNodeDrawable)root).getLeafNodeAt(x, y);
    }

    /**
     * Returns the parse node in the given x and y coordinates in the panel that displays the annotated tree.
     * @param x x coordinate
     * @param y y coordinate
     * @return The parse node in the given x and y coordinates in the panel that displays the annotated tree.
     */
    public ParseNodeDrawable getNodeAt(int x, int y){
        return ((ParseNodeDrawable)root).getNodeAt(x, y);
    }

    /**
     * Returns the leaf node at position index in the tree.
     * @param index Position of the leaf node.
     * @return The leaf node at position index in the tree.
     */
    public ParseNodeDrawable getLeafWithIndex(int index){
        return ((ParseNodeDrawable)root).getLeafWithIndex(index);
    }

}
