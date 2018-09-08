package AnnotatedTree.Layer;

import NamedEntityRecognition.NamedEntityType;

public class NERLayer extends SingleWordLayer<NamedEntityType>{
    private NamedEntityType namedEntity = null;

    public NERLayer(String layerValue) {
        layerName = "namedEntity";
        setLayerValue(layerValue);
    }

    public void setLayerValue(String layerValue){
        this.layerValue = layerValue;
        namedEntity = NamedEntityType.getNamedEntityType(layerValue);
    }

    public String getLayerValue(){
        return NamedEntityType.getNamedEntityType(namedEntity);
    }

}
