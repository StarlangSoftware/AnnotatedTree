package AnnotatedTree.Processor.Condition;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;

public class IsTransferable extends IsLeafNode {
    private final ViewLayerType secondLanguage;

    public IsTransferable(ViewLayerType secondLanguage){
        this.secondLanguage = secondLanguage;
    }

    /**
     * Checks if the node is a leaf node and is not a None or Null node.
     * @param parseNode Parse node to check.
     * @return True if the node is a leaf node and is not a None or Null node, false otherwise.
     */
    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (super.satisfies(parseNode)) {
            if (new IsNoneNode(secondLanguage).satisfies(parseNode)){
                return false;
            }
            return !new IsNullElement().satisfies(parseNode);
        }
        return false;
    }
}
