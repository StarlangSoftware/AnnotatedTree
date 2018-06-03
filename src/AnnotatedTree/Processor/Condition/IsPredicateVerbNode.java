package AnnotatedTree.Processor.Condition;

import AnnotatedTree.LayerInfo;
import AnnotatedTree.ParseNodeDrawable;
import WordNet.WordNet;

public class IsPredicateVerbNode extends IsVerbNode{

    public IsPredicateVerbNode(WordNet wordNet) {
        super(wordNet);
    }

    public boolean satisfies(ParseNodeDrawable parseNode) {
        LayerInfo layerInfo = parseNode.getLayerInfo();
        return super.satisfies(parseNode) && layerInfo != null && layerInfo.getArgument() != null && layerInfo.getArgument().getArgumentType().equals("PREDICATE");
    }

}
