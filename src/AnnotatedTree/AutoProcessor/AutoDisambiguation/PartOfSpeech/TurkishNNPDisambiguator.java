package AnnotatedTree.AutoProcessor.AutoDisambiguation.PartOfSpeech;

import AnnotatedSentence.ViewLayerType;
import MorphologicalAnalysis.FsmParse;
import MorphologicalAnalysis.FsmParseList;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;

public class TurkishNNPDisambiguator extends TurkishPartOfSpeechDisambiguator{

    public FsmParse[] disambiguate(FsmParseList[] fsmParses, ParseNodeDrawable node, ParseTreeDrawable parseTree) {
        if (containsPOS(fsmParses, "PROP", true)){
            return complexMultipleWordsPOSdisambiguate(fsmParses, "PROP");
        } else {
            if (!node.getLayerData(ViewLayerType.TURKISH_WORD).equalsIgnoreCase("milli")){
                return complexPOSdisambiguate(fsmParses, "NOUN", true);
            } else {
                return null;
            }
        }
    }

}
