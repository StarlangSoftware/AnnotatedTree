package AnnotatedTree.Processor.Condition;

import AnnotatedTree.LayerInfo;
import AnnotatedTree.ParseNodeDrawable;
import WordNet.WordNet;

public class IsPredicateVerbNode extends IsVerbNode{

    /**
     * Stores the wordnet for checking the pos tag of the synset.
     * @param wordNet Wordnet used for checking the pos tag of the synset.
     */
    public IsPredicateVerbNode(WordNet wordNet) {
        super(wordNet);
    }

    /**
     * Checks if the node is a leaf node and at least one of the semantic ids of the parse node belong to a verb synset,
     * and the semantic role of the node is PREDICATE.
     * @param parseNode Parse node to check.
     * @return True if the node is a leaf node and at least one of the semantic ids of the parse node belong to a verb
     *          synset and the semantic role of the node is PREDICATE, false otherwise.
     */
    public boolean satisfies(ParseNodeDrawable parseNode) {
        LayerInfo layerInfo = parseNode.getLayerInfo();
        return super.satisfies(parseNode) && layerInfo != null && layerInfo.getArgument() != null && layerInfo.getArgument().getArgumentType().equals("PREDICATE");
    }

}
