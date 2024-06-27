package AnnotatedTree.Layer;

public class DependencyLayer extends SingleWordLayer<String> {

    /**
     * Constructor for the dependency layer. Dependency layer stores the dependency information of a node.
     * @param layerValue Value of the dependency layer.
     */
    public DependencyLayer(String layerValue) {
        layerName = "dependency";
        setLayerValue(layerValue);
    }

}
