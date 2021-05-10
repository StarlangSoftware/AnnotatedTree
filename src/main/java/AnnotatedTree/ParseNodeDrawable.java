package AnnotatedTree;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedSentence.ViewLayerType;
import ContextFreeGrammar.ContextFreeGrammar;
import MorphologicalAnalysis.MorphologicalParse;
import MorphologicalAnalysis.MorphologicalTag;
import ParseTree.ParseNode;
import ContextFreeGrammar.Rule;
import ParseTree.Symbol;
import Dictionary.EnglishWordComparator;
import AnnotatedTree.Processor.Condition.IsTurkishLeafNode;
import AnnotatedTree.Processor.NodeDrawableCollector;
import Dictionary.*;
import NamedEntityRecognition.Gazetteer;
import Util.Permutation;

import java.awt.*;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;

public class ParseNodeDrawable extends ParseNode {

    protected LayerInfo layers = null;
    protected int inOrderTraversalIndex;
    protected int leafIndex = -1;
    protected int depth;
    protected Rectangle area;
    protected boolean selected = false;
    protected boolean editable = false;
    protected boolean dragged = false;
    protected boolean searched = false;
    protected int selectedIndex = -1;
    private static String verbalLabel = "VG";
    private boolean hasDeletedChild = false;
    private boolean guessed = false;
    private static ArrayList<String> sentenceLabels = new ArrayList<String>(Arrays.asList("SINV", "SBARQ", "SBAR", "SQ", "S"));

    public ParseNodeDrawable(ParseNodeDrawable parent, String line, boolean isLeaf, int depth) throws ParenthesisInLayerException {
        int parenthesisCount = 0;
        String childLine = "";
        this.depth = depth;
        this.parent = parent;
        children = new ArrayList<>();
        area = null;
        if (isLeaf){
            if (!line.contains("{")){
                data = new Symbol(line);
            } else {
                layers = new LayerInfo(line);
            }
        } else {
            int startPos = line.indexOf(" ");
            if (startPos < 0){
                throw new ParenthesisInLayerException(line);
            }
            data = new Symbol(line.substring(1, startPos));
            if (line.indexOf(")") == line.lastIndexOf(")")){
                children.add(new ParseNodeDrawable(this, line.substring(startPos + 1, line.indexOf(")")), true, depth + 1));
            } else {
                for (int i = startPos + 1; i < line.length(); i++){
                    if (line.charAt(i) != ' ' || parenthesisCount > 0){
                        childLine = childLine + line.charAt(i);
                    }
                    if (line.charAt(i) == '('){
                        parenthesisCount++;
                    } else {
                        if (line.charAt(i) == ')'){
                            parenthesisCount--;
                        }
                    }
                    if (parenthesisCount == 0 && !childLine.isEmpty()){
                        children.add(new ParseNodeDrawable(this, childLine.trim(), false, depth + 1));
                        childLine = "";
                    }
                }
            }
        }
    }

    public ParseNodeDrawable(Symbol data){
        super(data);
    }

    public ParseNodeDrawable(ParseNodeDrawable parent, ParseNodeDrawable child, String symbol){
        this.children = new ArrayList<ParseNode>();
        this.depth = child.depth;
        child.updateDepths(this.depth + 1);
        this.parent = parent;
        this.parent.setChild(parent.getChildIndex(child), this);
        this.children.add(child);
        child.parent = this;
        this.data = new Symbol(symbol);
        this.inOrderTraversalIndex = child.inOrderTraversalIndex;
    }

    public ParseNodeDrawable clone(){
        ParseNodeDrawable result = new ParseNodeDrawable(data);
        result.children = new ArrayList<ParseNode>();
        if (layers != null)
            result.layers = layers.clone();
        return result;
    }

    public LayerInfo getLayerInfo(){
        return layers;
    }

    public Symbol getData(){
        if (layers == null){
            return super.getData();
        } else {
            return new Symbol(getLayerData(ViewLayerType.ENGLISH_WORD));
        }
    }

    public void clearLayers(){
        layers = new LayerInfo();
    }

    public void clearLayer(ViewLayerType layerType){
        if (children.size() == 0 && layerExists(layerType)){
            layers.removeLayer(layerType);
        }
        for (int i = 0; i < numberOfChildren(); i++){
            ((ParseNodeDrawable) children.get(i)).clearLayer(layerType);
        }
    }

    public void clearData(){
        data = null;
    }

    public void setDataAndClearLayers(Symbol data){
        super.setData(data);
        layers = null;
    }

    public void setGuessed(){
        guessed = true;
    }

    public void setData(Symbol data){
        if (layers == null){
            super.setData(data);
        } else {
            layers.setLayerData(ViewLayerType.ENGLISH_WORD, data.getName());
        }
    }

    public String headWord(ViewLayerType viewLayerType){
        if (children.size() > 0){
            return ((ParseNodeDrawable) headChild()).headWord(viewLayerType);
        } else {
            return getLayerData(viewLayerType);
        }
    }

    public String getLayerData(){
        if (data != null)
            return data.getName();
        return layers.getLayerDescription();
    }

    public String getLayerData(ViewLayerType viewLayer){
        if (viewLayer == ViewLayerType.WORD || layers == null)
            return data.getName();
        return layers.getLayerData(viewLayer);
    }

    public int getLeafIndex(){
        return leafIndex;
    }

    public int getDepth(){
        return depth;
    }

    public int leafTraversal(int pos){
        int i;
        if (children.size() == 0){
            pos++;
            leafIndex = pos;
        }
        for (i = 0; i < children.size(); i++){
            pos = ((ParseNodeDrawable) children.get(i)).leafTraversal(pos);
        }
        return pos;
    }

