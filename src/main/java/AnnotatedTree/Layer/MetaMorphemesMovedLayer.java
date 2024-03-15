package AnnotatedTree.Layer;

import AnnotatedSentence.ViewLayerType;
import MorphologicalAnalysis.MetamorphicParse;

import java.util.ArrayList;

public class MetaMorphemesMovedLayer extends MultiWordMultiItemLayer<MetamorphicParse> {

    public MetaMorphemesMovedLayer(String layerValue) {
        layerName = "metaMorphemesMoved";
        setLayerValue(layerValue);
    }

    public void setLayerValue(String layerValue){
        items = new ArrayList<>();
        this.layerValue = layerValue;
        if (layerValue != null){
            String[] splitWords = layerValue.split("\\s");
            for (String word:splitWords){
                items.add(new MetamorphicParse(word));
            }
        }
    }

    public int getLayerSize(ViewLayerType viewLayer) {
        int size = 0;
        for (MetamorphicParse parse: items){
            size += parse.size();
        }
        return size;
    }

    public String getLayerInfoAt(ViewLayerType viewLayer, int index) {
        int size = 0;
        for (MetamorphicParse parse: items){
            if (index < size + parse.size()){
                return parse.getMetaMorpheme(index - size);
            }
            size += parse.size();
        }
        return null;
    }
}
