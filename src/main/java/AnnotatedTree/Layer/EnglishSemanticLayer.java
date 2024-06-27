package AnnotatedTree.Layer;

public class EnglishSemanticLayer extends SingleWordLayer<String>{

    /**
     * Constructor for the semantic layer for English language. Sets the layer value to the synset id defined in English
     * WordNet.
     * @param layerValue Value for the English semantic layer.
     */
    public EnglishSemanticLayer(String layerValue) {
        layerName = "englishSemantics";
        setLayerValue(layerValue);
    }

}
