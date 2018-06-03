package AnnotatedTree.Layer;

public abstract class WordLayer {
    protected String layerValue;
    protected String layerName;

    public String getLayerValue(){
        return layerValue;
    }

    public String getLayerName(){
        return layerName;
    }

    public String getLayerDescription(){
        return "{" + layerName + "=" + layerValue + "}";
    }

}
