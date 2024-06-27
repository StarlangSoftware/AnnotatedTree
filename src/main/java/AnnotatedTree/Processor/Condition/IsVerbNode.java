package AnnotatedTree.Processor.Condition;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedSentence.ViewLayerType;
import Dictionary.Pos;
import AnnotatedTree.*;
import WordNet.WordNet;

public class IsVerbNode extends IsLeafNode{
    private final WordNet wordNet;

    /**
     * Stores the wordnet for checking the pos tag of the synset.
     * @param wordNet Wordnet used for checking the pos tag of the synset.
     */
    public IsVerbNode(WordNet wordNet){
        this.wordNet = wordNet;
    }

    /**
     * Checks if the node is a leaf node and at least one of the semantic ids of the parse node belong to a verb synset.
     * @param parseNode Parse node to check.
     * @return True if the node is a leaf node and at least one of the semantic ids of the parse node belong to a verb
     * synset, false otherwise.
     */
    public boolean satisfies(ParseNodeDrawable parseNode) {
        LayerInfo layerInfo = parseNode.getLayerInfo();
        if (super.satisfies(parseNode) && layerInfo != null && layerInfo.getLayerData(ViewLayerType.SEMANTICS) != null){
            try {
                for (int i = 0; i < layerInfo.getNumberOfMeanings(); i++){
                    String synSetId = layerInfo.getSemanticAt(i);
                    if (wordNet.getSynSetWithId(synSetId) != null && wordNet.getSynSetWithId(synSetId).getPos() == Pos.VERB){
                        return true;
                    } else {
                        if (wordNet.getSynSetWithId(synSetId) == null){
                            System.out.println(parseNode.getLayerData(ViewLayerType.TURKISH_WORD) + " " + synSetId);
                        }
                    }
                }
            } catch (LayerNotExistsException | WordNotExistsException ignored) {
            }
        }
        return false;
    }
}
