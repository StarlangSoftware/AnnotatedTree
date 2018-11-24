package AnnotatedTree.Processor.LeafConverter;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedTree.*;

public class LeafToRootFormConverter implements LeafToStringConverter  {

    @Override
    public String leafConverter(ParseNodeDrawable parseNodeDrawable) {
        LayerInfo layerInfo = parseNodeDrawable.getLayerInfo();
        StringBuilder rootWords = new StringBuilder(" ");
        try {
            for (int i = 0; i < layerInfo.getNumberOfWords(); i++) {
                String root = layerInfo.getMorphologicalParseAt(i).getWord().getName();
                if (root != null && !root.isEmpty()){
                    rootWords.append(" ").append(root);
                }
            }
        } catch (LayerNotExistsException | WordNotExistsException e) {
        }
        return rootWords.toString();
    }

}