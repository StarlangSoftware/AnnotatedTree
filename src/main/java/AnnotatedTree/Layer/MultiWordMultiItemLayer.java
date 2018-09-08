package AnnotatedTree.Layer;

import AnnotatedSentence.ViewLayerType;

public abstract class MultiWordMultiItemLayer<T> extends MultiWordLayer<T> {
    public abstract int getLayerSize(ViewLayerType viewLayer);
    public abstract String getLayerInfoAt(ViewLayerType viewLayer, int index);

}
