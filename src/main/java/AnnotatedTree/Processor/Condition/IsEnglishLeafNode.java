package AnnotatedTree.Processor.Condition;

import AnnotatedTree.ParseNodeDrawable;

public class IsEnglishLeafNode extends IsLeafNode{

    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (super.satisfies(parseNode)) {
            return !new IsNullElement().satisfies(parseNode);
        }
        return false;
    }

}