    public int inOrderTraversal(int pos){
        int i;
        for (i = 0; i < children.size() / 2; i++){
            pos = ((ParseNodeDrawable) children.get(i)).inOrderTraversal(pos);
        }
        inOrderTraversalIndex = pos;
        if (children.size() % 2 != 1)
            pos++;
        for (i = children.size() / 2; i < children.size(); i++){
            pos = ((ParseNodeDrawable) children.get(i)).inOrderTraversal(pos);
        }
        return pos;
    }

    public int maxInOrderTraversal(){
        int maxIndex, childIndex;
        if (children.size() == 0)
            return inOrderTraversalIndex;
        else {
            maxIndex = inOrderTraversalIndex;
            for (ParseNode aChildren : children) {
                ParseNodeDrawable aChild = (ParseNodeDrawable) aChildren;
                childIndex = aChild.maxInOrderTraversal();
                if (childIndex > maxIndex) {
                    maxIndex = childIndex;
                }
            }
            return maxIndex;
        }
    }

    public int structureAgreementCount(ParseNodeDrawable parseNode){
        if (children.size() > 1){
            int sum = 1;
            for (int i = 0; i < children.size(); i++){
                if (i < parseNode.numberOfChildren()){
                    if (!getChild(i).getData().getName().equalsIgnoreCase(parseNode.getChild(i).getData().getName())){
                        sum = 0;
                        break;
                    }
                } else {
                    sum = 0;
                    break;
                }
            }
            for (int i = 0; i < children.size(); i++){
                if (i < parseNode.numberOfChildren() && getChild(i).getData().getName().equalsIgnoreCase(parseNode.getChild(i).getData().getName())){
                    sum += ((ParseNodeDrawable) getChild(i)).structureAgreementCount((ParseNodeDrawable) parseNode.getChild(i));
                } else {
                    for (int j = 0; j < parseNode.numberOfChildren(); j++){
                        if (getChild(i).getData().getName().equalsIgnoreCase(parseNode.getChild(j).getData().getName())){
                            sum += ((ParseNodeDrawable) getChild(i)).structureAgreementCount((ParseNodeDrawable) parseNode.getChild(j));
                            break;
                        }
                    }
                }
            }
            return sum;
        } else {
            return 0;
        }
    }

