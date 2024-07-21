package AnnotatedTree.Layer;

import AnnotatedSentence.ViewLayerType;
import MorphologicalAnalysis.MetamorphicParse;
import AnnotatedTree.LayerItemNotExistsException;

import java.util.ArrayList;

public class MetaMorphemeLayer extends MetaMorphemesMovedLayer{

    /**
     * Constructor for the metamorpheme layer. Sets the metamorpheme information for multiple words in the node.
     * @param layerValue Layer value for the metamorpheme information. Consists of metamorpheme information of multiple
     *                   words separated via space character.
     */
    public MetaMorphemeLayer(String layerValue) {
        super(layerValue);
        layerName = "metaMorphemes";
    }

    /**
     * Sets the layer value to the string form of the given parse.
     * @param parse New metamorphic parse.
     */
    public void setLayerValue(MetamorphicParse parse){
        layerValue = parse.toString();
        items = new ArrayList<>();
        if (layerValue != null){
            String[] splitWords = layerValue.split("\\s");
            for (String word:splitWords){
                items.add(new MetamorphicParse(word));
            }
        }
    }

    /**
     * Constructs metamorpheme information starting from the position index.
     * @param index Position of the morpheme to start.
     * @return Metamorpheme information starting from the position index.
     */
    public String getLayerInfoFrom(int index) {
        int size = 0;
        for (MetamorphicParse parse: items){
            if (index < size + parse.size()){
                StringBuilder result = new StringBuilder(parse.getMetaMorpheme(index - size));
                index++;
                while (index < size + parse.size()){
                    result.append("+").append(parse.getMetaMorpheme(index - size));
                    index++;
                }
                return result.toString();
            }
            size += parse.size();
        }
        return null;
    }

    /**
     * Removes metamorphemes from the given index. Index shows the position of the metamorpheme in the metamorphemes list.
     * @param index Position of the metamorpheme from which the other metamorphemes will be removed.
     * @throws LayerItemNotExistsException If the index is invalid for this layer, this exception is thrown.
     * @return New metamorphic parse not containing the removed parts.
     */
    public MetamorphicParse metaMorphemeRemoveFromIndex(int index) throws LayerItemNotExistsException {
        if (index >= 0 && index < getLayerSize(ViewLayerType.META_MORPHEME)){
            int size = 0;
            for (MetamorphicParse parse: items){
                if (index < size + parse.size()){
                    parse.removeMetaMorphemeFromIndex(index - size);
                    return parse;
                }
                size += parse.size();
            }
        } else {
            throw new LayerItemNotExistsException(this, index);
        }
        return null;
    }

}
