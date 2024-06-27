package AnnotatedTree.Layer;

import NamedEntityRecognition.NamedEntityType;

public class NERLayer extends SingleWordLayer<NamedEntityType>{
    private NamedEntityType namedEntity = null;

    /**
     * Constructor for the named entity layer. Sets single named entity information for multiple words in
     * the node.
     * @param layerValue Layer value for the named entity information. Consists of single named entity information
     *                   of multiple words.
     */
    public NERLayer(String layerValue) {
        layerName = "namedEntity";
        setLayerValue(layerValue);
    }

    /**
     * Sets the layer value for Named Entity layer. Converts the string form to a named entity.
     * @param layerValue New value for Named Entity layer.
     */
    public void setLayerValue(String layerValue){
        this.layerValue = layerValue;
        namedEntity = NamedEntityType.getNamedEntityType(layerValue);
    }

    /**
     * Get the string form of the named entity value. Converts named entity type to string form.
     * @return String form of the named entity value.
     */
    public String getLayerValue(){
        return NamedEntityType.getNamedEntityType(namedEntity);
    }

}
