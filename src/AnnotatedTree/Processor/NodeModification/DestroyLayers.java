package AnnotatedTree.Processor.NodeModification;

import AnnotatedTree.*;

public class DestroyLayers implements NodeModifier{

    public void modifier(ParseNodeDrawable parseNode){
        LayerInfo layerInfo = parseNode.getLayerInfo();
        if (layerInfo != null){
            layerInfo.englishClear();
            layerInfo.dependencyClear();
            layerInfo.metaMorphemesMovedClear();
        }
    }
}
