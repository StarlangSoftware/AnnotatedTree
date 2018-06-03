package AnnotatedTree.Processor.Condition;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.LayerInfo;
import AnnotatedTree.ParseNodeDrawable;

public class IsNodeWithPredicate extends IsNodeWithSynSetId{

    public IsNodeWithPredicate(String id) {
        super(id);
    }

    public boolean satisfies(ParseNodeDrawable parseNode) {
        LayerInfo layerInfo = parseNode.getLayerInfo();
        return super.satisfies(parseNode) && layerInfo != null && layerInfo.getLayerData(ViewLayerType.PROPBANK) != null && layerInfo.getLayerData(ViewLayerType.PROPBANK).equals("PREDICATE");
    }

}
