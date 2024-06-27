package AnnotatedTree.Processor.Condition;

import AnnotatedTree.ParseNodeDrawable;

public class IsEnglishLeafNode extends IsLeafNode{

    /**
     * Checks if the parse node is a leaf node and contains a valid English word in its data.
     * @param parseNode Parse node to check.
     * @return True if the parse node is a leaf node and contains a valid English word in its data; false otherwise.
     */
    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (super.satisfies(parseNode)) {
            return !new IsNullElement().satisfies(parseNode);
        }
        return false;
    }

}
