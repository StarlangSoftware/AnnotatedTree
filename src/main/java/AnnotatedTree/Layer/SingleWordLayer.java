package AnnotatedTree.Layer;

/**
 * Abstract class for storing single word (single property per each word) in the node of the tree
 * @param <T> Type of the property for the word
 */
public abstract class SingleWordLayer<T> extends WordLayer{

    /**
     * Sets the property of the word
     * @param layerValue Layer info
     */
    public void setLayerValue(String layerValue){
        this.layerValue = layerValue;
    }

}
