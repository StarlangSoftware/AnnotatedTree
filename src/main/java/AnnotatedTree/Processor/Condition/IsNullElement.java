package AnnotatedTree.Processor.Condition;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;

public class IsNullElement extends IsLeafNode{

    /**
     * Checks if the parse node is a leaf node and its data is '*' and its parent's data is '-NONE-'.
     * @param parseNode Parse node to check.
     * @return True if the parse node is a leaf node and its data is '*' and its parent's data is '-NONE-', false
     * otherwise.
     */
    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (super.satisfies(parseNode)){
            String data = parseNode.getLayerData(ViewLayerType.ENGLISH_WORD);
            String parentData = parseNode.getParent().getData().getName();
            return data.contains("*") || (data.equals("0") && parentData.equals("-NONE-"));
        }
        return false;
    }

}
