package AnnotatedTree.Processor.Condition;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;

public class IsTransferable extends IsLeafNode {
    private ViewLayerType secondLanguage;

    public IsTransferable(ViewLayerType secondLanguage){
        this.secondLanguage = secondLanguage;
    }

    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (super.satisfies(parseNode)) {
            if (new IsNoneNode(secondLanguage).satisfies(parseNode)){
                return false;
            }
            return !new IsNullElement().satisfies(parseNode);
        }
        return false;
    }
}
