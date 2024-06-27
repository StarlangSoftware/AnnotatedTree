package AnnotatedTree.Layer;

public abstract class WordLayer {
    protected String layerValue;
    protected String layerName;

    /**
     * Accessor for the layerValue attribute.
     * @return LayerValue attribute.
     */
    public String getLayerValue(){
        return layerValue;
    }

    /**
     * Accessor for the layerName attribute.
     * @return LayerName attribute.
     */
    public String getLayerName(){
        return layerName;
    }

    /**
     * Returns string form of the word layer.
     * @return String form of the word layer.
     */
    public String getLayerDescription(){
        return "{" + layerName + "=" + layerValue + "}";
    }

}
