package AnnotatedTree.Layer;

import AnnotatedSentence.ViewLayerType;
import MorphologicalAnalysis.MetamorphicParse;
import AnnotatedTree.LayerItemNotExistsException;

import java.util.ArrayList;

public class MetaMorphemeLayer extends MultiWordMultiItemLayer<MetamorphicParse>{

    public MetaMorphemeLayer(String layerValue) {
        layerName = "metaMorphemes";
        setLayerValue(layerValue);
    }

    public void setLayerValue(String layerValue){
        items = new ArrayList<MetamorphicParse>();
        this.layerValue = layerValue;
        if (layerValue != null){
            String[] splitWords = layerValue.split("\\s");
            for (String word:splitWords){
                items.add(new MetamorphicParse(word));
            }
        }
    }

    public void setLayerValue(MetamorphicParse parse){
        layerValue = parse.toString();
        items = new ArrayList<MetamorphicParse>();
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

    public String getLayerInfoFrom(int index) {
        int size = 0;
        for (MetamorphicParse parse: items){
            if (index < size + parse.size()){
                String result = parse.getMetaMorpheme(index - size);
                index++;
                while (index < size + parse.size()){
                    result = result + "+" + parse.getMetaMorpheme(index - size);
                    index++;
                }
                return result;
            }
            size += parse.size();
        }
        return null;
    }

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
