package AnnotatedTree.Processor.Condition;

import AnnotatedTree.ParseNodeDrawable;

public class IsDoubleNode implements NodeDrawableCondition {

    /**
     * Checks if the parse node is a double node, i.e., it has one child and his child has one or more children; its
     * tag equals to its child tag.
     * @param parseNode Parse node to check
     * @return True if the tag of the parse node is equal to the tag of its child node, false otherwise.
     */
    public boolean satisfies(ParseNodeDrawable parseNode) {
        return parseNode.numberOfChildren() == 1 && parseNode.getChild(0).numberOfChildren() >= 1 && !parseNode.getChild(0).isLeaf() && parseNode.getData().equals(parseNode.getChild(0).getData());
    }

}
