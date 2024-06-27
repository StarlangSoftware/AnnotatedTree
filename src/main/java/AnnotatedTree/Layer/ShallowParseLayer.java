package AnnotatedTree.Layer;

import java.util.ArrayList;
import java.util.Collections;

public class ShallowParseLayer extends MultiWordLayer<String>{

    /**
     * Constructor for the shallow parse layer. Sets shallow parse information for each word in
     * the node.
     * @param layerValue Layer value for the shallow parse information. Consists of shallow parse information
     *                   for every word.
     */
    public ShallowParseLayer(String layerValue) {
        layerName = "shallowParse";
        setLayerValue(layerValue);
    }

    /**
     * Sets the value for the shallow parse layer in a node. Value may consist of multiple shallow parse information
     * separated via space character. Each shallow parse value is a string.
     * @param layerValue New layer info
     */
    public void setLayerValue(String layerValue) {
        this.items = new ArrayList<>();
        this.layerValue = layerValue;
        if (layerValue != null){
            String[] splitParse = layerValue.split(" ");
            Collections.addAll(items, splitParse);
        }
    }

}
