package AnnotatedTree.Layer;

import AnnotatedSentence.ViewLayerType;

import java.util.ArrayList;

/**
 * Abstract class for storing single word (multiple properties per each word) in the node of the tree
 * @param <T> Type of the property for the word
 */
public abstract class SingleWordMultiItemLayer<T> extends SingleWordLayer<T> {
    protected ArrayList<T> items = new ArrayList<>();

    /**
     * Returns the property at position index for the word.
     * @param index Position of the property
     * @return The property at position index for the word.
     */
    public T getItemAt(int index){
        if (index < items.size()){
            return items.get(index);
        } else {
            return null;
        }
    }

    /**
     * Returns the total number of properties for the word in the node.
     * @param viewLayer Not used.
     * @return Total number of properties for the word in the node.
     */
    public int getLayerSize(ViewLayerType viewLayer){
        return items.size();
    }

}
