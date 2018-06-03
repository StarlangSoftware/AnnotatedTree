package AnnotatedTree.Processor.Condition;

import AnnotatedSentence.ViewLayerType;
import Dictionary.Word;
import AnnotatedTree.ParseNodeDrawable;

public class IsPunctuationNode extends IsLeafNode{

    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (super.satisfies(parseNode)){
            String data = parseNode.getLayerData(ViewLayerType.ENGLISH_WORD);
            return Word.isPunctuation(data) && !data.equals("$");
        }
        return false;
    }

}
