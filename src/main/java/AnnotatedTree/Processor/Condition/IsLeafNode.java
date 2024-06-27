package AnnotatedTree.Processor.Condition;

import AnnotatedTree.ParseNodeDrawable;

public class IsLeafNode implements NodeDrawableCondition {

    /**
     * Checks if the parse node is a leaf node, i.e., it has no child.
     * @param parseNode Parse node to check.
     * @return True if the parse node is a leaf node, false otherwise.
     */
    public boolean satisfies(ParseNodeDrawable parseNode) {
        return parseNode.numberOfChildren() == 0;
    }
}
