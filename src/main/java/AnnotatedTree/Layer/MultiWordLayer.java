package AnnotatedTree.Layer;

import java.util.ArrayList;

/**
 * Abstract class for storing multiple words (single property per each word) in the node of the tree
 * @param <T> Type of the property for the word
 */
public abstract class MultiWordLayer<T> extends WordLayer{

    protected ArrayList<T> items = new ArrayList<>();

    /**
     * Returns the item (word or its property) at position index.
     * @param index Position of the item (word or its property).
     * @return The item at position index.
     */
    public T getItemAt(int index){
        if (index < items.size()){
            return items.get(index);
        } else {
            return null;
        }
    }

    /**
     * Returns number of items (words) in the items array list.
     * @return Number of items (words) in the items array list.
     */
    public int size(){
        return items.size();
    }

    public abstract void setLayerValue(String layerValue);

}
