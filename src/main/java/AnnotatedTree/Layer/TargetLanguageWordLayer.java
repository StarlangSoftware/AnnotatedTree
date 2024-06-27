package AnnotatedTree.Layer;

import AnnotatedSentence.ViewLayerType;

import java.util.ArrayList;
import java.util.Collections;

public abstract class TargetLanguageWordLayer extends MultiWordLayer<String> {

    /**
     * Sets the surface form(s) of the word(s) possibly separated with space.
     * @param layerValue Surface form(s) of the word(s) possibly separated with space.
     */
    public TargetLanguageWordLayer(String layerValue){
        setLayerValue(layerValue);
    }

    /**
     * Sets the surface form(s) of the word(s). Value may consist of multiple surface form(s)
     * separated via space character.
     * @param layerValue New layer info
     */
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
