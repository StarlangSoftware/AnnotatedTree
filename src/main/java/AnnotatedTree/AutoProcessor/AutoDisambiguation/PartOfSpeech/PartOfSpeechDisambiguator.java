package AnnotatedTree.AutoProcessor.AutoDisambiguation.PartOfSpeech;

import MorphologicalAnalysis.FsmParse;
import MorphologicalAnalysis.FsmParseList;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;

public interface PartOfSpeechDisambiguator {
    FsmParse[] disambiguate(FsmParseList[] fsmParses, ParseNodeDrawable node, ParseTreeDrawable parseTree);
}
