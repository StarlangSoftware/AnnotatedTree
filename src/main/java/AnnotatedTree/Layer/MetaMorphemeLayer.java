package AnnotatedTree.Layer;

import AnnotatedSentence.ViewLayerType;
import MorphologicalAnalysis.MetamorphicParse;
import AnnotatedTree.LayerItemNotExistsException;

import java.util.ArrayList;

public class MetaMorphemeLayer extends MetaMorphemesMovedLayer{

    public MetaMorphemeLayer(String layerValue) {
        super(layerValue);
        layerName = "metaMorphemes";
    }

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
