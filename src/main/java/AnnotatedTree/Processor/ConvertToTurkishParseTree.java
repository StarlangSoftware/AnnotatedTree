package AnnotatedTree.Processor;

import AnnotatedSentence.ViewLayerType;
import ParseTree.ParseNode;
import AnnotatedTree.*;

import java.util.Iterator;

public class ConvertToTurkishParseTree {
    private final ParseTreeDrawable parseTree;

    /**
     * Searches recursively all descendants of the given parse node for NONE leaf nodes. If a NONE node is found, it is
     * deleted.
     * @param parseNode Parse node for which all descendants will be checked for NONE.
     */
    private void searchNONE(ParseNodeDrawable parseNode) {
        boolean isDeleted;
        Iterator<ParseNode> childIterator = parseNode.getChildIterator();
        while (childIterator.hasNext()) {
            isDeleted = false;
            ParseNodeDrawable child = (ParseNodeDrawable) childIterator.next();
            for (int i = 0; i < child.numberOfChildren(); i++) {
                ParseNodeDrawable grandChild = (ParseNodeDrawable) child.getChild(i);
                if (grandChild.getLayerInfo() != null && grandChild.getLayerData(ViewLayerType.TURKISH_WORD) != null) {
                    if ((grandChild.getLayerData(ViewLayerType.TURKISH_WORD)).contains("*")) {
                        childIterator.remove();
                        isDeleted = true;
                        parseNode.setChildDeleted();
                    }
                }
            }
            if (!isDeleted) {
                searchNONE(child);
            }
        }
    }

    /**
     * Recursive method that searches if any descendants of the given parse node is a terminal leaf node.
     * @param parseNode Parse node to be checked.
     * @return True if any descendant of the given parse node is a terminal leaf node, false otherwise.
     */
    private boolean hasTerminal(ParseNodeDrawable parseNode) {
        boolean result = false;
        if (parseNode.getLayerInfo() == null) {
            if (parseNode.getData().isTerminal()) {
                return true;
            } else {
                for (int i = 0; i < parseNode.numberOfChildren(); i++) {
                    ParseNodeDrawable child = (ParseNodeDrawable)parseNode.getChild(i);
                    if (child.getLayerInfo() == null) {
                        if (child.getData().isTerminal()) {
                            return true;
                        } else {
                            result = hasTerminal(child) || result;
                        }
                    } else {
                        return true;
                    }
                }
            }
        } else {
            return true;
        }
        return result;
    }

    /**
     * Recursive method that deletes all non-terminal leaf descendants of the given node.
     * @param parseNode PArse node to be checked
     */
    private void deleteLeafNonTerminals(ParseNodeDrawable parseNode) {
        ParseNodeDrawable currentNode;
        Iterator<ParseNode> childIterator = parseNode.getChildIterator();
        while (childIterator.hasNext()) {
            currentNode = (ParseNodeDrawable) childIterator.next();
            if (!hasTerminal(currentNode)) {
                childIterator.remove();
                parseNode.setChildDeleted();
            } else {
                deleteLeafNonTerminals(currentNode);
            }
        }
    }

    private void deleteExtraNodes() {
        int numberOfPasses = 0;
        while (deleteExtraNodes((ParseNodeDrawable)parseTree.getRoot())){
            numberOfPasses++;
        }
    }

    private boolean deleteExtraNodes(ParseNodeDrawable parseNode) {
        boolean result = false;
        if (parseNode.hasDeletedChild() && parseNode.numberOfChildren() == 1 && ((ParseNodeDrawable)parseNode.getChild(0)).getLayerInfo() == null) {
            ParseNodeDrawable child = (ParseNodeDrawable) parseNode.getChild(0);
            for (int i = 0; i < child.numberOfChildren(); i++) {
                parseNode.addChild(child.getChild(i));
            }
            parseNode.removeChild(parseNode.getChild(0));
            result = true;
        }
        for(int i = 0; i < parseNode.numberOfChildren(); i++){
            if (deleteExtraNodes((ParseNodeDrawable)parseNode.getChild(i))){
                result = true;
            }
        }
        return result;
    }

    public void convert() {
        searchNONE((ParseNodeDrawable)parseTree.getRoot());
        deleteLeafNonTerminals((ParseNodeDrawable)parseTree.getRoot());
        deleteExtraNodes();
    }

    public ConvertToTurkishParseTree(ParseTreeDrawable parseTree){
        this.parseTree = parseTree;
    }
}
