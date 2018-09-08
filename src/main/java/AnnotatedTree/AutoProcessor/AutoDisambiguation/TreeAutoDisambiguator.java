package AnnotatedTree.AutoProcessor.AutoDisambiguation;

import MorphologicalDisambiguation.AutoDisambiguator;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import MorphologicalDisambiguation.RootWordStatistics;
import AnnotatedTree.ParseTreeDrawable;

public abstract class TreeAutoDisambiguator extends AutoDisambiguator{
    protected abstract boolean autoFillSingleAnalysis(ParseTreeDrawable parseTree);
    protected abstract boolean autoDisambiguateWithRules(ParseTreeDrawable parseTree);
    protected abstract boolean autoDisambiguateSingleRootWords(ParseTreeDrawable parseTree);
    protected abstract boolean autoDisambiguateMultipleRootWords(ParseTreeDrawable parseTree);

    protected TreeAutoDisambiguator(FsmMorphologicalAnalyzer morphologicalAnalyzer, RootWordStatistics rootWordStatistics){
        this.morphologicalAnalyzer = morphologicalAnalyzer;
        this.rootWordStatistics = rootWordStatistics;
    }

    public void autoDisambiguate(ParseTreeDrawable parseTree){
        boolean modified;
        modified = autoFillSingleAnalysis(parseTree);
        modified = modified || autoDisambiguateWithRules(parseTree);
        modified = modified || autoDisambiguateSingleRootWords(parseTree);
        modified = modified || autoDisambiguateMultipleRootWords(parseTree);
        if (modified){
            parseTree.save();
        }
    }

}
