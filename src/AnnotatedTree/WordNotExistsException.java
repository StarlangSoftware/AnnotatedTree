package AnnotatedTree;

import AnnotatedTree.Layer.MultiWordLayer;

public class WordNotExistsException extends Exception{

    private MultiWordLayer layer;
    private int index;

    public WordNotExistsException(MultiWordLayer layer, int index){
        this.layer = layer;
        this.index = index;
    }

    public String toString(){
        return "Word with index " + index + " of layer " + layer.getLayerName() + " does not exist";
    }

}
