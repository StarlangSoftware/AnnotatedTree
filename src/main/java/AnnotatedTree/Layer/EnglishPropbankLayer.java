package AnnotatedTree.Layer;

import PropBank.Argument;

import java.util.ArrayList;

public class EnglishPropbankLayer extends SingleWordMultiItemLayer<Argument>{

    /**
     * Constructor for the propbank layer for English language.
     * @param layerValue Value for the English propbank layer.
     */
    public EnglishPropbankLayer(String layerValue) {
        layerName = "englishPropbank";
        setLayerValue(layerValue);
    }

    /**
     * Sets the value for the propbank layer in a node. Value may consist of multiple propbank information separated via
     * '#' character. Each propbank value consists of argumentType and id info separated via '$' character.
     * @param layerValue New layer info
     */
    public void setLayerValue(String layerValue){
        items = new ArrayList<>();
        this.layerValue = layerValue;
        if (layerValue != null){
            String[] splitWords = layerValue.split("#");
            for (String word:splitWords){
                items.add(new Argument(word));
            }
        }
    }

}
