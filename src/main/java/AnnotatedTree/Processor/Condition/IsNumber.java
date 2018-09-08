package AnnotatedTree.Processor.Condition;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;

public class IsNumber extends IsLeafNode{

    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (super.satisfies(parseNode)){
            String data = parseNode.getLayerData(ViewLayerType.ENGLISH_WORD);
            String parentData = parseNode.getParent().getData().getName();
            return parentData.equals("CD") && data.matches("[0-9,.]+");
        }
        return false;
    }

}
