package AnnotatedTree.Layer;

import AnnotatedSentence.ViewLayerType;
import MorphologicalAnalysis.MorphologicalParse;

import java.util.ArrayList;

public class MorphologicalAnalysisLayer extends MultiWordMultiItemLayer<MorphologicalParse>{

    /**
     * Constructor for the morphological analysis layer. Sets the morphological parse information for multiple words in
     * the node.
     * @param layerValue Layer value for the morphological parse information. Consists of morphological parse information
     *                   of multiple words separated via space character.
     */
    public MorphologicalAnalysisLayer(String layerValue) {
        layerName = "morphologicalAnalysis";
        setLayerValue(layerValue);
    }

    /**
     * Sets the layer value to the string form of the given morphological parse.
     * @param layerValue New morphological parse.
     */
    public void setLayerValue(String layerValue){
        this.items = new ArrayList<>();
        this.layerValue = layerValue;
        if (layerValue != null){
            String[] splitWords = layerValue.split("\\s");
            for (String word:splitWords){
                items.add(new MorphologicalParse(word));
            }
        }
    }

    /**
     * Sets the layer value to the string form of the given morphological parse.
     * @param parse New morphological parse.
     */
    public void setLayerValue(MorphologicalParse parse){
        layerValue = parse.getTransitionList();
        items = new ArrayList<>();
        items.add(parse);
    }

    /**
     * Returns the total number of morphological tags (for PART_OF_SPEECH) or inflectional groups
     * (for INFLECTIONAL_GROUP) in the words in the node.
     * @param viewLayer Layer type.
     * @return Total number of morphological tags (for PART_OF_SPEECH) or inflectional groups (for INFLECTIONAL_GROUP)
     * in the words in the node.
     */
    public int getLayerSize(ViewLayerType viewLayer) {
        int size;
        switch (viewLayer){
            case PART_OF_SPEECH:
                size = 0;
                for (MorphologicalParse parse:items){
                    size += parse.tagSize();
                }
                return size;
            case INFLECTIONAL_GROUP:
                size = 0;
                for (MorphologicalParse parse:items){
                    size += parse.size();
                }
                return size;
            default:
                return 0;
        }
    }

    /**
     * Returns the morphological tag (for PART_OF_SPEECH) or inflectional group (for INFLECTIONAL_GROUP) at position
     * index.
     * @param viewLayer Layer type.
     * @param index Position of the morphological tag (for PART_OF_SPEECH) or inflectional group (for INFLECTIONAL_GROUP)
     * @return The morphological tag (for PART_OF_SPEECH) or inflectional group (for INFLECTIONAL_GROUP)
     */
    public String getLayerInfoAt(ViewLayerType viewLayer, int index) {
        int size;
        switch (viewLayer){
            case PART_OF_SPEECH:
                size = 0;
                for (MorphologicalParse parse:items){
                    if (index < size + parse.tagSize()){
                        return parse.getTag(index - size);
                    }
                    size += parse.tagSize();
                }
                return null;
            case INFLECTIONAL_GROUP:
                size = 0;
                for (MorphologicalParse parse:items){
                    if (index < size + parse.size()){
                        return parse.getInflectionalGroupString(index - size);
                    }
                    size += parse.size();
                }
                return null;
        }
        return null;
    }

    /**
     * Checks if the last inflectional group contains VERB tag.
     * @return True if the last inflectional group contains VERB tag, false otherwise.
     */
    public boolean isVerbal(){
        String dbLabel = "^DB+";
        String needle = "VERB+";
        String haystack;
        if (layerValue.contains(dbLabel))
            haystack = layerValue.substring(layerValue.lastIndexOf(dbLabel) + 4);
        else
            haystack = layerValue;
        return haystack.contains(needle);
    }

    /**
     * Checks if the last verbal inflectional group contains ZERO tag.
     * @return True if the last verbal inflectional group contains ZERO tag, false otherwise.
     */
    public boolean isNominal(){
        String dbLabel = "^DB+VERB+";
        String needle = "ZERO+";
        String haystack;
        if (layerValue.contains(dbLabel))
            haystack = layerValue.substring(layerValue.lastIndexOf(dbLabel) + 9);
        else
            haystack = layerValue;
        return haystack.contains(needle);
    }

}
