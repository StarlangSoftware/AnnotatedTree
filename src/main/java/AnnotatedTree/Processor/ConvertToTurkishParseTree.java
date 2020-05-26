package AnnotatedTree.Processor;

import AnnotatedSentence.ViewLayerType;
import ParseTree.ParseNode;
import AnnotatedTree.*;

import java.util.Iterator;

public class ConvertToTurkishParseTree {
    private ParseTreeDrawable parseTree;

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
