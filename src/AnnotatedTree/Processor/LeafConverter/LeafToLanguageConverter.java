package AnnotatedTree.Processor.LeafConverter;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;

public class LeafToLanguageConverter implements LeafToStringConverter {
    protected ViewLayerType viewLayerType;

    public String leafConverter(ParseNodeDrawable leafNode) {
        String layerData = leafNode.getLayerData(viewLayerType);
        String parentLayerData = ((ParseNodeDrawable)leafNode.getParent()).getLayerData(viewLayerType);
        if (layerData != null){
            if (layerData.contains("*") || (layerData.equals("0") && parentLayerData.equals("-NONE-"))){
                return "";
            } else {
                return " " + layerData.replaceAll("-LRB-", "(").replaceAll("-RRB-", ")").replaceAll("-LSB-", "[").replaceAll("-RSB-", "]").replaceAll("-LCB-", "{").replaceAll("-RCB-", "}").replaceAll("-lrb-", "(").replaceAll("-rrb-", ")").replaceAll("-lsb-", "[").replaceAll("-rsb-", "]").replaceAll("-lcb-", "{").replaceAll("-rcb-", "}");
            }
        } else {
            return "";
        }
    }

}
