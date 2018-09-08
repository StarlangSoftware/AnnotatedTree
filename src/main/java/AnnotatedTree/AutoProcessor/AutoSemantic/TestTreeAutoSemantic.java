package AnnotatedTree.AutoProcessor.AutoSemantic;

import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.TreeBankDrawable;
import WordNet.WordNet;

import java.io.File;

public class TestTreeAutoSemantic {

    public static void automaticSemantic(String dataFolder){
        TreeBankDrawable treeBank = new TreeBankDrawable(new File(dataFolder), ".");
        TreeAutoSemantic treeAutoSemantic = new TurkishTreeAutoSemantic(new WordNet(), new FsmMorphologicalAnalyzer());
        System.out.println("Treebank read. Now Semantics...");
        for (int i = 0; i < treeBank.size(); i++){
            ParseTreeDrawable parseTree = treeBank.get(i);
            treeAutoSemantic.autoSemantic(parseTree);
        }
    }

    public static void main(String[] args){
    }

}
