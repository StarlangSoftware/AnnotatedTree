package AnnotatedTree.AutoProcessor.AutoDisambiguation.PartOfSpeech;

import MorphologicalAnalysis.FsmParse;
import MorphologicalAnalysis.FsmParseList;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;

public class TurkishNNDisambiguator extends TurkishPartOfSpeechDisambiguator{

    public FsmParse[] disambiguate(FsmParseList[] fsmParses, ParseNodeDrawable node, ParseTreeDrawable parseTree) {
        return complexPOSdisambiguate(fsmParses, "NOUN", false);
    }
}
