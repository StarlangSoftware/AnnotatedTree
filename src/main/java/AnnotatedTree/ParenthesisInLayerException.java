package AnnotatedTree;

public class ParenthesisInLayerException extends Exception{
    private String layerString;

    public ParenthesisInLayerException(String layerString){
        this.layerString = layerString;
    }

    public String toString(){
        return "Layer value " + layerString + " contains parenthesis '('";
    }

}
