package AnnotatedTree.Layer;

import PropBank.Argument;

public class TurkishPropbankLayer extends SingleWordLayer<Argument> {
    private Argument propbank = null;

    public TurkishPropbankLayer(String layerValue) {
        layerName = "propBank";
        setLayerValue(layerValue);
    }

    public void setLayerValue(String layerValue){
        this.layerValue = layerValue;
        propbank = new Argument(layerValue);
    }

    public Argument getArgument(){
        return propbank;
    }

    public String getLayerValue(){
        return propbank.getArgumentType() + "$" + propbank.getId();
    }

}
