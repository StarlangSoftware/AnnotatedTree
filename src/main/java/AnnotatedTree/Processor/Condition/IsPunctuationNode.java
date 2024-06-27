package AnnotatedTree.Processor.Condition;

import AnnotatedSentence.ViewLayerType;
import Dictionary.Word;
import AnnotatedTree.ParseNodeDrawable;

public class IsPunctuationNode extends IsLeafNode{

    /**
     * Checks if the node is a leaf node and contains punctuation as the data.
     * @param parseNode Parse node to check.
     * @return True if the node is a leaf node and contains punctuation as the data, false otherwise.
     */
    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (super.satisfies(parseNode)){
            String data = parseNode.getLayerData(ViewLayerType.ENGLISH_WORD);
            return Word.isPunctuation(data) && !data.equals("$");
        }
        return false;
    }

}
