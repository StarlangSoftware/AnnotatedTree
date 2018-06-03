package AnnotatedTree.Layer;

import PropBank.Argument;

import java.util.ArrayList;

public class EnglishPropbankLayer extends SingleWordMultiItemLayer<Argument>{

    public EnglishPropbankLayer(String layerValue) {
        layerName = "englishPropbank";
        setLayerValue(layerValue);
    }

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
