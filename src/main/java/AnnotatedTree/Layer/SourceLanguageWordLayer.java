package AnnotatedTree.Layer;

public abstract class SourceLanguageWordLayer extends SingleWordLayer<String>{

    /**
     * Sets the name of the word
     * @param layerValue Name of the word
     */
    public SourceLanguageWordLayer(String layerValue){
        setLayerValue(layerValue);
    }

}
