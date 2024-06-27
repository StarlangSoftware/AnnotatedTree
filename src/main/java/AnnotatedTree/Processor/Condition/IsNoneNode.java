package AnnotatedTree.Processor.Condition;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;

public class IsNoneNode extends IsLeafNode{
    private final ViewLayerType secondLanguage;

    public IsNoneNode(ViewLayerType secondLanguage){
        this.secondLanguage = secondLanguage;
    }

    /**
     * Checks if the data of the parse node is '*NONE*'.
     * @param parseNode Parse node to check.
     * @return True if the data of the parse node is '*NONE*', false otherwise.
     */
    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (super.satisfies(parseNode)){
            String data = parseNode.getLayerData(secondLanguage);
            return data != null && data.equals("*NONE*");
        }
        return false;
    }

}
