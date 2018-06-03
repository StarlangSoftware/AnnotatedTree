package AnnotatedTree.Processor.LayerExist;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.Processor.Condition.IsTurkishLeafNode;

import java.util.ArrayList;

public class SemiContainsLayerInformation implements LeafListCondition{

    private ViewLayerType viewLayerType;

    public SemiContainsLayerInformation(ViewLayerType viewLayerType){
        this.viewLayerType = viewLayerType;
    }

    public boolean satisfies(ArrayList<ParseNodeDrawable> leafList) {
        int notDone = 0, done = 0;
        for (ParseNodeDrawable parseNode : leafList){
            if (!parseNode.getLayerData(ViewLayerType.ENGLISH_WORD).contains("*")) {
                switch (viewLayerType){
                    case TURKISH_WORD:
                        if (parseNode.getLayerData(viewLayerType) != null){
                            done++;
                        } else {
                            notDone++;
                        }
                        break;
                    case PART_OF_SPEECH:
                    case INFLECTIONAL_GROUP:
                    case NER:
                    case SEMANTICS:
                        if (new IsTurkishLeafNode().satisfies(parseNode)){
                            if (parseNode.getLayerData(viewLayerType) != null){
                                done++;
                            } else {
                                notDone++;
                            }
                        }
                        break;
                }
            }
        }
        return done != 0 && notDone != 0;
    }
}
