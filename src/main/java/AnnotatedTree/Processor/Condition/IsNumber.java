package AnnotatedTree.Processor.Condition;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;

public class IsNumber extends IsLeafNode{

    /**
     * Checks if the node is a leaf node and contains numerals as the data and its parent has the tag CD.
     * @param parseNode Parse node to check.
     * @return True if the node is a leaf node and contains numerals as the data and its parent has the tag CD, false
     * otherwise.
     */
    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (super.satisfies(parseNode)){
            String data = parseNode.getLayerData(ViewLayerType.ENGLISH_WORD);
            String parentData = parseNode.getParent().getData().getName();
            return parentData.equals("CD") && data.matches("[0-9,.]+");
        }
        return false;
    }

}
