package AnnotatedTree.Processor.LayerExist;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.Processor.Condition.IsTurkishLeafNode;

import java.util.ArrayList;

public class ContainsLayerInformation implements LeafListCondition {
    private ViewLayerType viewLayerType;

    public ContainsLayerInformation(ViewLayerType viewLayerType){
        this.viewLayerType = viewLayerType;
    }

    public boolean satisfies(ArrayList<ParseNodeDrawable> leafList) {
        for (ParseNodeDrawable parseNode : leafList){
            if (!parseNode.getLayerData(ViewLayerType.ENGLISH_WORD).contains("*")){
                switch (viewLayerType){
                    case TURKISH_WORD:
                        if (parseNode.getLayerData(viewLayerType) == null){
                            return false;
                        }
                        break;
                    case PART_OF_SPEECH:
                    case INFLECTIONAL_GROUP:
                    case NER:
                    case SEMANTICS:
                    case PROPBANK:
                        if (parseNode.getLayerData(viewLayerType) == null && new IsTurkishLeafNode().satisfies(parseNode)){
                            return false;
                        }
                        break;
                }
            }
        }
        return true;
    }
}