    public int glossAgreementCount(ParseNodeDrawable parseNode, ViewLayerType viewLayerType){
        if (children.size() == 0){
            if (parseNode.numberOfChildren() == 0){
                if (getLayerData(viewLayerType).equalsIgnoreCase(parseNode.getLayerData(viewLayerType))){
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        } else {
            int sum = 0;
            for (int i = 0; i < children.size(); i++){
                if (i < parseNode.numberOfChildren()){
                    sum += ((ParseNodeDrawable) getChild(i)).glossAgreementCount((ParseNodeDrawable) parseNode.getChild(i), viewLayerType);
                }
            }
            return sum;
        }
    }

    public void replaceChild(ParseNodeDrawable oldChild, ParseNodeDrawable newChild){
        newChild.updateDepths(this.depth + 1);
        newChild.parent = this;
        children.set(children.indexOf(oldChild), newChild);
    }

    public void updateDepths(int depth){
        this.depth = depth;
        for (ParseNode aChildren:children){
            ParseNodeDrawable aChild = (ParseNodeDrawable) aChildren;
            aChild.updateDepths(depth + 1);
        }
    }

    public int maxDepth(){
        int depth = this.depth;
        for (ParseNode aChildren : children) {
            ParseNodeDrawable aChild = (ParseNodeDrawable) aChildren;
            if (aChild.maxDepth() > depth)
                depth = aChild.maxDepth();
        }
        return depth;
    }

    public int getSelectedIndex(){
        return selectedIndex;
    }

    public Rectangle getArea(){
        return area;
    }

    public void setSelected(boolean selected){
        this.selected = selected;
        selectedIndex = -1;
    }

    public void setSelected(boolean selected, int selectedIndex){
        this.selected = selected;
        this.selectedIndex = selectedIndex;
    }

    public void setEditable(boolean editable){
        this.editable = editable;
    }

    public void setSearched(boolean searched){
        this.searched = searched;
    }

    public void setDragged(boolean dragged){
        this.dragged = dragged;
    }

    public void setDragged(boolean dragged, int selectedIndex){
        this.dragged = dragged;
        this.selectedIndex = selectedIndex;
    }

    public void setChildDeleted(){
        hasDeletedChild = true;
    }

    public boolean hasDeletedChild(){
        return hasDeletedChild;
    }

    public int getSubItemAt(int x, int y){
        if (area.contains(x, y) && children.size() == 0)
            return (int) ((y - area.getY()) / 20);
        else {
            for (ParseNode aChildren : children) {
                ParseNodeDrawable aChild = (ParseNodeDrawable) aChildren;
                int result = aChild.getSubItemAt(x, y);
                if (result != -1){
                    return result;
                }
            }
            return -1;
        }
    }

    public boolean satisfy(ParseNodeSearchable node){
        int i;
        if (node.isLeaf() && children.size() > 0)
            return false;
        for (i = 0; i < node.size(); i++){
            ViewLayerType viewLayer = node.getViewLayerType(i);
            String data = node.getValue(i);
            if (getLayerData(viewLayer) == null && node.getType(i) != SearchType.EQUALS && node.getType(i) != SearchType.IS_NULL){
                return false;
            }
            switch (node.getType(i)) {
                case CONTAINS:
                    if (!getLayerData(viewLayer).contains(data)) {
                        return false;
                    }
                    break;
                case EQUALS:
                    if (getLayerData(viewLayer) == null) {
                        if (!node.getValue(i).isEmpty()) {
                            return false;
                        }
                    } else {
                        if (!getLayerData(viewLayer).equals(data)) {
                            return false;
                        }
                    }
                    break;
                case EQUALS_IGNORE_CASE:
                    if (!getLayerData(viewLayer).equalsIgnoreCase(data)) {
                        return false;
                    }
                    break;
                case MATCHES:
                    if (!getLayerData(viewLayer).matches(data)) {
                        return false;
                    }
                    break;
                case STARTS:
                    if (!getLayerData(viewLayer).startsWith(data)) {
                        return false;
                    }
                    break;
                case ENDS:
                    if (!getLayerData(viewLayer).endsWith(data)) {
                        return false;
                    }
                    break;
                case IS_NULL:
                    if (getLayerData(viewLayer) != null) {
                        return false;
                    }
            }
        }
        if (node.numberOfChildren() > children.size()){
            return false;
        }
        for (i = 0; i < children.size(); i++){
            if (i < node.numberOfChildren() && !((ParseNodeDrawable)getChild(i)).satisfy((ParseNodeSearchable)node.getChild(i))){
                return false;
            }
        }
        return true;
    }

    public void updatePosTags(){
        if (children.size() == 1 && children.get(0).isLeaf() && !children.get(0).isDummyNode()){
            LayerInfo layerInfo = ((ParseNodeDrawable)children.get(0)).getLayerInfo();
            try {
                MorphologicalParse morphologicalParse = layerInfo.getMorphologicalParseAt(layerInfo.getNumberOfWords() - 1);
                String symbol = morphologicalParse.getTreePos();
                setData(new Symbol(symbol));
            } catch (LayerNotExistsException | WordNotExistsException e) {
                e.printStackTrace();
            }
        } else {
            for (ParseNode aChildren:children){
                ParseNodeDrawable aChild = (ParseNodeDrawable) aChildren;
                aChild.updatePosTags();
            }
        }
    }

    public int score(ParseNodeDrawable correctNode){
        int sum;
        boolean isCorrectOrder = true;
        if (correctNode.numberOfChildren() < 2)
            return 0;
        for (int i = 0; i < children.size(); i++){
            isCorrectOrder = ((ParseNodeDrawable)correctNode.getChild(i)).isPermutation((ParseNodeDrawable)getChild(i));
            if (!isCorrectOrder)
                break;
        }
        if (isCorrectOrder)
            sum = 1;
        else{
            sum = 0;
            NodePermutation p = new NodePermutation(this, correctNode);
            p.apply(children);
        }
        for (int i = 0; i < children.size(); i++)
            sum += ((ParseNodeDrawable)getChild(i)).score((ParseNodeDrawable)correctNode.getChild(i));
        return sum;
    }

    public ArrayList<Symbol> getChildrenSymbols(){
        ArrayList<Symbol> childrenSymbols = new ArrayList<>();
        for (ParseNode node: children){
            ParseNodeDrawable treeNode = (ParseNodeDrawable)node;
            childrenSymbols.add(treeNode.getData());
        }
        return childrenSymbols;
    }

    public void augment(){
        for (ParseNode child: children)
            ((ParseNodeDrawable)child).augment();
        ArrayList<Symbol> childrenSymbols = getChildrenSymbols();
        Collections.sort(childrenSymbols, new EnglishWordComparator());
        if (childrenSymbols.size() > 0){
            if (layers != null){
                layers.setLayerData(ViewLayerType.ENGLISH_WORD, layers.getLayerData(ViewLayerType.ENGLISH_WORD) + childrenSymbols.toString());
            } else {
                data = new Symbol(data.getName() + childrenSymbols.toString());
            }
        }
    }

    public String ancestorString(){
        if (parent == null){
            return data.getName();
        } else {
            if (layers == null){
                return parent.ancestorString() + data.getName();
            } else {
                return parent.ancestorString() + layers.getLayerData(ViewLayerType.ENGLISH_WORD);
            }
        }
    }

    public void deAugment(){
        String old;
        old = getData().getName();
        int index = old.indexOf("[");
        if (index >= 0){
            setData(new Symbol(old.substring(0, index)));
        }
        for (ParseNode child: children)
            ((ParseNodeDrawable)child).deAugment();
    }

    public boolean isPermutation(ParseNodeDrawable thatParseNode){
        boolean b;
        this.augment();
        thatParseNode.augment();
        b = getData().equals(thatParseNode.getData());
        deAugment();
        thatParseNode.deAugment();
        return b;
    }

    public boolean layerExists(ViewLayerType viewLayerType){
        if (children.size() == 0){
            if (getLayerData(viewLayerType) != null){
                return true;
            }
        } else {
            for (ParseNode aChild : children) {
                if (((ParseNodeDrawable)aChild).layerExists(viewLayerType)){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDummyNode(){
        String data = getLayerData(ViewLayerType.ENGLISH_WORD);
        String parentData = ((ParseNodeDrawable) parent).getLayerData(ViewLayerType.ENGLISH_WORD);
        String targetData = getLayerData(ViewLayerType.TURKISH_WORD);
        if (data != null && parentData != null){
            if (targetData != null && targetData.contains("*")){
                return true;
            }
            return data.contains("*") || (data.equals("0") && parentData.equals("-NONE-"));
        } else {
            return false;
        }
    }

    public boolean mapTree(ParseNodeDrawable parseNode, HashMap<ParseNode, ParseNodeDrawable> nodeMap){
        if (this.children.size() > 0){
            if (this.getData().equals(parseNode.getData()) && this.children.size() == parseNode.children.size()){
                Permutation permutation = new Permutation(children.size());
                boolean found = false;
                do{
                    boolean childrenOk = true;
                    for (int i = 0; i < children.size(); i++){
                        if (!((ParseNodeDrawable)children.get(i)).mapTree((ParseNodeDrawable) parseNode.children.get(permutation.get()[i]), nodeMap)){
                            childrenOk = false;
                            break;
                        }
                    }
                    if (childrenOk){
                        nodeMap.put(this, parseNode);
                        found = true;
                    }
                }while (permutation.next());
                return found;
            } else {
                return false;
            }
        } else {
            if (this.getData().getName().equals(parseNode.getLayerData(ViewLayerType.ENGLISH_WORD))){
                nodeMap.put(this, parseNode);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean layerAll(ViewLayerType viewLayerType){
        if (children.size() == 0){
            if (getLayerData(viewLayerType) == null && !isDummyNode()){
                return false;
            }
        } else {
            for (ParseNode aChild : children) {
                if (!((ParseNodeDrawable)aChild).layerAll(viewLayerType)){
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<ParseNodeDrawable> satisfy(ParseTreeSearchable tree){
        ArrayList<ParseNodeDrawable> result = new ArrayList<ParseNodeDrawable>();
        if (satisfy((ParseNodeSearchable)tree.getRoot())){
            result.add(this);
        }
        for (ParseNode child:children){
            result.addAll(((ParseNodeDrawable)child).satisfy(tree));
        }
        return result;
    }

    private void attachVGToVP(ParseNodeDrawable node){
        ParseNodeDrawable pn = new ParseNodeDrawable(new Symbol(verbalLabel));
        pn.addChild(node.clone());
        pn.parent = this;
        ParseNodeDrawable current = node;
        while (current.parent != this)
            current = (ParseNodeDrawable) current.parent;
        int index = this.children.indexOf(current);
        this.children.add(index + 1, pn);
        node.layers.setLayerData(ViewLayerType.TURKISH_WORD, "*NONE*");
        node.layers.setLayerData(ViewLayerType.ENGLISH_WORD, "*NONE*");
    }

    public boolean extractVerbal(){
        ArrayList<ParseNodeDrawable> queue = new ArrayList<ParseNodeDrawable>();
        queue.add(this);
        while (queue.size() > 0){
            ParseNodeDrawable nextItem = queue.remove(0);
            if (nextItem.layers != null && nextItem.layers.isVerbal()){
                if (nextItem.layers.isNominal()){
                    nextItem.parent.getParent().setData(new Symbol(verbalLabel));
                }
                else {
                    attachVGToVP(nextItem);
                }
                return true;
            }
            for (ParseNode child:nextItem.children)
                queue.add((ParseNodeDrawable) child);
        }
        return false;
    }

    public void extractTags(ArrayList<String> tagList){
        if (numberOfChildren() != 0){
            tagList.add(getData().getName());
        }
        for (int i = 0; i < numberOfChildren(); i++){
            ((ParseNodeDrawable) getChild(i)).extractTags(tagList);
        }
    }

    public void extractNumberOfChildren(ArrayList<Integer> childNumberList){
        if (numberOfChildren() != 0){
            childNumberList.add(numberOfChildren());
        }
        for (int i = 0; i < numberOfChildren(); i++){
            ((ParseNodeDrawable) getChild(i)).extractNumberOfChildren(childNumberList);
        }
    }

    private void setShallowParseLayer(ChunkType chunkType, String label){
        boolean startWord = true;
        String nodeLabel = "", wordLabel = "";
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector(this, new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        if (sentenceLabels.contains(label))
            label = label.replaceAll(label, "S");
        switch (chunkType){
            case EXISTS:
                label = "";
                break;
            case NORMAL:
                label = label.replaceAll("-.*", "");
                label = "-" + label;
                break;
            case DETAILED:
                label = label.replaceAll("[-=](\\d)+$","");
                if (label.contains("-")){
                    label = label.substring(0, label.indexOf('-') + 4);
                }
                label = "-" + label;
                break;
        }
        for (ParseNodeDrawable node:leafList){
            LayerInfo layers = node.getLayerInfo();
            try{
                for (int i = 0; i < layers.getNumberOfWords(); i++){
                    if (startWord){
                        wordLabel = "B" + label;
                        startWord = false;
                    } else {
                        wordLabel = "I" + label;
                    }
                    if (i == 0){
                        nodeLabel = wordLabel;
                    } else {
                        nodeLabel = nodeLabel + " " + wordLabel;
                    }
                }
                node.getLayerInfo().setLayerData(ViewLayerType.SHALLOW_PARSE, nodeLabel);
            } catch (LayerNotExistsException e) {
                e.printStackTrace();
            }
        }
    }

    public void setShallowParseLayer(ChunkType chunkType){
        String label;
        if (getData() != null && getData().isChunkLabel() && parent != null) {
            if (Word.isPunctuation(getData().getName()))
                label = "PUP";
            else
                label = data.getName();
            setShallowParseLayer(chunkType, label);
        } else {
            for (int i = 0; i < numberOfChildren(); i++)
                ((ParseNodeDrawable) getChild(i)).setShallowParseLayer(chunkType);
        }
    }

    public String toTurkishSentence(){
        if (children.size() == 0){
            if (getLayerData(ViewLayerType.TURKISH_WORD) != null && !getLayerData(ViewLayerType.TURKISH_WORD).equals("*NONE*")){
                return " " + getLayerData(ViewLayerType.TURKISH_WORD).replaceAll("-LRB-", "(").replaceAll("-RRB-", ")").replaceAll("-LSB-", "[").replaceAll("-RSB-", "]").replaceAll("-LCB-", "{").replaceAll("-RCB-", "}").replaceAll("-lrb-", "(").replaceAll("-rrb-", ")").replaceAll("-lsb-", "[").replaceAll("-rsb-", "]").replaceAll("-lcb-", "{").replaceAll("-rcb-", "}");
            } else {
                return " ";
            }
        } else {
            String st = "";
            for (ParseNode aChild : children) {
                st = st + ((ParseNodeDrawable) aChild).toTurkishSentence();
            }
            return st;
        }
    }

    public void checkGazetteer(Gazetteer gazetteer, String word){
        if (gazetteer.contains(word) && getParent().getData().getName().equals("NNP")){
            getLayerInfo().setLayerData(ViewLayerType.NER, gazetteer.getName());
        }
        if (word.contains("'") && gazetteer.contains(word.substring(0, word.indexOf("'"))) && getParent().getData().getName().equals("NNP")){
            getLayerInfo().setLayerData(ViewLayerType.NER, gazetteer.getName());
        }
    }

    public String toString(){
        if (children.size() < 2){
            if (children.size() < 1){
                return getLayerData();
            } else {
                return "(" + data.getName() + " " + children.get(0).toString() + ")";
            }
        } else {
            String st = "(" + data.getName();
            for (ParseNode aChild : children) {
                st = st + " " + aChild.toString();
            }
            return st + ") ";
        }
    }

    public ParseNodeDrawable getLeafWithIndex(int index){
        if (children.size() == 0 && leafIndex == index){
            return this;
        } else {
            for (ParseNode aChildren : children) {
                ParseNodeDrawable aChild = (ParseNodeDrawable) aChildren;
                ParseNodeDrawable result = aChild.getLeafWithIndex(index);
                if (result != null)
                    return result;
            }
            return null;
        }
    }

    public ParseNodeDrawable getNodeAt(int x, int y){
        if (area.contains(x, y))
            return this;
        else {
            for (ParseNode aChildren : children) {
                ParseNodeDrawable aChild = (ParseNodeDrawable) aChildren;
                ParseNodeDrawable result = aChild.getNodeAt(x, y);
                if (result != null){
                    return result;
                }
            }
            return null;
        }
    }

    public ParseNodeDrawable getLeafNodeAt(int x, int y){
        if (area.contains(x, y) && children.size() == 0)
            return this;
        else {
            for (ParseNode aChildren : children) {
                ParseNodeDrawable aChild = (ParseNodeDrawable) aChildren;
                ParseNodeDrawable result = aChild.getLeafNodeAt(x, y);
                if (result != null){
                    return result;
                }
            }
            return null;
        }
    }

    private int getStringSize(Graphics g, ViewLayerType viewLayer){
        int i, stringSize = 0;
        if (children.size() == 0){
            switch (viewLayer){
                case ENGLISH_WORD:
                case TURKISH_WORD:
                case PERSIAN_WORD:
                case NER:
                    stringSize = g.getFontMetrics().stringWidth(layers.getLayerData(viewLayer));
                    break;
                case PROPBANK:
                    stringSize = g.getFontMetrics().stringWidth(layers.getArgument().getArgumentType());
                    break;
                case ENGLISH_SEMANTICS:
                    stringSize = g.getFontMetrics().stringWidth(layers.getLayerData(viewLayer).substring(6, 14));
                    break;
                case SHALLOW_PARSE:
                    try {
                        for (i = 0; i < layers.getNumberOfWords(); i++)
                            if (g.getFontMetrics().stringWidth(layers.getShallowParseAt(i)) > stringSize){
                                stringSize = g.getFontMetrics().stringWidth(layers.getShallowParseAt(i));
                            }
                    } catch (LayerNotExistsException | WordNotExistsException e) {
                        e.printStackTrace();
                    }
                    break;
                case SEMANTICS:
                    try {
                        stringSize = g.getFontMetrics().stringWidth(layers.getLayerData(ViewLayerType.TURKISH_WORD));
                        for (i = 0; i < layers.getNumberOfMeanings(); i++)
                            if (g.getFontMetrics().stringWidth(layers.getSemanticAt(i).substring(6)) > stringSize){
                                stringSize = g.getFontMetrics().stringWidth(layers.getSemanticAt(i).substring(6));
                            }
                    } catch (LayerNotExistsException | WordNotExistsException e) {
                        e.printStackTrace();
                    }
                    break;
                case META_MORPHEME_MOVED:
                case META_MORPHEME:
                case PART_OF_SPEECH:
                case INFLECTIONAL_GROUP:
                case ENGLISH_PROPBANK:
                    for (i = 0; i < layers.getLayerSize(viewLayer); i++)
                        try {
                            if (g.getFontMetrics().stringWidth(layers.getLayerInfoAt(viewLayer, i)) > stringSize){
                                stringSize = g.getFontMetrics().stringWidth(layers.getLayerInfoAt(viewLayer, i));
                            }
                        } catch (LayerNotExistsException | LayerItemNotExistsException | WordNotExistsException e) {
                            e.printStackTrace();
                        }
                    break;
                default:
                    stringSize = g.getFontMetrics().stringWidth(data.getName());
                    break;
            }
            return stringSize;
        } else {
            return g.getFontMetrics().stringWidth(data.getName());
        }
    }

    private void setArea(int x, int y, int stringSize, ViewLayerType viewLayer){
        if (children.size() == 0){
            switch (viewLayer){
                case WORD:
                case TURKISH_WORD:
                case PERSIAN_WORD:
                case ENGLISH_WORD:
                case NER:
                case PROPBANK:
                    area = new Rectangle(x - 5, y - 15, stringSize + 10, 20);
                    break;
                case ENGLISH_SEMANTICS:
                    area = new Rectangle(x - 5, y - 15, stringSize + 10, 40);
                    break;
                case SHALLOW_PARSE:
                case SEMANTICS:
                    try {
                        area = new Rectangle(x - 5, y - 15, stringSize + 10, 20 * (layers.getNumberOfWords() + 1));
                    } catch (LayerNotExistsException e) {
                        e.printStackTrace();
                    }
                    break;
                case META_MORPHEME_MOVED:
                case META_MORPHEME:
                case PART_OF_SPEECH:
                case INFLECTIONAL_GROUP:
                case ENGLISH_PROPBANK:
                    area = new Rectangle(x - 5, y - 15, stringSize + 10, 20 * (layers.getLayerSize(viewLayer) + 1));
                    break;
            }
        } else {
            area = new Rectangle(x - 5, y - 15, stringSize + 10, 20);
        }
    }

    private void drawString(Graphics g, int x, int y, ViewLayerType viewLayer){
        int i;
        if (children.size() == 0){
            switch (viewLayer){
                case WORD:
                    g.drawString(data.getName(), x, y);
                    break;
                case ENGLISH_WORD:
                case TURKISH_WORD:
                case PERSIAN_WORD:
                    g.drawString(layers.getLayerData(viewLayer), x, y);
                    break;
                case ENGLISH_SEMANTICS:
                    g.drawString(layers.getLayerData(ViewLayerType.ENGLISH_WORD), x, y);
                    g.setColor(Color.RED);
                    g.drawString(layers.getLayerData(viewLayer).substring(6, 14), x, y + 20);
                    break;
                case META_MORPHEME_MOVED:
                case META_MORPHEME:
                case PART_OF_SPEECH:
                case INFLECTIONAL_GROUP:
                    for (i = 0; i < layers.getLayerSize(viewLayer); i++){
                        if (i > 0 && !guessed){
                            g.setColor(Color.RED);
                        }
                        try {
                            g.drawString(layers.getLayerInfoAt(viewLayer, i), x, y);
                            y += 20;
                        } catch (LayerNotExistsException | LayerItemNotExistsException | WordNotExistsException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case PROPBANK:
                    g.drawString(layers.getLayerData(ViewLayerType.TURKISH_WORD), x, y);
                    g.setColor(Color.RED);
                    y += 25;
                    g.drawString(layers.getArgument().getArgumentType(), x, y);
                    if (layers.getArgument().getId() != null){
                        Font previousFont = g.getFont();
                        g.setFont(new Font("Serif", Font.PLAIN, 10));
                        g.drawString(layers.getArgument().getId(), x - 15, y + 10);
                        g.setFont(previousFont);
                    }
                    break;
                case ENGLISH_PROPBANK:
                    g.drawString(layers.getLayerData(ViewLayerType.TURKISH_WORD), x, y);
                    g.setColor(Color.RED);
                    y += 25;
                    if (layers.getLayerData(ViewLayerType.PROPBANK) != null){
                        g.drawString(layers.getArgument().getArgumentType(), x, y);
                        if (layers.getArgument().getId() != null){
                            Font previousFont = g.getFont();
                            g.setFont(new Font("Serif", Font.PLAIN, 10));
                            g.drawString(layers.getArgument().getId(), x - 15, y + 10);
                            g.setFont(previousFont);
                        }
                    }
                    y += 25;
                    g.setColor(Color.MAGENTA);
                    g.drawString(layers.getLayerData(ViewLayerType.ENGLISH_WORD), x, y);
                    for (i = 0; i < layers.getLayerSize(viewLayer); i++){
                        g.setColor(Color.RED);
                        try {
                            y += 25;
                            g.drawString(layers.getArgumentAt(i).getArgumentType(), x, y);
                            if (layers.getArgumentAt(i).getId() != null){
                                Font previousFont = g.getFont();
                                g.setFont(new Font("Serif", Font.PLAIN, 10));
                                g.drawString(layers.getArgumentAt(i).getId(), x - 15, y + 10);
                                g.setFont(previousFont);
                            }
                        } catch (LayerNotExistsException | WordNotExistsException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case SHALLOW_PARSE:
                    g.drawString(layers.getLayerData(ViewLayerType.TURKISH_WORD), x, y);
                    g.setColor(Color.RED);
                    try {
                        for (i = 0; i < layers.getNumberOfWords(); i++){
                            try {
                                y += 20;
                                g.drawString(layers.getShallowParseAt(i), x, y);
                            } catch (LayerNotExistsException | WordNotExistsException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (LayerNotExistsException e) {
                        e.printStackTrace();
                    }
                    break;
                case SEMANTICS:
                    g.drawString(layers.getLayerData(ViewLayerType.TURKISH_WORD), x, y);
                    g.setColor(Color.RED);
                    for (i = 0; i < layers.getNumberOfMeanings(); i++){
                        try {
                            y += 20;
                            g.drawString(layers.getSemanticAt(i).substring(6), x, y);
                        } catch (LayerNotExistsException | WordNotExistsException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case NER:
                    g.drawString(layers.getLayerData(ViewLayerType.TURKISH_WORD), x, y);
                    g.setColor(Color.RED);
                    g.drawString(layers.getLayerData(ViewLayerType.NER), x, y + 20);
                    break;
            }
        } else {
            g.drawString(data.getName(), x, y);
        }
    }

    public void drawDependency(Graphics g, ParseTreeDrawable tree){
        ParseNodeDrawable toNode;
        int toIG;
        String dependency;
        int startX, startY, dragX, dragY;
        Point2D.Double pointCtrl1, pointCtrl2, pointStart, pointEnd;
        CubicCurve2D.Double cubicCurve;
        if (children.size() == 0 && layers != null && layers.getLayerData(ViewLayerType.DEPENDENCY) != null){
            String[] words = layers.getLayerData(ViewLayerType.DEPENDENCY).split(",");
            toNode = tree.getLeafWithIndex(Integer.parseInt(words[0]));
            toIG = Integer.parseInt(words[1]);
            dependency = words[2];
            startX = area.x + area.width / 2;
            startY = area.y + 20;
            dragX = toNode.area.x + toNode.area.width / 2;
            dragY = toNode.area.y + 20 * toIG;
            pointStart = new Point2D.Double(startX, startY);
            pointEnd = new Point2D.Double(dragX, dragY);
            if (dragY > startY){
                pointCtrl1 = new Point2D.Double(startX, (startY + dragY) / 2 + 40);
                pointCtrl2 = new Point2D.Double((startX + dragX) / 2, dragY + 50);
            } else {
                pointCtrl1 = new Point2D.Double((startX + dragX) / 2, startY + 30);
                pointCtrl2 = new Point2D.Double(dragX, (startY + dragY) / 2 + 40);
            }
            cubicCurve = new CubicCurve2D.Double(pointStart.x, pointStart.y, pointCtrl1.x, pointCtrl1.y, pointCtrl2.x, pointCtrl2.y, pointEnd.x, pointEnd.y);
            Graphics2D g2 = (Graphics2D)g;
            g2.setColor(Color.RED);
            g.drawString(dependency, (startX + dragX) / 2, Math.max(startY, dragY) + 50);
            g2.draw(cubicCurve);
            g2.setColor(Color.BLACK);
        } else {
            for (ParseNode aChildren : children) {
                ParseNodeDrawable aChild = (ParseNodeDrawable) aChildren;
                aChild.drawDependency(g, tree);
            }
        }
    }

    private String toSvgText(int x, int y, String color, String text, int fontSize){
        return "<text x=\"" + x + "\" y=\"" + y + "\" font-size = \"" + fontSize + "\" fill=\"" + color + "\">" + text + "</text>\n";
    }

    private String toSvgText(int x, int y, String color, String text){
        return "<text x=\"" + x + "\" y=\"" + y + "\" fill=\"" + color + "\">" + text + "</text>\n";
    }

    public String toSvgFormat(ViewLayerType viewLayer){
        String result;
        Graphics g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).getGraphics();
        int i, x, y, stringSize = getStringSize(g, viewLayer), width;
        if (viewLayer.equals(ViewLayerType.ENGLISH_PROPBANK)){
            width = 100;
        } else {
            width = 70;
        }
        x = (inOrderTraversalIndex + 1) * width - stringSize / 2;
        y = depth * 80 + 15;
        setArea(x, y, stringSize, viewLayer);
        if (children.size() == 0){
            switch (viewLayer){
                case WORD:
                    return toSvgText(x, y, "blue", data.getName());
                case ENGLISH_WORD:
                case TURKISH_WORD:
                case PERSIAN_WORD:
                    return toSvgText(x, y, "blue", layers.getLayerData(viewLayer));
                case ENGLISH_SEMANTICS:
                    return toSvgText(x, y, "blue", layers.getLayerData(viewLayer).substring(6, 14));
                case META_MORPHEME_MOVED:
                case META_MORPHEME:
                case PART_OF_SPEECH:
                case INFLECTIONAL_GROUP:
                    result = "";
                    for (i = 0; i < layers.getLayerSize(viewLayer); i++){
                        try {
                            if (layers.getLayerInfoAt(viewLayer, i).length() > 1 && layers.getLayerInfoAt(viewLayer, i).toUpperCase(new Locale("tr")).equals(layers.getLayerInfoAt(viewLayer, i))){
                                result = result + toSvgText(x, y, "red", layers.getLayerInfoAt(viewLayer, i));
                            } else {
                                result = result + toSvgText(x, y, "blue", layers.getLayerInfoAt(viewLayer, i));
                            }
                        } catch (LayerNotExistsException | LayerItemNotExistsException | WordNotExistsException e) {
                            e.printStackTrace();
                        }
                        y += 20;
                    }
                    return result;
                case ENGLISH_PROPBANK:
                    result = toSvgText(x, y, "blue", layers.getLayerData(ViewLayerType.TURKISH_WORD));
                    y += 25;
                    if (layers.getLayerData(ViewLayerType.PROPBANK) != null){
                        result = result + toSvgText(x, y, "red", layers.getLayerData(ViewLayerType.PROPBANK));
                    }
                    y += 25;
                    result = result + toSvgText(x, y, "magenta", layers.getLayerData(ViewLayerType.ENGLISH_WORD));
                    for (i = 0; i < layers.getLayerSize(viewLayer); i++){
                        try {
                            y += 25;
                            result = result + toSvgText(x, y, "red", layers.getArgumentAt(i).getArgumentType());
                            if (layers.getArgumentAt(i).getId() != null){
                                result = result + toSvgText(x - 15, y + 10, "red", layers.getArgumentAt(i).getId(), 10);
                            }
                        } catch (LayerNotExistsException | WordNotExistsException e) {
                            e.printStackTrace();
                        }
                    }
                    return result;
                case SHALLOW_PARSE:
                    result = toSvgText(x, y, "blue", layers.getLayerData(ViewLayerType.TURKISH_WORD));
                    try {
                        for (i = 0; i < layers.getNumberOfWords(); i++){
                            try {
                                y += 20;
                                result = result + toSvgText(x, y, "red", layers.getShallowParseAt(i));
                            } catch (LayerNotExistsException | WordNotExistsException e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (LayerNotExistsException e) {
                        e.printStackTrace();
                    }
                    return result;
                case SEMANTICS:
                    result = toSvgText(x, y, "blue", layers.getLayerData(ViewLayerType.TURKISH_WORD));
                    for (i = 0; i < layers.getNumberOfMeanings(); i++){
                        try {
                            y += 20;
                            result = result + toSvgText(x, y, "red", layers.getSemanticAt(i).substring(6));
                        } catch (LayerNotExistsException | WordNotExistsException e) {
                            e.printStackTrace();
                        }
                    }
                    return result;
                case NER:
                    result = toSvgText(x, y, "blue", layers.getLayerData(ViewLayerType.TURKISH_WORD));
                    return result + toSvgText(x, y + 20, "red", layers.getLayerData(ViewLayerType.NER));
                case PROPBANK:
                    result = toSvgText(x, y, "blue", layers.getLayerData(ViewLayerType.TURKISH_WORD));
                    return result + toSvgText(x, y + 20, "red", layers.getLayerData(ViewLayerType.PROPBANK));
            }
        } else {
            if (parent == null){
                result = toSvgText(x, y, "black", data.getName());
            } else {
                result = toSvgText(x, y - 10, "black", data.getName());
            }
            for (ParseNode aChildren : children) {
                ParseNodeDrawable aChild = (ParseNodeDrawable) aChildren;
                int x1 = (inOrderTraversalIndex + 1) * width;
                int y1 = depth * 80 + 20;
                int x2 = (aChild.inOrderTraversalIndex + 1) * width;
                int y2 = aChild.depth * 80 - 20;
                result = result + "<line x1=\"" + x1 + "\" y1=\"" + y1 + "\" x2=\"" + x2 + "\" y2=\"" + y2 + "\" style=\"stroke:rgb(0,0,0);stroke-width:2\"/>\n";
            }
            for (ParseNode aChildren : children) {
                ParseNodeDrawable aChild = (ParseNodeDrawable) aChildren;
                result = result + aChild.toSvgFormat(viewLayer);
            }
            return result;
        }
        return null;
    }

    public void paint(Graphics g, int nodeWidth, int nodeHeight, int maxDepth, ViewLayerType viewLayer){
        int stringSize, addY, x, y;
        ViewLayerType originalLayer = viewLayer;
        if (children.size() == 0 && viewLayer != ViewLayerType.WORD){
            viewLayer = layers.checkLayer(viewLayer);
        }
        stringSize = getStringSize(g, viewLayer);
        if (depth == 0){
            addY = 15;
        } else {
            if (depth == maxDepth){
                addY = -5;
            } else {
                addY = 5;
            }
        }
        x = (inOrderTraversalIndex + 1) * nodeWidth - stringSize / 2;
        y = depth * nodeHeight + addY;
        setArea(x, y, stringSize, viewLayer);
        if (searched){
            g.setColor(Color.BLUE);
            g.draw3DRect(x - 5, y - 15, stringSize + 10, 20, true);
            g.setColor(Color.BLACK);
        } else {
            if (editable){
                g.setColor(Color.RED);
                g.drawRect(x - 5, y - 15, stringSize + 10, 20);
                g.setColor(Color.BLACK);
            } else {
                if (dragged){
                    g.setColor(Color.MAGENTA);
                    if (selectedIndex == -1)
                        g.drawRect(x - 5, y - 15, stringSize + 10, 20);
                    else {
                        if (originalLayer != ViewLayerType.TURKISH_WORD){
                            g.drawRect(x - 5, y - 15 + 20 * selectedIndex, stringSize + 10, 20);
                        } else {
                            g.drawRect(x - 5 + selectedIndex * (stringSize + 10) / (numberOfChildren() + 1), y - 15, (stringSize + 10) / (numberOfChildren() + 1), 20);
                        }
                    }
                    g.setColor(Color.BLACK);
                } else {
                    if (selected){
                        if (selectedIndex == -1)
                            g.drawRect(x - 5, y - 15, stringSize + 10, 20);
                        else
                            g.drawRect(x - 5, y - 15 + 20 * selectedIndex, stringSize + 10, 20);
                    }
                }
            }
        }
        if (children.size() == 0){
            if (guessed){
                g.setColor(Color.MAGENTA);
            } else {
                if (originalLayer != viewLayer && (originalLayer == ViewLayerType.TURKISH_WORD || originalLayer == ViewLayerType.PERSIAN_WORD)){
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.BLUE);
                }
            }
        } else {
            if (parent != null && this == parent.headChild()){
                g.setColor(Color.GRAY);
            }
        }
        drawString(g, x, y, viewLayer);
        g.setColor(Color.BLACK);
        for (ParseNode aChildren : children) {
            ParseNodeDrawable aChild = (ParseNodeDrawable) aChildren;
            g.drawLine((inOrderTraversalIndex + 1) * nodeWidth, depth * nodeHeight + 20, (aChild.inOrderTraversalIndex + 1) * nodeWidth, aChild.depth * nodeHeight - 20);
        }
        for (ParseNode aChildren : children) {
            ParseNodeDrawable aChild = (ParseNodeDrawable) aChildren;
            aChild.paint(g, nodeWidth, nodeHeight, maxDepth, viewLayer);
        }
    }

}
