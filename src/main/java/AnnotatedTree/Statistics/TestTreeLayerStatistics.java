package AnnotatedTree.Statistics;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.TreeBankDrawable;
import WordNet.WordNet;

import java.io.File;

public class TestTreeLayerStatistics {

    public static void main(String[] args){
        TreeBankDrawable treebank = new TreeBankDrawable(new File("../Penn-Treebank/Turkish"));
        //WordNet wordNet = new WordNet("Data/Wordnet/english_wordnet_version_31.xml");
        WordNet wordNet = new WordNet();
        TreeLayerStatistics treeLayerStatistics = new TreeLayerStatistics(treebank);
        treeLayerStatistics.calculateStatistics(ViewLayerType.SEMANTICS);
        treeLayerStatistics.printStatistics(wordNet);
    }

}
