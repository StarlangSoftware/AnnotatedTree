package AnnotatedTree.Processor.Condition;

import AnnotatedTree.ParseNodeDrawable;

public class IsDoubleNodeWithDifferentTags implements NodeDrawableCondition {

    public boolean satisfies(ParseNodeDrawable parseNode) {
        return parseNode.numberOfChildren() == 1 && parseNode.getChild(0).numberOfChildren() >= 1 && !parseNode.getChild(0).isLeaf() && !parseNode.getData().equals(parseNode.getChild(0).getData());
    }
}
