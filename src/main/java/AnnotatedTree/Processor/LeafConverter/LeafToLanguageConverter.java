package AnnotatedTree.Processor.LeafConverter;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;

public class LeafToLanguageConverter implements LeafToStringConverter {
    protected ViewLayerType viewLayerType;

    /**
     * Converts the data in the leaf node to string, except shortcuts to parentheses are converted to its normal forms,
     * '*', '0', '-NONE-' are converted to empty string.
     * @param leafNode Node to be converted to string.
     * @return String form of the data, except shortcuts to parentheses are converted to its normal forms,
     * '*', '0', '-NONE-' are converted to empty string.
     */
    public String leafConverter(ParseNodeDrawable leafNode) {
        String layerData = leafNode.getLayerData(viewLayerType);
        String parentLayerData = ((ParseNodeDrawable)leafNode.getParent()).getLayerData(viewLayerType);
        if (layerData != null){
            if (layerData.contains("*") || (layerData.equals("0") && parentLayerData.equals("-NONE-"))){
                return "";
            } else {
                return " " + layerData
                        .replaceAll("-LRB-", "(")
                        .replaceAll("-RRB-", ")")
                        .replaceAll("-LSB-", "[")
                        .replaceAll("-RSB-", "]")
                        .replaceAll("-LCB-", "{")
                        .replaceAll("-RCB-", "}")
                        .replaceAll("-lrb-", "(")
                        .replaceAll("-rrb-", ")")
                        .replaceAll("-lsb-", "[")
                        .replaceAll("-rsb-", "]")
                        .replaceAll("-lcb-", "{")
                        .replaceAll("-rcb-", "}");
            }
        } else {
            return "";
        }
    }

}
