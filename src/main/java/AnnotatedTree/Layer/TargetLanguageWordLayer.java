package AnnotatedTree.Layer;

import AnnotatedSentence.ViewLayerType;

import java.util.ArrayList;
import java.util.Collections;

public abstract class TargetLanguageWordLayer extends MultiWordLayer<String> {

    public TargetLanguageWordLayer(String layerValue){
        setLayerValue(layerValue);
    }

    public void setLayerValue(String layerValue){
        items = new ArrayList<>();
        this.layerValue = layerValue;
        if (layerValue != null){
            String[] splitWords = layerValue.split("\\s");
            Collections.addAll(items, splitWords);
        }
    }

    public int getLayerSize(ViewLayerType viewLayer) {
        return 0;
    }

    public String getLayerInfoAt(ViewLayerType viewLayer, int index) {
        return null;
    }

}
