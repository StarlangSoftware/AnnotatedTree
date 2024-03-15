package AnnotatedTree.Layer;

import java.util.ArrayList;

public abstract class MultiWordLayer<T> extends WordLayer{

    protected ArrayList<T> items = new ArrayList<>();

    public T getItemAt(int index){
        if (index < items.size()){
            return items.get(index);
        } else {
            return null;
        }
    }

    public int size(){
        return items.size();
    }

    public abstract void setLayerValue(String layerValue);

}
