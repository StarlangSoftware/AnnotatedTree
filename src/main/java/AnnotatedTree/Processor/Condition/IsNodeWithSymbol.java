package AnnotatedTree.Processor.Condition;

import AnnotatedTree.ParseNodeDrawable;

public class IsNodeWithSymbol implements NodeDrawableCondition {
    private final String symbol;

    /**
     * Stores the symbol to check.
     * @param symbol Symbol to check
     */
    public IsNodeWithSymbol(String symbol){
        this.symbol = symbol;
    }

    /**
     * Checks if the tag of the parse node is equal to the given symbol.
     * @param parseNode Parse node to check.
     * @return True if the tag of the parse node is equal to the given symbol, false otherwise.
     */
    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (parseNode.numberOfChildren() > 0){
            return parseNode.getData().toString().equals(symbol);
        } else {
            return false;
        }
    }
}
