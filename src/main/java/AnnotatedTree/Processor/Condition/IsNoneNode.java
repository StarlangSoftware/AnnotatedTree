package AnnotatedTree.Processor.Condition;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;

public class IsNoneNode extends IsLeafNode{
    private ViewLayerType secondLanguage;

    public IsNoneNode(ViewLayerType secondLanguage){
        this.secondLanguage = secondLanguage;
    }

    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (super.satisfies(parseNode)){
            String data = parseNode.getLayerData(secondLanguage);
            return data != null && data.equals("*NONE*");
        }
        return false;
    }

}
