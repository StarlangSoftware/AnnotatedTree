package AnnotatedTree.Processor;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedSentence.ViewLayerType;
import ParseTree.ParseNode;
import MorphologicalAnalysis.MorphologicalParse;
import AnnotatedTree.*;
import AnnotatedTree.Processor.NodeModification.DestroyLayers;
import AnnotatedTree.Processor.NodeModification.ModifyTags;

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
                if (grandChild.getLayerInfo() != null) {
                    if ((grandChild.getLayerData(ViewLayerType.TURKISH_WORD)).contains("*") || (grandChild.getLayerData(ViewLayerType.TURKISH_WORD)).equals("0")) {
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

    private void separateNodeToNodes(ParseNodeDrawable parseNode) throws ParenthesisInLayerException {
        int k, l;
        ParseNodeDrawable nonTerminal;
        LayerInfo layerInfo;
        boolean willContinue;
        for (k = 0; k < parseNode.numberOfChildren(); k++) {
            ParseNodeDrawable child = (ParseNodeDrawable) parseNode.getChild(k);
            willContinue = false;
            for (l = 0; l < child.numberOfChildren(); l++) {
                ParseNodeDrawable grandChild = (ParseNodeDrawable) child.getChild(l);
                if (grandChild.getLayerInfo() != null && grandChild.getLayerData(ViewLayerType.META_MORPHEME) != null) {
                    layerInfo = grandChild.getLayerInfo();
                    try{
                        if (layerInfo.getMorphologicalParseAt(0).size() > 0){
                            nonTerminal = new ParseNodeDrawable(grandChild, layerInfo.getMorphologicalParseAt(0).getRootPos(), true, grandChild.getDepth() + 1);
                        } else {
                            nonTerminal = new ParseNodeDrawable(grandChild, "-XXX-", true, grandChild.getDepth() + 1);
                        }
                        nonTerminal.addChild(grandChild);
                        child.replaceChild(grandChild, nonTerminal);
                    } catch (LayerNotExistsException e) {
                        e.printStackTrace();
                    } catch (WordNotExistsException e) {
                        e.printStackTrace();
                    }
                }  else {
                    willContinue = true;
                }
            }
            if (willContinue){
                separateNodeToNodes(child);
            }
        }
    }

    private int deleteExtraNodes() {
        int numberOfPasses = 0;
        while (deleteExtraNodes((ParseNodeDrawable)parseTree.getRoot())){
            numberOfPasses++;
        }
        return numberOfPasses;
    }

    private void separateMultiWord(ParseNodeDrawable parseNode) throws ParenthesisInLayerException {
        ParseNodeDrawable newChildNode;
        int k, l, t;
        boolean willContinue;
        for (k = 0; k < parseNode.numberOfChildren(); k++) {
            ParseNodeDrawable child = (ParseNodeDrawable) parseNode.getChild(k);
            willContinue = false;
            for (l = 0; l < child.numberOfChildren(); l++) {
                ParseNodeDrawable grandChild = (ParseNodeDrawable) child.getChild(l);
                LayerInfo layerInfo = grandChild.getLayerInfo();
                if (layerInfo != null && grandChild.getLayerData(ViewLayerType.META_MORPHEME) != null) {
                    try{
                        if (layerInfo.getNumberOfWords() > 1 && grandChild.numberOfChildren() == 0) {
                            ParseNodeDrawable nonTerminal;
                            for (t = 0; t < layerInfo.getNumberOfWords(); t++){
                                MorphologicalParse parse = layerInfo.getMorphologicalParseAt(t);
                                nonTerminal = new ParseNodeDrawable(grandChild, parse.getPos(), true, grandChild.getDepth() + 1);
                                if (t == 0){
                                    child.setChild(l, nonTerminal);
                                } else {
                                    child.addChild(l, nonTerminal);
                                }
                                l++;
                                newChildNode = new ParseNodeDrawable(nonTerminal, layerInfo.getTurkishWordAt(t), true, grandChild.getDepth() + 2);
                                newChildNode.clearLayers();
                                newChildNode.getLayerInfo().setLayerData(ViewLayerType.TURKISH_WORD, layerInfo.getTurkishWordAt(t));
                                newChildNode.getLayerInfo().setMorphologicalAnalysis(layerInfo.getMorphologicalParseAt(t));
                                newChildNode.getLayerInfo().setMetaMorphemes(layerInfo.getMetamorphicParseAt(t));
                                newChildNode.getLayerInfo().setLayerData(ViewLayerType.NER, layerInfo.getLayerData(ViewLayerType.NER));
                                if (layerInfo.getLayerData(ViewLayerType.SEMANTICS) != null){
                                    if (layerInfo.getLayerSize(ViewLayerType.SEMANTICS) < t){
                                        newChildNode.getLayerInfo().setLayerData(ViewLayerType.SEMANTICS, layerInfo.getSemanticAt(0));
                                    } else {
                                        newChildNode.getLayerInfo().setLayerData(ViewLayerType.SEMANTICS, layerInfo.getSemanticAt(t));
                                    }
                                }
                                newChildNode.clearData();
                                nonTerminal.addChild(newChildNode);
                            }
                        }
                    } catch (WordNotExistsException e) {
                        e.printStackTrace();
                    } catch (LayerNotExistsException e) {
                        e.printStackTrace();
                    }
                } else {
                    willContinue = true;
                }
            }
            if (willContinue){
                separateMultiWord(child);
            }
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

    public void convert() throws ParenthesisInLayerException {
        TreeModifier treeModifier;
        searchNONE((ParseNodeDrawable)parseTree.getRoot());
        deleteLeafNonTerminals((ParseNodeDrawable)parseTree.getRoot());
        deleteExtraNodes();
        treeModifier = new TreeModifier(parseTree, new ModifyTags());
        treeModifier.modify();
        separateMultiWord((ParseNodeDrawable)parseTree.getRoot());
        separateNodeToNodes((ParseNodeDrawable)parseTree.getRoot());
        treeModifier = new TreeModifier(parseTree, new DestroyLayers());
        treeModifier.modify();
    }

    public ConvertToTurkishParseTree(ParseTreeDrawable parseTree){
        this.parseTree = parseTree;
    }
}
