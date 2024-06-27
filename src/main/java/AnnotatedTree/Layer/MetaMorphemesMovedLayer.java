package AnnotatedTree.Layer;

import AnnotatedSentence.ViewLayerType;
import MorphologicalAnalysis.MetamorphicParse;

import java.util.ArrayList;

public class MetaMorphemesMovedLayer extends MultiWordMultiItemLayer<MetamorphicParse> {

    /**
     * Constructor for the metaMorphemesMoved layer. Sets the metamorpheme information for multiple words in the node.
     * @param layerValue Layer value for the metaMorphemesMoved information. Consists of metamorpheme information of
     *                   multiple words separated via space character.
     */
    public MetaMorphemesMovedLayer(String layerValue) {
        layerName = "metaMorphemesMoved";
        setLayerValue(layerValue);
    }

    /**
     * Sets the layer value to the string form of the given parse.
     * @param layerValue New metamorphic parse.
     */
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

    /**
     * Returns the total number of metamorphemes in the words in the node.
     * @param viewLayer Not used.
     * @return Total number of metamorphemes in the words in the node.
     */
    public int getLayerSize(ViewLayerType viewLayer) {
        int size = 0;
        for (MetamorphicParse parse: items){
            size += parse.size();
        }
        return size;
    }

    /**
     * Returns the metamorpheme at position index in the metamorpheme list.
     * @param viewLayer Not used.
     * @param index Position in the metamorpheme list.
     * @return The metamorpheme at position index in the metamorpheme list.
     */
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
