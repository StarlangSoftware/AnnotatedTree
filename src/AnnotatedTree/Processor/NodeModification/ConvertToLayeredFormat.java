package AnnotatedTree.Processor.NodeModification;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;

public class ConvertToLayeredFormat implements NodeModifier{

    public void modifier(ParseNodeDrawable parseNode) {
        if (parseNode.isLeaf()){
            String name = parseNode.getData().getName();
            parseNode.clearLayers();
            parseNode.getLayerInfo().setLayerData(ViewLayerType.ENGLISH_WORD, name);
            parseNode.clearData();
        }
    }
}
