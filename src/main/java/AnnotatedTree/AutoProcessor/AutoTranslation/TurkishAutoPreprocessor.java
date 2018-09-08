package AnnotatedTree.AutoProcessor.AutoTranslation;

import AnnotatedSentence.ViewLayerType;
import ContextFreeGrammar.Rule;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.Processor.Condition.*;
import AnnotatedTree.Processor.NodeDrawableCollector;

import java.util.ArrayList;

public class TurkishAutoPreprocessor extends AutoPreprocessor{
    private String[] swapRules = {"NP->NP PP", "NP->NP ADJP", "NP->NP VP", "NP->NP SBAR", "NP->NNP CD", "NP->NP VP .", "NP->CD RB .", "NP->DT JJ NN", "NP->NP PP .", "NP->DT ADJP NN",
            "S->VP NP",
            "ADJP->JJ PP", "ADJP->ADJP PP", "ADJP->CD NN", "ADJP->JJ S", "ADJP->JJ NP", "ADJP->ADJP SBAR",
            "ADVP->RB PP", "ADVP->RB NP",
            "VP->ADJP PP", "VP->NP PP VBD", "VP->NP PP VBP", "VP->ADJP PP VBZ", "VP->NP PP VBZ"};

    public TurkishAutoPreprocessor(){
        secondLanguage = ViewLayerType.TURKISH_WORD;
    }

    public void autoFillWithNoneTags(ParseTreeDrawable parseTree) {
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsNoneReplaceable());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            if (parseNode.getLayerData(ViewLayerType.TURKISH_WORD) == null || parseNode.getLayerData(ViewLayerType.TURKISH_WORD).equals("'")){
                parseNode.getLayerInfo().setLayerData(ViewLayerType.TURKISH_WORD, "*NONE*");
            }
        }
    }

    public void autoSwap(ParseTreeDrawable parseTree) {
        NodeDrawableCollector nodeDrawableCollector;
        ArrayList<ParseNodeDrawable> leafList, nodeList;
        nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsNoneNode(ViewLayerType.TURKISH_WORD));
        leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            ParseNodeDrawable grandParent = (ParseNodeDrawable) parseNode.getParent().getParent();
            ParseNodeDrawable parent = (ParseNodeDrawable) parseNode.getParent();
            String parentData = parent.getData().getName();
            if (parentData != null && !parentData.equals("DT") && !parentData.equals("WP")){
                swapBeforeDot(grandParent, parent);
            }
        }
        nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsLeafNode());
        leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            ParseNodeDrawable grandGrandParent = (ParseNodeDrawable) parseNode.getParent().getParent().getParent();
            ParseNodeDrawable grandParent = (ParseNodeDrawable) parseNode.getParent().getParent();
            ParseNodeDrawable parent = (ParseNodeDrawable) parseNode.getParent();
            String parentData = parent.getData().getName();
            if (parentData != null){
                if (parentData.equals("PRP$") || parentData.equals("$") || parentData.equals("VBZ") || parentData.equals("VB") || parentData.equals("VBG") || parentData.equals("VBP") || parentData.equals("VBD") || parentData.equals("VBN")){
                    swapBeforeDot(grandParent, parent);
                } else {
                    if (parentData.equals("EX")){
                        swapBeforeDot(grandGrandParent, grandParent);
                    } else {
                        if (parseNode.getLayerData(ViewLayerType.TURKISH_WORD) != null && parseNode.getLayerData(ViewLayerType.ENGLISH_WORD).equals("%")){
                            grandParent.removeChild(parent);
                            grandParent.addChild(0, parent);
                        }
                    }
                }
            }
        }
        boolean swapped = true;
        while (swapped){
            swapped = false;
            for (String rule : swapRules){
                nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsNodeWithRule(new Rule(rule)));
                nodeList = nodeDrawableCollector.collect();
                for (ParseNodeDrawable parseNode : nodeList){
                    ParseNodeDrawable child = (ParseNodeDrawable) parseNode.getChild(0);
                    parseNode.removeChild(child);
                    parseNode.addChild(1, child);
                    swapped = true;
                }
            }
        }
        nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsLeafNode());
        leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            String parentData = parseNode.getParent().getData().getName();
            if (parentData != null && parentData.equals("PRP")){
                parseNode.getLayerInfo().setLayerData(ViewLayerType.TURKISH_WORD, "*NONE*");
                swapBeforeDot((ParseNodeDrawable) parseNode.getParent().getParent().getParent(), (ParseNodeDrawable) parseNode.getParent().getParent());
            }
        }
        nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsNodeWithSymbol("IN"));
        nodeList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : nodeList){
            if (parseNode.getParent().numberOfChildren() == 2 && parseNode.getParent().getChild(0).getData().getName().equals("IN")){
                ParseNodeDrawable parent = (ParseNodeDrawable) parseNode.getParent();
                swapBeforeDot(parent, parseNode);
            }
        }
    }
}
