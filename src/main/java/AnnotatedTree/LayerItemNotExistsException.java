package AnnotatedTree;

import AnnotatedTree.Layer.MultiWordLayer;

public class LayerItemNotExistsException extends Exception{
    private MultiWordLayer layer;
    private int index;

    public LayerItemNotExistsException(MultiWordLayer layer, int index){
        this.layer = layer;
        this.index = index;
    }

    public String toString(){
        return "Layer item with index " + index + " of layer " + layer.getLayerName() + " does not exist";
    }

}
