package AnnotatedTree.Processor.Condition;

import AnnotatedTree.ParseNodeDrawable;

public class IsNodeWithSymbol implements NodeDrawableCondition {
    private String symbol;

    public IsNodeWithSymbol(String symbol){
        this.symbol = symbol;
    }

    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (parseNode.numberOfChildren() > 0){
            return parseNode.getData().toString().equals(symbol);
        } else {
            return false;
        }
    }
}
