package AnnotatedTree.Processor.NodeModification;

import AnnotatedSentence.LayerNotExistsException;
import ParseTree.Symbol;
import AnnotatedTree.LayerInfo;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.WordNotExistsException;

public class ModifyTags implements NodeModifier{

    public void modifier(ParseNodeDrawable parseNode) {
        LayerInfo layerInfo = parseNode.getLayerInfo();
        if (layerInfo != null){
            if (parseNode.getParent().getData().getName().equalsIgnoreCase("NNS")){
                boolean isPlural = false;
                try{
                    for (int i = 0; i < layerInfo.getNumberOfWords(); i++){
                        if (layerInfo.getMorphologicalParseAt(i).isPlural()){
                            isPlural = true;
                            break;
                        }
                    }
                } catch (WordNotExistsException e) {
                    e.printStackTrace();
                } catch (LayerNotExistsException e) {
                    e.printStackTrace();
                }
                if (!isPlural){
                    parseNode.getParent().setData(new Symbol("NN"));
                }
            }
        } else {
            parseNode.setData(parseNode.getData().trimSymbol());
            if (parseNode.getData().getName().startsWith("VB")){
                parseNode.setData(new Symbol("VB"));
            }
        }
    }
}
