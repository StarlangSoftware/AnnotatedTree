package AnnotatedTree.AutoProcessor.AutoDisambiguation.PartOfSpeech;

import MorphologicalAnalysis.FsmParse;
import MorphologicalAnalysis.FsmParseList;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;

public class TurkishJJDisambiguator extends TurkishPartOfSpeechDisambiguator{

    public FsmParse[] disambiguate(FsmParseList[] fsmParses, ParseNodeDrawable node, ParseTreeDrawable parseTree) {
        FsmParse[] result;
        result = complexPOSdisambiguate(fsmParses, "ADJ", false);
        if (result != null && result[result.length - 1].transitionList().endsWith("ADJ+ALMOST")){
            return null;
        } else {
            return result;
        }
    }
}
