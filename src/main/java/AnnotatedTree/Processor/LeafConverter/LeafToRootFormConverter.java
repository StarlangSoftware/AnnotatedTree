package AnnotatedTree.Processor.LeafConverter;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedTree.*;

public class LeafToRootFormConverter implements LeafToStringConverter  {

    /**
     * Converts the data in the leaf node to string. If there are multiple words in the leaf node, they are concatenated
     * with space.
     * @param parseNodeDrawable Node to be converted to string.
     * @return String form of the data. If there are multiple words in the leaf node, they are concatenated
     * with space.
     */
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
        } catch (LayerNotExistsException | WordNotExistsException ignored) {
        }
        return rootWords.toString();
    }

}