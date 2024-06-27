package AnnotatedTree.Layer;

public class PersianWordLayer extends TargetLanguageWordLayer{

    /**
     * Constructor for the word layer for Persian language. Sets the surface form.
     * @param layerValue Value for the word layer.
     */
    public PersianWordLayer(String layerValue){
        super(layerValue);
        layerName = "persian";
    }

}
