package AnnotatedTree.Processor.Condition;

import AnnotatedTree.ParseNodeDrawable;

public class IsProperNoun extends IsLeafNode{

    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (super.satisfies(parseNode)){
            String parentData = parseNode.getParent().getData().getName();
            return parentData.equals("NNP") || parentData.equals("NNPS");
        }
        return false;
    }

}
