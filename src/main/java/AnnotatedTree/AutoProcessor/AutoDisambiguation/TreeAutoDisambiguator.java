package AnnotatedTree.AutoProcessor.AutoDisambiguation;

import MorphologicalDisambiguation.AutoDisambiguator;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import MorphologicalDisambiguation.RootWordStatistics;
import AnnotatedTree.ParseTreeDrawable;

public abstract class TreeAutoDisambiguator extends AutoDisambiguator{
    protected abstract void autoFillSingleAnalysis(ParseTreeDrawable parseTree);
    protected abstract void autoDisambiguateWithRules(ParseTreeDrawable parseTree);
    protected abstract void autoDisambiguateMultipleRootWords(ParseTreeDrawable parseTree);

    protected TreeAutoDisambiguator(FsmMorphologicalAnalyzer morphologicalAnalyzer, RootWordStatistics rootWordStatistics){
        this.morphologicalAnalyzer = morphologicalAnalyzer;
        this.rootWordStatistics = rootWordStatistics;
    }

    public void autoDisambiguate(ParseTreeDrawable parseTree){
        autoFillSingleAnalysis(parseTree);
        autoDisambiguateWithRules(parseTree);
        autoDisambiguateMultipleRootWords(parseTree);
        parseTree.save();
    }

}
