package AnnotatedTree.AutoProcessor.AutoTranslation;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.Processor.Condition.IsNullElement;
import AnnotatedTree.Processor.Condition.IsPunctuationNode;
import AnnotatedTree.Processor.NodeDrawableCollector;

import java.util.ArrayList;

public abstract class AutoPreprocessor {
    protected ViewLayerType secondLanguage;

    public abstract void autoFillWithNoneTags(ParseTreeDrawable parseTree);
    public abstract void autoSwap(ParseTreeDrawable parseTree);

    protected void autoFillWithSame(ArrayList<ParseNodeDrawable> leafList){
        for (ParseNodeDrawable parseNode : leafList){
            if (parseNode.getLayerData(secondLanguage) == null){
                parseNode.getLayerInfo().setLayerData(secondLanguage, parseNode.getLayerData(ViewLayerType.ENGLISH_WORD));
            }
        }
    }

    public void autoFillNullElements(ParseTreeDrawable parseTree){
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsNullElement());
        autoFillWithSame(nodeDrawableCollector.collect());
    }

    public void autoFillPunctuation(ParseTreeDrawable parseTree){
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsPunctuationNode());
        autoFillWithSame(nodeDrawableCollector.collect());
    }

    protected void swapBeforeDot(ParseNodeDrawable parent, ParseNodeDrawable child){
        parent.removeChild(child);
        int i;
        for (i = parent.numberOfChildren() - 1; i >= 0; i--){
            if (parent.getChild(i).getData().getName().equals(".")){
                parent.addChild(i, child);
                break;
            }
        }
        if (i == -1){
            parent.addChild(child);
        }
    }

}
