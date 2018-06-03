package AnnotatedTree.AutoProcessor.AutoArgument;

import AnnotatedSentence.ViewLayerType;
import Dictionary.Word;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.Processor.Condition.IsTransferable;
import AnnotatedTree.Processor.NodeDrawableCollector;
import PropBank.ArgumentType;
import PropBank.Frameset;

import java.util.ArrayList;

public abstract class AutoArgument {
    protected ViewLayerType secondLanguage;
    protected abstract boolean autoDetectArgument(ParseNodeDrawable parseNode, ArgumentType argumentType);

    protected AutoArgument(ViewLayerType secondLanguage){
        this.secondLanguage = secondLanguage;
    }

    public void autoArgument(ParseTreeDrawable parseTree, Frameset frameset){
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTransferable(secondLanguage));
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            if (parseNode.getLayerData(ViewLayerType.PROPBANK) == null){
                for (ArgumentType argumentType : ArgumentType.values()){
                    if (frameset.containsArgument(argumentType) && autoDetectArgument(parseNode, argumentType)){
                        parseNode.getLayerInfo().setLayerData(ViewLayerType.PROPBANK, ArgumentType.getPropbankType(argumentType));
                    }
                }
                if (Word.isPunctuation(parseNode.getLayerData(secondLanguage))){
                    parseNode.getLayerInfo().setLayerData(ViewLayerType.PROPBANK, "NONE");
                }
            }
        }
        parseTree.save();
    }

}
