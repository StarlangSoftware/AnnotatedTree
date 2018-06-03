package AnnotatedTree.Processor.Condition;

import AnnotatedTree.ParseNodeDrawable;

public class IsLeafNode implements NodeDrawableCondition {

    public boolean satisfies(ParseNodeDrawable parseNode) {
        return parseNode.numberOfChildren() == 0;
    }
}
