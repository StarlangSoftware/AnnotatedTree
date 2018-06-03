package AnnotatedTree.Layer;

import AnnotatedSentence.ViewLayerType;

import java.util.ArrayList;

public abstract class SingleWordMultiItemLayer<T> extends SingleWordLayer<T> {
    protected ArrayList<T> items = new ArrayList<T>();

    public T getItemAt(int index){
        if (index < items.size()){
            return items.get(index);
        } else {
            return null;
        }
    }

    public int getLayerSize(ViewLayerType viewLayer){
        return items.size();
    }

}
