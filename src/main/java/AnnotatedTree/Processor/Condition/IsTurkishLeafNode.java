package AnnotatedTree.Processor.Condition;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;

public class IsTurkishLeafNode extends IsLeafNode{

    /**
     * Checks if the parse node is a leaf node and contains a valid Turkish word in its data.
     * @param parseNode Parse node to check.
     * @return True if the parse node is a leaf node and contains a valid Turkish word in its data; false otherwise.
     */
    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (super.satisfies(parseNode)){
            String data = parseNode.getLayerInfo().getLayerData(ViewLayerType.TURKISH_WORD);
            String parentData = parseNode.getParent().getData().getName();
            return data != null && !data.contains("*") && !(data.equals("0") && parentData.equals("-NONE-"));
        }
        return false;
    }
}
