package AnnotatedTree.Processor.Condition;

import AnnotatedTree.ParseNodeDrawable;

public class IsVPNode implements NodeDrawableCondition {

    public boolean satisfies(ParseNodeDrawable parseNode) {
        return parseNode.numberOfChildren() > 0 && parseNode.getData().isVP();
    }
}
