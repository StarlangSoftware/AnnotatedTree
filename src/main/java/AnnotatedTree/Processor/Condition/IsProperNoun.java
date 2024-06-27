package AnnotatedTree.Processor.Condition;

import AnnotatedTree.ParseNodeDrawable;

public class IsProperNoun extends IsLeafNode{

    /**
     * Checks if the node is a leaf node and its parent has the tag NNP or NNPS.
     * @param parseNode Parse node to check.
     * @return True if the node is a leaf node and its parent has the tag NNP or NNPS, false otherwise.
     */
    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (super.satisfies(parseNode)){
            String parentData = parseNode.getParent().getData().getName();
            return parentData.equals("NNP") || parentData.equals("NNPS");
        }
        return false;
    }

}
