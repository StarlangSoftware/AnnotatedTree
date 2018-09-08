package AnnotatedTree.AutoProcessor.AutoDisambiguation.PartOfSpeech;

import MorphologicalAnalysis.FsmParse;
import MorphologicalAnalysis.FsmParseList;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;

public class TurkishDTDisambiguator extends TurkishPartOfSpeechDisambiguator{

    public FsmParse[] disambiguate(FsmParseList[] fsmParses, ParseNodeDrawable node, ParseTreeDrawable parseTree) {
        if (containsPOS(fsmParses, "DET", true)){
            return simpleSingleWordDisambiguate(fsmParses, "DET");
        } else {
            return simpleSingleWordDisambiguate(fsmParses, "PRON");
        }
    }

}
