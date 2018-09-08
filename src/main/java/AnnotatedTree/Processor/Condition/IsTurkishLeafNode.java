package AnnotatedTree.Processor.Condition;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;

public class IsTurkishLeafNode extends IsLeafNode{

    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (super.satisfies(parseNode)){
            String data = parseNode.getLayerInfo().getLayerData(ViewLayerType.TURKISH_WORD);
            String parentData = parseNode.getParent().getData().getName();
            return (data != null && !data.contains("*") && !(data.equals("0") && parentData.equals("-NONE-")));
        }
        return false;
    }
}
