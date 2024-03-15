package AnnotatedTree.Layer;

import AnnotatedSentence.ViewLayerType;
import MorphologicalAnalysis.MorphologicalParse;

import java.util.ArrayList;

public class MorphologicalAnalysisLayer extends MultiWordMultiItemLayer<MorphologicalParse>{

    public MorphologicalAnalysisLayer(String layerValue) {
        layerName = "morphologicalAnalysis";
        setLayerValue(layerValue);
    }

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

    public void setLayerValue(MorphologicalParse parse){
        layerValue = parse.getTransitionList();
        items = new ArrayList<>();
        items.add(parse);
    }

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
