package AnnotatedTree.Layer;

import java.util.ArrayList;
import java.util.Collections;

public class ShallowParseLayer extends MultiWordLayer<String>{

    public ShallowParseLayer(String layerValue) {
        layerName = "shallowParse";
        setLayerValue(layerValue);
    }

    public void setLayerValue(String layerValue) {
        this.items = new ArrayList<String>();
        this.layerValue = layerValue;
        if (layerValue != null){
            String[] splitParse = layerValue.split(" ");
            Collections.addAll(items, splitParse);
        }
    }

}
