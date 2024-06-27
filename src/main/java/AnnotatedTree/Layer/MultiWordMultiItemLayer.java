package AnnotatedTree.Layer;

import AnnotatedSentence.ViewLayerType;

/**
 * Abstract class for storing multiple words (multiple properties per each word) in the node of the tree
 * @param <T> Type of the property for the word
 */
public abstract class MultiWordMultiItemLayer<T> extends MultiWordLayer<T> {
    public abstract int getLayerSize(ViewLayerType viewLayer);
    public abstract String getLayerInfoAt(ViewLayerType viewLayer, int index);

}
