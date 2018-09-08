package AnnotatedTree.AutoProcessor.AutoDisambiguation.PartOfSpeech;

import AnnotatedSentence.ViewLayerType;
import MorphologicalAnalysis.FsmParse;
import MorphologicalAnalysis.FsmParseList;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;

public class TurkishVBDisambiguator extends TurkishPartOfSpeechDisambiguator{

    public FsmParse[] disambiguate(FsmParseList[] fsmParses, ParseNodeDrawable node, ParseTreeDrawable parseTree) {
        if (node.getLayerData(ViewLayerType.TURKISH_WORD).contains("değil")){
            return null;
        }
        FsmParse[] result = complexPOSdisambiguate(fsmParses, "VERB", false);
        if (result != null){
            for (FsmParse fsmParse : result){
                if (fsmParse.getWord().getName().equals("var")){
                    return null;
                }
            }
        }
        return result;
    }

}
