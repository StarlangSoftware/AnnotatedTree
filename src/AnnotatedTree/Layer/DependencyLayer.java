package AnnotatedTree.Layer;

public class DependencyLayer extends SingleWordLayer<String> {

    public DependencyLayer(String layerValue) {
        layerName = "dependency";
        setLayerValue(layerValue);
    }

}
