package AnnotatedTree.Processor.Condition;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedTree.*;

public class IsNodeWithSynSetId extends IsLeafNode{
    private final String id;

    public IsNodeWithSynSetId(String id){
        this.id = id;
    }

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
