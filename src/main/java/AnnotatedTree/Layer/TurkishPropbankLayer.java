package AnnotatedTree.Layer;

import PropBank.Argument;

public class TurkishPropbankLayer extends SingleWordLayer<Argument> {
    private Argument propbank = null;

    /**
     * Constructor for the Turkish propbank layer. Sets single semantic role information for multiple words in
     * the node.
     * @param layerValue Layer value for the propbank information. Consists of semantic role information
     *                   of multiple words.
     */
    public TurkishPropbankLayer(String layerValue) {
        layerName = "propBank";
        setLayerValue(layerValue);
    }

    /**
     * Sets the layer value for Turkish propbank layer. Converts the string form to an Argument.
     * @param layerValue New value for Turkish propbank layer.
     */
    public void setLayerValue(String layerValue){
        this.layerValue = layerValue;
        propbank = new Argument(layerValue);
    }

    /**
     * Accessor for the propbank field.
     * @return Propbank field.
     */
    public Argument getArgument(){
        return propbank;
    }

    /**
     * Another accessor for the propbank field.
     * @return String form of the propbank field.
     */
    public String getLayerValue(){
        return propbank.getArgumentType() + "$" + propbank.getId();
    }

}
