package AnnotatedTree.Processor.LayerExist;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.Processor.Condition.IsTurkishLeafNode;

import java.util.ArrayList;

public class NotContainsLayerInformation implements LeafListCondition {

    private final ViewLayerType viewLayerType;

    /**
     * Constructor for NotContainsLayerInformation class. Sets the viewLayerType attribute.
     * @param viewLayerType Layer for which check is done.
     */
    public NotContainsLayerInformation(ViewLayerType viewLayerType){
        this.viewLayerType = viewLayerType;
    }

    /**
     * Checks if none of the leaf nodes in the leafList contains the given layer information.
     * @param leafList Array list storing the leaf nodes.
     * @return True if none of the leaf nodes in the leafList contains the given layer information, false otherwise.
     */
    public boolean satisfies(ArrayList<ParseNodeDrawable> leafList) {
        for (ParseNodeDrawable parseNode : leafList){
            if (!parseNode.getLayerData(ViewLayerType.ENGLISH_WORD).contains("*")) {
                switch (viewLayerType){
                    case TURKISH_WORD:
                        if (parseNode.getLayerData(viewLayerType) != null){
                            return false;
                        }
                        break;
                    case PART_OF_SPEECH:
                    case INFLECTIONAL_GROUP:
                    case NER:
                    case SEMANTICS:
                        if (parseNode.getLayerData(viewLayerType) != null && new IsTurkishLeafNode().satisfies(parseNode)){
                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }
}
