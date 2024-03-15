package AnnotatedTree.Layer;

import java.util.ArrayList;
import java.util.Collections;

public class TurkishSemanticLayer extends MultiWordLayer<String>{

    public TurkishSemanticLayer(String layerValue) {
        layerName = "semantics";
        setLayerValue(layerValue);
    }

    public void setLayerValue(String layerValue){
        this.items = new ArrayList<>();
        this.layerValue = layerValue;
        if (layerValue != null){
            String[] splitMeanings = layerValue.split("\\$");
            Collections.addAll(items, splitMeanings);
        }
    }

}
