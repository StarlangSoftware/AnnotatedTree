package AnnotatedTree.AutoProcessor.AutoMetaMorphemeMovement;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.Processor.Condition.IsLeafNode;
import AnnotatedTree.Processor.NodeDrawableCollector;
import AnnotatedTree.ParallelTreeBankDrawable;

import java.io.File;
import java.util.ArrayList;

public class TestAutoMetaMorphemeMover {

    public static void autoMetaMorphemeMove(String dataFolder, String correctFolder){
        int count = 0, total = 0, overall = 0;
        ParallelTreeBankDrawable treeBank = new ParallelTreeBankDrawable(new File(dataFolder), new File(correctFolder), ".");
        AutoMetaMorphemeMover autoDisambiguator = new TurkishAutoMetaMorphemeMover();
        System.out.println("Parallel Treebank read. Now pos moving...");
        for (int i = 0; i < treeBank.size(); i++){
            ParseTreeDrawable parseTree = treeBank.fromTree(i);
            autoDisambiguator.autoPosMove(parseTree);
            ParseTreeDrawable correctTree = treeBank.toTree(i);
            NodeDrawableCollector nodeDrawableCollector1 = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsLeafNode());
            ArrayList<ParseNodeDrawable> leafList1 = nodeDrawableCollector1.collect();
            NodeDrawableCollector nodeDrawableCollector2 = new NodeDrawableCollector((ParseNodeDrawable) correctTree.getRoot(), new IsLeafNode());
            ArrayList<ParseNodeDrawable> leafList2 = nodeDrawableCollector2.collect();
            for (int j = 0; j < Math.min(leafList1.size(), leafList2.size()); j++){
                String correctMoved = leafList2.get(j).getLayerData(ViewLayerType.META_MORPHEME_MOVED);
                String autoMoved = leafList1.get(j).getLayerData(ViewLayerType.META_MORPHEME_MOVED);
                if (correctMoved != null){
                    if (autoMoved != null){
                        if (autoMoved.equalsIgnoreCase(correctMoved)){
                            count++;
                        } else {
                            System.out.println(parseTree.getName() + ":" + correctMoved + "--->" + autoMoved);
                        }
                        total++;
                    }
                    overall++;
                }
            }
        }
        System.out.println("Accuracy: " + 100 * count / (total + 0.0) + " Coverage:" + 100 * total / (overall + 0.0));
    }

    public static void main(String[]args){
        autoMetaMorphemeMove("../Turkish", "../Penn-Treebank/Turkish");
    }
}
