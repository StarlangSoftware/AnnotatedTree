package AnnotatedTree.AutoProcessor.AutoNER;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.Processor.Condition.IsTurkishLeafNode;
import AnnotatedTree.Processor.NodeDrawableCollector;
import AnnotatedTree.ParallelTreeBankDrawable;
import AnnotatedTree.TreeBankDrawable;

import java.io.File;
import java.util.ArrayList;

public class TestTreeAutoNER {

    public static void automaticNER(String dataFolder){
        TreeBankDrawable treeBank = new TreeBankDrawable(new File(dataFolder), ".");
        TreeAutoNER treeAutoNER = new TurkishTreeAutoNER();
        System.out.println("Treebank read. Now NER...");
        for (int i = 0; i < treeBank.size(); i++){
            ParseTreeDrawable parseTree = treeBank.get(i);
            treeAutoNER.autoNER(parseTree);
        }
    }

    public static void autoNER(String dataFolder, String correctFolder){
        int count = 0, total = 0;
        ParallelTreeBankDrawable treeBank = new ParallelTreeBankDrawable(new File(dataFolder), new File(correctFolder), ".");
        TreeAutoNER treeAutoNER = new TurkishTreeAutoNER();
        System.out.println("Parallel Treebank read. Now NER...");
        for (int i = 0; i < treeBank.size(); i++){
            ParseTreeDrawable parseTree = treeBank.fromTree(i);
            treeAutoNER.autoNER(parseTree);
            ParseTreeDrawable correctTree = treeBank.toTree(i);
            NodeDrawableCollector nodeDrawableCollector1 = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
            ArrayList<ParseNodeDrawable> leafList1 = nodeDrawableCollector1.collect();
            NodeDrawableCollector nodeDrawableCollector2 = new NodeDrawableCollector((ParseNodeDrawable) correctTree.getRoot(), new IsTurkishLeafNode());
            ArrayList<ParseNodeDrawable> leafList2 = nodeDrawableCollector2.collect();
            for (int j = 0; j < Math.min(leafList1.size(), leafList2.size()); j++){
                String correctNERString = leafList2.get(j).getLayerData(ViewLayerType.NER);
                String autoNERString = leafList1.get(j).getLayerData(ViewLayerType.NER);
                if (correctNERString != null){
                    if (autoNERString != null){
                        if (autoNERString.equalsIgnoreCase(correctNERString)){
                            count++;
                        } else {
                            System.out.println(parseTree.getName() + "(" + leafList2.get(j).getLayerData(ViewLayerType.TURKISH_WORD) + ") Wrong: " + autoNERString + "---> Correct: " + correctNERString);
                        }
                        total++;
                    }
                }
            }
        }
        System.out.println("Accuracy: " + 100 * count / (total + 0.0));
    }

    public static void main(String[]args){
        /*Accuracy: 95.60450647385237*/
        autoNER("../Turkish", "../Penn-Treebank/Turkish");
    }

}
