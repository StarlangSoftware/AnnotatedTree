package AnnotatedTree.Processor.Condition;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedTree.*;

public class IsNodeWithSynSetId extends IsLeafNode{
    private final String id;

    /**
     * Stores the synset id to check.
     * @param id Synset id to check
     */
    public IsNodeWithSynSetId(String id){
        this.id = id;
    }

    /**
     * Checks if at least one of the semantic ids of the parse node is equal to the given id.
     * @param parseNode Parse node to check.
     * @return True if at least one of the semantic ids of the parse node is equal to the given id, false otherwise.
     */
    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (super.satisfies(parseNode)){
            LayerInfo layerInfo = parseNode.getLayerInfo();
            for (int i = 0; i < layerInfo.getNumberOfMeanings(); i++) {
                try {
                    String synSetId = layerInfo.getSemanticAt(i);
                    if (synSetId.equals(id)){
                        return true;
                    }
                } catch (LayerNotExistsException | WordNotExistsException ignored) {
                }
            }
        }
        return false;
    }

}
