package AnnotatedTree;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedSentence.ViewLayerType;
import MorphologicalAnalysis.MorphologicalParse;
import ParseTree.ParseNode;
import ParseTree.Symbol;
import Dictionary.EnglishWordComparator;
import AnnotatedTree.Processor.Condition.IsTurkishLeafNode;
import AnnotatedTree.Processor.NodeDrawableCollector;
import Dictionary.*;
import NamedEntityRecognition.Gazetteer;
import Util.Permutation;
import Util.RectAngle;

import java.util.*;

public class ParseNodeDrawable extends ParseNode {

    protected LayerInfo layers = null;
    protected int inOrderTraversalIndex;
    protected int leafIndex = -1;
    protected int depth;
    protected RectAngle area;
    protected boolean selected = false;
    protected boolean editable = false;
    protected boolean dragged = false;
    protected boolean searched = false;
    protected int selectedIndex = -1;
    private static final String verbalLabel = "VG";
    private boolean hasDeletedChild = false;
    private boolean guessed = false;
    private static final ArrayList<String> sentenceLabels = new ArrayList<>(Arrays.asList("SINV", "SBARQ", "SBAR", "SQ", "S"));

    /**
     * Constructs a ParseNodeDrawable from a single line. If the node is a leaf node, it only sets the data. Otherwise,
     * splits the line w.r.t. spaces and parenthesis and calls itself recursively to generate its child parseNodes.
     * @param parent The parent node of this node.
     * @param line The input line to create this parseNode.
     * @param isLeaf True, if this node is a leaf node; false otherwise.
     * @param depth Depth of the node.
     */
    public ParseNodeDrawable(ParseNodeDrawable parent, String line, boolean isLeaf, int depth) throws ParenthesisInLayerException {
        int parenthesisCount = 0;
        StringBuilder childLine = new StringBuilder();
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
                        childLine.append(line.charAt(i));
                    }
                    if (line.charAt(i) == '('){
                        parenthesisCount++;
                    } else {
                        if (line.charAt(i) == ')'){
                            parenthesisCount--;
                        }
                    }
                    if (parenthesisCount == 0 && (childLine.length() > 0)){
                        children.add(new ParseNodeDrawable(this, childLine.toString().trim(), false, depth + 1));
                        childLine = new StringBuilder();
                    }
                }
            }
        }
    }

    /**
     * Another simple constructor for ParseNode. It only takes input the data, and sets it.
     * @param data Data for this node.
     */
    public ParseNodeDrawable(Symbol data){
        super(data);
    }

    /**
     * Another constructor for ParseNodeDrawable. Sets the parent to the given parent, and adds given child as a
     * single child, and sets the given symbol as data.
     * @param parent Parent of this node.
     * @param child Single child of this node.
     * @param symbol Symbol of this node.
     */
    public ParseNodeDrawable(ParseNodeDrawable parent, ParseNodeDrawable child, String symbol){
        this.children = new ArrayList<>();
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
        result.children = new ArrayList<>();
        if (layers != null)
            result.layers = layers.clone();
        return result;
    }

    /**
     * Accessor for layers attribute
     * @return Layers attribute
     */
    public LayerInfo getLayerInfo(){
        return layers;
    }

    /**
     * Returns the data. Either the node is a leaf node, in which case English word layer is returned; or the node is
     * a nonleaf node, in which case the node tag is returned.
     * @return English word for leaf node, constituency tag for non-leaf node.
     */
    public Symbol getData(){
        if (layers == null){
            return super.getData();
        } else {
            return new Symbol(getLayerData(ViewLayerType.ENGLISH_WORD));
        }
    }

    /**
     * Clears the layers hash map.
     */
    public void clearLayers(){
        layers = new LayerInfo();
    }

    /**
     * Recursive method to clear a given layer.
     * @param layerType Name of the layer to be cleared
     */
    public void clearLayer(ViewLayerType layerType){
        if (children.isEmpty() && layerExists(layerType)){
            layers.removeLayer(layerType);
        }
        for (int i = 0; i < numberOfChildren(); i++){
            ((ParseNodeDrawable) children.get(i)).clearLayer(layerType);
        }
    }

    /**
     * Clears the node tag.
     */
    public void clearData(){
        data = null;
    }

    /**
     * Setter for the data attribute and also clears all layers.
     * @param data New data field.
     */
    public void setDataAndClearLayers(Symbol data){
        super.setData(data);
        layers = null;
    }

    /**
     * Accessor for dragged attribute
     * @return Dragged attribute.
     */
    public boolean isDragged(){
        return dragged;
    }

    /**
     * Accessor for editable attribute
     * @return Editable attribute.
     */
    public boolean isEditable(){
        return editable;
    }

    /**
     * Accessor for searched attribute
     * @return Searched attribute.
     */
    public boolean isSearched(){
        return searched;
    }

    /**
     * Accessor for guessed attribute
     * @return Guessed attribute.
     */
    public boolean isGuessed(){
        return guessed;
    }

    /**
     * Accessor for selected attribute
     * @return Selected attribute.
     */
    public boolean isSelected(){
        return selected;
    }

    /**
     * Accessor for inOrderTraversalIndex attribute
     * @return InOrderTraversalIndex attribute.
     */
    public int getInOrderTraversalIndex(){
        return inOrderTraversalIndex;
    }

    /**
     * Mutator for the guessed attribute. It sets to true.
     */
    public void setGuessed(){
        guessed = true;
    }

    /**
     * Mutator for the data field. If the layers is null, its sets the data field, otherwise it sets the English layer
     * to the given value.
     * @param data Data to be set.
     */
    public void setData(Symbol data){
        if (layers == null){
            super.setData(data);
        } else {
            layers.setLayerData(ViewLayerType.ENGLISH_WORD, data.getName());
        }
    }

    /**
     * Returns the layer value of the head child of this node.
     * @param viewLayerType Layer name
     * @return Layer value of the head child of this node.
     */
    public String headWord(ViewLayerType viewLayerType){
        if (!children.isEmpty()){
            return ((ParseNodeDrawable) headChild()).headWord(viewLayerType);
        } else {
            return getLayerData(viewLayerType);
        }
    }

    /**
     * Accessor for the data or layers attribute.
     * @return If data is not null, this node is a non-leaf node, it returns the data field. Otherwise, this node is a
     * leaf node, it returns the layer description.
     */
    public String getLayerData(){
        if (data != null){
            return data.getName();
        }
        return layers.getLayerDescription();
    }

    /**
     * Returns the layer value of a given layer.
     * @param viewLayer Layer name
     * @return Value of the given layer
     */
    public String getLayerData(ViewLayerType viewLayer){
        if (viewLayer == ViewLayerType.WORD || layers == null){
            return data.getName();
        }
        return layers.getLayerData(viewLayer);
    }

    /**
     * Accessor for the leafIndex attribute
     * @return LeafIndex attribute
     */
    public int getLeafIndex(){
        return leafIndex;
    }

    /**
     * Accessor for the depth attribute
     * @return Depth attribute
     */
    public int getDepth(){
        return depth;
    }

    /*
     * Recursive setter method for the leafIndex attribute. LeafIndex shows the index of the leaf node according to the
     * inorder traversal without considering non-leaf nodes.
     * @param pos Current leaf index
     * @return Updated leaf index
     */
    public int leafTraversal(int pos){
        int i;
        if (children.isEmpty()){
            pos++;
            leafIndex = pos;
        }
        for (i = 0; i < children.size(); i++){
            pos = ((ParseNodeDrawable) children.get(i)).leafTraversal(pos);
        }
        return pos;
    }

    /**
     * Recursive setter method for the inOrderTraversalIndex attribute. InOrderTraversalIndex shows the index of the
     * node according to the inorder traversal.
     * @param pos Current inorder traversal index
     * @return Update inorder traversal index
     */
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

    /**
     * Returns the maximum inorder traversal index considering this node and all of its descendants.
     * @return The maximum inorder traversal index considering this node and all of its descendants.
     */
    public int maxInOrderTraversal(){
        int maxIndex, childIndex;
        if (children.isEmpty())
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

    /**
     * Returns the number of structural agreement between this node and the given node recursively. Two nodes agree in
     * structural manner if they have the same number of children and all of their children have the same tags in the
     * same order.
     * @param parseNode Parse node to compare in structural manner
     * @return The number of structural agreement between this node and the given node recursively.
     */
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

    /**
     * Returns the number of gloss agreements between this node and the given node recursively. Two nodes agree in
     * glosses if they are both leaf nodes and their layer info are the same.
     * @param parseNode Parse node to compare in gloss manner
     * @param viewLayerType Layer name to compare
     * @return The number of gloss agreements between this node and the given node recursively.
     */
    public int glossAgreementCount(ParseNodeDrawable parseNode, ViewLayerType viewLayerType){
        if (children.isEmpty()){
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

    /**
     * Replaces a given old child with the given new child.
     * @param oldChild Old child to be replaced
     * @param newChild New child which replaces old child
     */
    public void replaceChild(ParseNodeDrawable oldChild, ParseNodeDrawable newChild){
        newChild.updateDepths(this.depth + 1);
        newChild.parent = this;
        children.set(children.indexOf(oldChild), newChild);
    }

    /**
     * Recursive method which updates the depth attribute
     * @param depth Current depth to set.
     */
    public void updateDepths(int depth){
        this.depth = depth;
        for (ParseNode aChildren:children){
            ParseNodeDrawable aChild = (ParseNodeDrawable) aChildren;
            aChild.updateDepths(depth + 1);
        }
    }

    /**
     * Calculates the maximum depth of the subtree rooted from this node.
     * @return The maximum depth of the subtree rooted from this node.
     */
    public int maxDepth(){
        int depth = this.depth;
        for (ParseNode aChildren : children) {
            ParseNodeDrawable aChild = (ParseNodeDrawable) aChildren;
            if (aChild.maxDepth() > depth)
                depth = aChild.maxDepth();
        }
        return depth;
    }

    /**
     * Accessor for the selectedIndex attribute.
     * @return SelectedIndex attribute
     */
    public int getSelectedIndex(){
        return selectedIndex;
    }

    /**
     * Accessor for the area attribute.
     * @return Area attribute.
     */
    public RectAngle getArea(){
        return area;
    }

    /**
     * Mutator for the selected attribute.
     * @param selected New selected value
     */
    public void setSelected(boolean selected){
        this.selected = selected;
        selectedIndex = -1;
    }

    /**
     * Mutator for the selected and selectedIndex attributes.
     * @param selected New selected value
     * @param selectedIndex New selectedIndex value
     */
    public void setSelected(boolean selected, int selectedIndex){
        this.selected = selected;
        this.selectedIndex = selectedIndex;
    }

    /**
     * Mutator for the editable attribute.
     * @param editable New editable attribute.
     */
    public void setEditable(boolean editable){
        this.editable = editable;
    }

    /**
     * Mutator for the searched attribute.
     * @param searched New searched attribute.
     */
    public void setSearched(boolean searched){
        this.searched = searched;
    }

    /**
     * Mutator for the dragged attribute.
     * @param dragged New dragged attribute.
     */
    public void setDragged(boolean dragged){
        this.dragged = dragged;
    }

    /**
     * Mutator for the dragged and selectedIndex attributes.
     * @param dragged New dragged attribute.
     * @param selectedIndex New selectedIndex attribute.
     */
    public void setDragged(boolean dragged, int selectedIndex){
        this.dragged = dragged;
        this.selectedIndex = selectedIndex;
    }

    /**
     * Mutator for the hasDeletedChild attribute. Sets it to true.
     */
    public void setChildDeleted(){
        hasDeletedChild = true;
    }

    /**
     * Accessor for the hasDeletedChild attribute
     * @return hasDeletedChild attribute.
     */
    public boolean hasDeletedChild(){
        return hasDeletedChild;
    }

    /**
     * Recursive method that checks if the current node satisfies the conditions in the given search node.
     * @param node Node containing the search condition
     * @return True if the node satisfies the condition, false otherwise.
     */
    public boolean satisfy(ParseNodeSearchable node){
        int i;
        if (node.isLeaf() && !children.isEmpty())
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

    /**
     * Recursive method that updates all pos tags in the leaf nodes according to the morphological tag in those leaf
     * nodes.
     */
    public void updatePosTags(){
        if (children.size() == 1 && children.get(0).isLeaf() && !children.get(0).isDummyNode()){
            LayerInfo layerInfo = ((ParseNodeDrawable)children.get(0)).getLayerInfo();
            try {
                MorphologicalParse morphologicalParse = layerInfo.getMorphologicalParseAt(layerInfo.getNumberOfWords() - 1);
                String symbol = morphologicalParse.getTreePos();
                setData(new Symbol(symbol));
            } catch (LayerNotExistsException | WordNotExistsException ignored) {
            }
        } else {
            for (ParseNode aChildren:children){
                ParseNodeDrawable aChild = (ParseNodeDrawable) aChildren;
                aChild.updatePosTags();
            }
        }
    }

    /**
     * Recursive method that calculates the score of this node compared to the given correctNode. If the children of
     * this node are the same as the children of the correctNode (also in the same order), then the score is 1,
     * otherwise the score is 0. The total score is the sum of all scores of all descendant nodes of the current node.
     * @param correctNode Node to be compared with this node.
     * @return Number of nodes matched in the subtree rooted with this node.
     */
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

    /**
     * Returns all symbols in this node.
     * @return All symbols in the children of this node.
     */
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
        childrenSymbols.sort(new EnglishWordComparator());
        if (!childrenSymbols.isEmpty()){
            if (layers != null){
                layers.setLayerData(ViewLayerType.ENGLISH_WORD, layers.getLayerData(ViewLayerType.ENGLISH_WORD) + childrenSymbols);
            } else {
                data = new Symbol(data.getName() + childrenSymbols);
            }
        }
    }

    /**
     * Recursive method that returns the concatenation of all pos tags of all descendants of this node.
     * @return The concatenation of all pos tags of all descendants of this node.
     */
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

    /**
     * Checks if the children of the current node is a permutation of the children of the given thatParseNode.
     * @param thatParseNode Parse node to be compared.
     * @return True if the children of the current node is a permutation of the children of the given thatParseNode,
     * false otherwise.
     */
    public boolean isPermutation(ParseNodeDrawable thatParseNode){
        boolean b;
        this.augment();
        thatParseNode.augment();
        b = getData().equals(thatParseNode.getData());
        deAugment();
        thatParseNode.deAugment();
        return b;
    }

    /**
     * Recursive method that checks if all nodes in the subtree rooted with this node has the annotation in the given
     * layer.
     * @param viewLayerType Layer name
     * @return True if all nodes in the subtree rooted with this node has the annotation in the given layer, false
     * otherwise.
     */
    public boolean layerExists(ViewLayerType viewLayerType){
        if (children.isEmpty()){
            return getLayerData(viewLayerType) != null;
        } else {
            for (ParseNode aChild : children) {
                if (((ParseNodeDrawable)aChild).layerExists(viewLayerType)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the current node is a dummy node or not. A node is a dummy node if its data contains '*', or its
     * data is '0' and its parent is '-NONE-'.
     * @return True if the current node is a dummy node, false otherwise.
     */
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
        if (!this.children.isEmpty()){
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

    /**
     * Checks if all nodes in the subtree rooted with this node has annotation with the given layer.
     * @param viewLayerType Layer name
     * @return True if all nodes in the subtree rooted with this node has annotation with the given layer, false
     * otherwise.
     */
    public boolean layerAll(ViewLayerType viewLayerType){
        if (children.isEmpty()){
            return getLayerData(viewLayerType) != null || isDummyNode();
        } else {
            for (ParseNode aChild : children) {
                if (!((ParseNodeDrawable)aChild).layerAll(viewLayerType)){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Recursive method that returns all nodes in the subtree rooted with this node those satisfy the conditions in the
     * given tree.
     * @param tree Tree containing the search condition
     * @return All nodes in the subtree rooted with this node those satisfy the conditions in the given tree.
     */
    public ArrayList<ParseNodeDrawable> satisfy(ParseTreeSearchable tree){
        ArrayList<ParseNodeDrawable> result = new ArrayList<>();
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
        ArrayList<ParseNodeDrawable> queue = new ArrayList<>();
        queue.add(this);
        while (!queue.isEmpty()){
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

    /**
     * Recursive method that accumulates all tag symbols in the descendants of this node in the tagList.
     * @param tagList Array list of strings to store the tag symbols.
     */
    public void extractTags(ArrayList<String> tagList){
        if (numberOfChildren() != 0){
            tagList.add(getData().getName());
        }
        for (int i = 0; i < numberOfChildren(); i++){
            ((ParseNodeDrawable) getChild(i)).extractTags(tagList);
        }
    }

    /**
     * Recursive method that accumulates number of children of all descendants of this node in the childNumberList.
     * @param childNumberList Array of list to store the number of children
     */
    public void extractNumberOfChildren(ArrayList<Integer> childNumberList){
        if (numberOfChildren() != 0){
            childNumberList.add(numberOfChildren());
        }
        for (int i = 0; i < numberOfChildren(); i++){
            ((ParseNodeDrawable) getChild(i)).extractNumberOfChildren(childNumberList);
        }
    }

    /**
     * Sets the shallow parse layer of all leaf nodes in the subtree rooted with this node to the given label according
     * to the given chunking type.
     * @param chunkType Type of the chunking used to annotate.
     * @param label Shallow parse label for the leaf nodes.
     */
    private void setShallowParseLayer(ChunkType chunkType, String label){
        boolean startWord = true;
        String nodeLabel = "", wordLabel;
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
            } catch (LayerNotExistsException ignored) {
            }
        }
    }

    /**
     * Recursive method that sets the shallow parse layer of all leaf nodes in the subtree rooted with this node
     * according to the given chunking type.
     * @param chunkType Type of the chunking used to annotate.
     */
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

    /**
     * Recursive method to convert the subtree rooted with this node to a string. All parenthesis types are converted to
     * their regular forms.
     * @return String version of the subtree rooted with this node.
     */
    public String toTurkishSentence(){
        if (children.isEmpty()){
            if (getLayerData(ViewLayerType.TURKISH_WORD) != null && !getLayerData(ViewLayerType.TURKISH_WORD).equals("*NONE*")){
                return " " + getLayerData(ViewLayerType.TURKISH_WORD)
                        .replaceAll("-LRB-", "(")
                        .replaceAll("-RRB-", ")")
                        .replaceAll("-LSB-", "[")
                        .replaceAll("-RSB-", "]")
                        .replaceAll("-LCB-", "{")
                        .replaceAll("-RCB-", "}")
                        .replaceAll("-lrb-", "(")
                        .replaceAll("-rrb-", ")")
                        .replaceAll("-lsb-", "[")
                        .replaceAll("-rsb-", "]")
                        .replaceAll("-lcb-", "{")
                        .replaceAll("-rcb-", "}");
            } else {
                return " ";
            }
        } else {
            StringBuilder st = new StringBuilder();
            for (ParseNode aChild : children) {
                st.append(((ParseNodeDrawable) aChild).toTurkishSentence());
            }
            return st.toString();
        }
    }

    /**
     * Sets the NER layer according to the tag of the parent node and the word in the node. The word is searched in the
     * gazetteer, if it exists, the NER info is replaced with the NER tag in the gazetter.
     * @param gazetteer Gazetteer where we search the word
     * @param word Word to be searched in the gazetteer
     */
    public void checkGazetteer(Gazetteer gazetteer, String word){
        if (gazetteer.contains(word) && getParent().getData().getName().equals("NNP")){
            getLayerInfo().setLayerData(ViewLayerType.NER, gazetteer.getName());
        }
        if (word.contains("'") && gazetteer.contains(word.substring(0, word.indexOf("'"))) && getParent().getData().getName().equals("NNP")){
            getLayerInfo().setLayerData(ViewLayerType.NER, gazetteer.getName());
        }
    }

    /**
     * Recursive method that sets the tag information of the given parse node with all descendants with respect to the
     * morphological annotation of the current node with all descendants.
     * @param parseNode Parse node whose tag information will be changed.
     * @param surfaceForm If true, tag will be replaced with the surface form annotation.
     */
    public void generateParseNode(ParseNode parseNode, boolean surfaceForm){
        if (numberOfChildren() == 0){
            if (surfaceForm){
                parseNode.setData(new Symbol(getLayerData(ViewLayerType.TURKISH_WORD)));
            } else {
                try{
                    parseNode.setData(new Symbol(getLayerInfo().getMorphologicalParseAt(0).getWord().getName()));
                } catch (LayerNotExistsException | WordNotExistsException ignored) {
                }
            }
        } else {
            parseNode.setData(data);
            for (int i = 0; i < numberOfChildren(); i++){
                ParseNode newChild = new ParseNode();
                parseNode.addChild(newChild);
                ((ParseNodeDrawable) children.get(i)).generateParseNode(newChild, surfaceForm);
            }
        }
    }

    /**
     * Recursive method to convert the subtree rooted with this node to a string.
     * @return String version of the subtree rooted with this node.
     */
    public String toString(){
        if (children.size() < 2){
            if (children.isEmpty()){
                return getLayerData();
            } else {
                return "(" + data.getName() + " " + children.get(0).toString() + ")";
            }
        } else {
            StringBuilder st = new StringBuilder("(" + data.getName());
            for (ParseNode aChild : children) {
                st.append(" ").append(aChild.toString());
            }
            return st + ") ";
        }
    }

    /**
     * Returns the leaf node at position index in the subtree rooted with this node.
     * @param index Position of the leaf node.
     * @return The leaf node at position index in the subtree rooted with this node.
     */
    public ParseNodeDrawable getLeafWithIndex(int index){
        if (children.isEmpty() && leafIndex == index){
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

    /**
     * Returns the index of the layer data in the given x and y coordinates in the panel that displays the annotated
     * tree.
     * @param x x coordinate
     * @param y y coordinate
     * @return Index of the layer data in the given x and y coordinates in the panel that displays the annotated tree.
     */
    public int getSubItemAt(int x, int y){
        if (area.contains(x, y) && children.isEmpty())
            return (y - area.getY()) / 20;
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

    /**
     * Returns the parse node in the given x and y coordinates in the panel that displays the annotated tree.
     * @param x x coordinate
     * @param y y coordinate
     * @return The parse node in the given x and y coordinates in the panel that displays the annotated tree.
     */
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

    /**
     * Returns the leaf node in the given x and y coordinates in the panel that displays the annotated tree.
     * @param x x coordinate
     * @param y y coordinate
     * @return The leaf node in the given x and y coordinates in the panel that displays the annotated tree.
     */
    public ParseNodeDrawable getLeafNodeAt(int x, int y){
        if (area.contains(x, y) && children.isEmpty())
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

    /**
     * Mutator for the area attribute
     * @param x New x coordinate of the area
     * @param y New y coordinate of the area
     * @param width New width of the area
     * @param height New height of the area
     */
    public void setArea(int x, int y, int width, int height){
        this.area = new RectAngle(x, y, width, height);
    }

}
