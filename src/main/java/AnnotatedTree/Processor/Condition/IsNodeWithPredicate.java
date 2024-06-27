package AnnotatedTree.Processor.Condition;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.LayerInfo;
import AnnotatedTree.ParseNodeDrawable;

public class IsNodeWithPredicate extends IsNodeWithSynSetId{

    /**
     * Stores the synset id to check.
     * @param id Synset id to check
     */
    public IsNodeWithPredicate(String id) {
        super(id);
    }

    /**
     * Checks if at least one of the semantic ids of the parse node is equal to the given id and also the node is
     * annotated as PREDICATE with semantic role.
     * @param parseNode Parse node to check.
     * @return True if at least one of the semantic ids of the parse node is equal to the given id and also the node is
     * annotated as PREDICATE with semantic role, false otherwise.
     */
    public boolean satisfies(ParseNodeDrawable parseNode) {
        LayerInfo layerInfo = parseNode.getLayerInfo();
        return super.satisfies(parseNode) && layerInfo != null && layerInfo.getLayerData(ViewLayerType.PROPBANK) != null && layerInfo.getLayerData(ViewLayerType.PROPBANK).equals("PREDICATE");
    }

}
