package AnnotatedTree.Layer;

import java.util.ArrayList;
import java.util.Collections;

public class TurkishSemanticLayer extends MultiWordLayer<String>{

    /**
     * Constructor for the Turkish semantic layer. Sets semantic information for each word in
     * the node.
     * @param layerValue Layer value for the Turkish semantic information. Consists of semantic (Turkish synset id)
     *                   information for every word.
     */
    public TurkishSemanticLayer(String layerValue) {
        layerName = "semantics";
        setLayerValue(layerValue);
    }

    /**
     * Sets the value for the Turkish semantic layer in a node. Value may consist of multiple sense information
     * separated via '$' character. Each sense value is a string representing the synset id of the sense.
     * @param layerValue New layer info
     */
    public void setLayerValue(String layerValue){
        this.items = new ArrayList<>();
        this.layerValue = layerValue;
        if (layerValue != null){
            String[] splitMeanings = layerValue.split("\\$");
            Collections.addAll(items, splitMeanings);
        }
    }

}
