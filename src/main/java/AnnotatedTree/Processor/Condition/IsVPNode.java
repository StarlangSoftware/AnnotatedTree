package AnnotatedTree.Processor.Condition;

import AnnotatedTree.ParseNodeDrawable;

public class IsVPNode implements NodeDrawableCondition {

    /**
     * Checks if the node is not a leaf node and its tag is VP.
     * @param parseNode Parse node to check.
     * @return True if the node is not a leaf node and its tag is VP, false otherwise.
     */
    public boolean satisfies(ParseNodeDrawable parseNode) {
        return parseNode.numberOfChildren() > 0 && parseNode.getData().isVP();
    }
}
