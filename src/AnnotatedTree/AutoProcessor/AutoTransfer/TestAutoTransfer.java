package AnnotatedTree.AutoProcessor.AutoTransfer;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.Processor.Condition.IsTurkishLeafNode;
import AnnotatedTree.Processor.NodeDrawableCollector;
import AnnotatedTree.Processor.NodeModification.ConvertToLayeredFormat;
import AnnotatedTree.Processor.TreeModifier;
import AnnotatedTree.ParallelTreeBankDrawable;
import AnnotatedTree.TreeBankDrawable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class TestAutoTransfer {

    public static void sortNodes(ArrayList<ParseNodeDrawable> leafList){
        for (int i = 0; i < leafList.size(); i++){
            for (int j = i + 1; j < leafList.size(); j++){
                String english1 = leafList.get(i).getLayerData(ViewLayerType.ENGLISH_WORD);
                String english2 = leafList.get(j).getLayerData(ViewLayerType.ENGLISH_WORD);
                if (english1.compareTo(english2) > 0){
                    Collections.swap(leafList, i, j);
                }
            }
        }
    }

    public static void autoTransfer(String englishFolder, String turkishFolder){
        int count = 0, total = 0, overall = 0;
        ParallelTreeBankDrawable treeBank = new ParallelTreeBankDrawable(new File(englishFolder), new File(turkishFolder), ".");
        AutoTransfer autoTransfer = new TurkishAutoTransfer();
        System.out.println("Parallel Treebank read. Now autotransfer...");
        for (int i = 0; i < treeBank.size(); i++){
            ParseTreeDrawable parseTree = treeBank.fromTree(i);
            TreeModifier treeModifier = new TreeModifier(parseTree, new ConvertToLayeredFormat());
            treeModifier.modify();
            autoTransfer.autoTransfer(parseTree, new TransferredSentence(new File(TreeBankDrawable.TURKISH_PHRASE_PATH + parseTree.getName())));
            ParseTreeDrawable correctTree = treeBank.toTree(i);
            NodeDrawableCollector nodeDrawableCollector1 = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
            ArrayList<ParseNodeDrawable> leafList1 = nodeDrawableCollector1.collect();
            NodeDrawableCollector nodeDrawableCollector2 = new NodeDrawableCollector((ParseNodeDrawable) correctTree.getRoot(), new IsTurkishLeafNode());
            ArrayList<ParseNodeDrawable> leafList2 = nodeDrawableCollector2.collect();
            sortNodes(leafList1);
            sortNodes(leafList2);
            int j = 0, k = 0;
            overall += leafList2.size();
            while (j < leafList1.size() && k < leafList2.size()){
                String english1 = leafList1.get(j).getLayerData(ViewLayerType.ENGLISH_WORD);
                String english2 = leafList2.get(k).getLayerData(ViewLayerType.ENGLISH_WORD);
                int comparisonResult = english1.compareTo(english2);
                if (comparisonResult < 0){
                    j++;
                } else {
                    if (comparisonResult > 0){
                        k++;
                    } else {
                        String autoTurkishWord = leafList1.get(j).getLayerData(ViewLayerType.TURKISH_WORD);
                        String correctTurkishWord = leafList2.get(k).getLayerData(ViewLayerType.TURKISH_WORD);
                        if (correctTurkishWord != null){
                            if (autoTurkishWord != null){
                                if (autoTurkishWord.equalsIgnoreCase(correctTurkishWord)){
                                    count++;
                                }
                                total++;
                            }
                        }
                        j++;
                        k++;
                    }
                }
            }
        }
        System.out.println("Accuracy: " + 100 * count / (total + 0.0) + " Coverage:" + 100 * total / (overall + 0.0));
    }

    public static void main(String[]args){
        /*Accuracy: 94.78583063346878 Coverage:73.13487544018118*/
        autoTransfer("../Penn-Treebank/English", "../Penn-Treebank/Turkish");
    }

}
