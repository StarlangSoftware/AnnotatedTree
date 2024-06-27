package AnnotatedTree.Layer;

public class EnglishWordLayer extends SourceLanguageWordLayer {

    /**
     * Constructor for the word layer for English language. Sets the surface form.
     * @param layerValue Value for the word layer.
     */
    public EnglishWordLayer(String layerValue){
        super(layerValue);
        layerName = "english";
    }

}
