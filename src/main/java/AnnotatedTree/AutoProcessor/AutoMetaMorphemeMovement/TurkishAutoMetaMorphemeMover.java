package AnnotatedTree.AutoProcessor.AutoMetaMorphemeMovement;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedSentence.ViewLayerType;
import MorphologicalAnalysis.MetamorphicParse;
import AnnotatedTree.*;
import AnnotatedTree.Processor.Condition.IsLeafNode;
import AnnotatedTree.Processor.NodeDrawableCollector;

import java.util.ArrayList;

public class TurkishAutoMetaMorphemeMover extends AutoMetaMorphemeMover {

    public void singleMetaMorphemeMoveWithList(ParseNodeDrawable parseNode, ParseNodeDrawable noneNode, String[] metaMorphemeList) {
        try {
            int previousParseSize = 0;
            for (int i = 0; i < parseNode.getLayerInfo().getNumberOfWords() - 1; i++){
                previousParseSize += parseNode.getLayerInfo().getMetamorphicParseAt(i).size();
            }
            MetamorphicParse metamorphicParse = parseNode.getLayerInfo().getMetamorphicParseAt(parseNode.getLayerInfo().getNumberOfWords() - 1);
            for (int i = metamorphicParse.size() - 1; i >= 0; i--){
                for (String metaMorpheme : metaMorphemeList) {
                    if (metamorphicParse.getMetaMorpheme(i).equals(metaMorpheme)) {
                        LayerInfo fromLayer = parseNode.getLayerInfo(), toLayer = noneNode.getLayerInfo();
                        toLayer.setLayerData(ViewLayerType.META_MORPHEME_MOVED, fromLayer.getMetaMorphemeFromIndex(previousParseSize + i));
                        fromLayer.metaMorphemeRemove(previousParseSize + i);
                        return;
                    }
                }
            }
        } catch (WordNotExistsException | LayerNotExistsException | LayerItemNotExistsException e) {
            e.printStackTrace();
        }
    }

    private void singleMetaMorphemeMoveWithPosTags(ParseNodeDrawable parseNode, ParseNodeDrawable noneNode){
        String[] metaMorphemeList = null;
        ParseNodeDrawable parentNode = (ParseNodeDrawable) noneNode.getParent();
        switch (parentNode.getData().getName()){
            case "IN":
                metaMorphemeList = new String[]{"nHn", "DA", "nA", "yA", "yH", "ylA", "DAn", "nDA", "nDAn", "'Hn", "'nHn", "'DA", "'nDA", "'nA", "'yA", "'ylA", "'DAn", "yHncA", "DHkCA", "yken", "yArAk"};
                break;
            case "TO":
                metaMorphemeList = new String[]{"yA", "nA", "'yA", "'nA"};
                break;
            case "POS":
                metaMorphemeList = new String[]{"nHn", "'nHn", "DAn", "'DAn"};
                break;
            case "PRP$":
                metaMorphemeList = new String[]{"Hm", "Hn", "sH", "HmHz", "H", "lArH"};
                break;
            case "VBZ":
            case "VBP":
                metaMorphemeList = new String[]{"DHr", "'DHr", "Hr", "yHm", "yHz", "SHn", "SHnHz", "lAr"};
                break;
            case "MD":
                metaMorphemeList = new String[]{"yAcAk", "yAbil", "mAlH"};
                break;
            case "VBD":
                metaMorphemeList = new String[]{"yDH", "DH"};
                break;
            case "PRP":
                metaMorphemeList = new String[]{"Hm", "SHn", "Hz", "lAr", "m", "n", "k", "z", "yHm", "yHz", "zsHn", "zlAr", "nHz"};
                break;
            case "EX":
                metaMorphemeList = new String[]{"yDH", "DH", "DHr"};
                break;
        }
        if (metaMorphemeList != null){
            singleMetaMorphemeMoveWithList(parseNode, noneNode, metaMorphemeList);
        }
    }

    private int[] searchMetaMorphemes(String[][] metaMorphemes, MetamorphicParse parse){
        int[] result = new int[metaMorphemes.length];
        int t = parse.size() - 1, k = metaMorphemes.length - 1;
        while (t >= 0 && k >= 0){
            if (metaMorphemes[k].length > 0){
                for (String metaMorpheme : metaMorphemes[k]){
                    if (parse.getMetaMorpheme(t).equals(metaMorpheme)){
                        result[k] = t;
                        k--;
                        break;
                    }
                }
            } else {
                result[k] = -1;
                k--;
                t++;
            }
            t--;
        }
        if (k == -1){
            return result;
        } else {
            return null;
        }
    }

    private void multipleMetaMorphemeMoveWithPosTags(ParseNodeDrawable parseNode, ArrayList<ParseNodeDrawable> noneNodes){
        String[][] posTags = {
                {"VBD", "PRP"},
                {"VBD", "PRP"},
                {"VBD", "EX"},
                {"VB", "MD", "PRP"},
                {"VB", "MD", "RB"},
                {"VB", "MD"},
                {"VB", "MD"},
                {"VB", "RB", "MD"},
                {"RB", "VBD", "PRP"},
                {"RB", "VBD"},
                {"RB", "MD", "PRP"},
                {"RB", "MD"},
                {"RB", "VBP", "PRP"},
                {"RB", "VBP"},
                {"RB", "VBZ"},
                {"RB", "PRP"},
                {"VBN", "VBD"},
                {"VBN", "VBD"},
                {"VBN", "VBZ"},
                {"VBN", "VBZ"},
                {"VBN", "VBP"},
                {"VBN", "RB", "VBZ"},
                {"VBP", "PRP"},
                {"VBZ", "PRP"},
                {"IN", "VBD"},
                {"IN", "VBD"},
                {"IN", "VBZ"},
                {"IN", "VBZ"},
                {"IN", "VBZ"},
                {"IN", "VBP"},
                {"IN", "VBP"},
                {"IN", "VBP"},
                {"IN", "RB"},
                {"IN", "IN"},
                {"MD", "RB", "PRP"},
                {"MD", "RB"},
                {"MD", "PRP"},
                {"MD", "PRP"},
                {"MD", "EX"},
                {"MD", "IN"},
                {"MD", "IN"},
                {"TO", "VBG", "VBZ"},
                {"TO", "VBD"},
                {"TO", "VBZ"},
                {"TO", "VBZ"},
                {"TO", "VBP"},
                {"VBG", "VBZ"},
                {"PRP$", "IN"},
                {"PRP$", "TO"},
                {"PRP", "VB"},
                {"PRP", "IN"},
                {"POS", "IN"},
                {"POS", "IN"}
        };
        String[][][] metaMorphemes = {
                /*VBD*/{{"DH", "yDH"}, {"m", "n", "k", "lAr"}},
                {{"DH", "yDH"}, {}},
                {{"DH", "yDH"}, {}},
                /*VB*/{{}, {"yAcAk"}, {"Hm", "SHn", "Hz", "lAr", "lArH"}},
                {{}, {}, {"yAmA"}},
                {{"n", "Hl"}, {"yAcAk", "yAbil"}},
                {{}, {"yAcAk", "yAbil"}},
                {{}, {"yAmA"}, {}},
                /*RB*/{{"mA"}, {"DH", "yDH"}, {"m", "n", "k", "lAr"}},
                {{"mA"}, {"DH", "yDH"}},
                {{"mA", "yAmA"}, {}, {"yHm", "SHn", "yHz", "lAr", "m", "z", "zsHn", "yHz", "zlAr"}},
                {{"mA"}, {"yAcAk"}},
                {{"mA"}, {}, {"yHm", "SHn", "yHz", "lAr"}},
                {{"mA"}, {}},
                {{"mA"}, {}},
                {{"mA", "yAmA"}, {"m", "n", "k", "lAr", "z", "zsHn", "yHz", "zlAr"}},
                /*VBN*/{{"mHs"}, {"DH"}},
                {{}, {"DH", "yDH"}},
                {{"DH", "yDH", "mAktA"}, {"DHr"}},
                {{"mHs"}, {}},
                {{"DH", "yDH"}, {}},
                {{"n"}, {"mA"},{}},
                /*VBP*/{{}, {"m", "n", "k", "lAr", "yHm", "SHn", "yHz", "SHnHz"}},
                /*VBZ*/{{"Hr", "DHr"}, {}},
                /*IN*/{{"nHn", "DA", "nA", "yA", "ylA", "DAn", "nDA", "nDAn", "'Hn", "'nHn", "'DA", "'nA", "'yA", "'ylA", "'DAn"}, {"yDH", "DH"}},
                {{}, {"yDH", "DH"}},
                {{}, {"DHr"}},
                {{"nHn", "DA", "nA", "yA", "ylA", "DAn", "nDA", "nDAn", "'Hn", "'nHn", "'DA", "'nA", "'yA", "'ylA", "'DAn"}, {"DHr"}},
                {{"nHn", "DA", "nA", "yA", "ylA", "DAn", "nDA", "nDAn", "'Hn", "'nHn", "'DA", "'nA", "'yA", "'ylA", "'DAn"}, {}},
                {{}, {"DHr"}},
                {{"nHn", "DA", "nA", "yA", "ylA", "DAn", "nDA", "nDAn", "'Hn", "'nHn", "'DA", "'nA", "'yA", "'ylA", "'DAn"}, {"DHr"}},
                {{"nHn", "DA", "nA", "yA", "ylA", "DAn", "nDA", "nDAn", "'Hn", "'nHn", "'DA", "'nA", "'yA", "'ylA", "'DAn"}, {}},
                {{"nHn", "DA", "nA", "yA", "ylA", "DAn", "nDA", "nDAn", "'Hn", "'nHn", "'DA", "'nA", "'yA", "'ylA", "'DAn"}, {}},
                {{"nHn", "DA", "nA", "yA", "ylA", "DAn", "nDA", "nDAn", "'Hn", "'nHn", "'DA", "'nA", "'yA", "'ylA", "'DAn"}, {}},
                /*MD*/{{}, {"yAmA"}, {"zsHnHz", "zsHn", "zlAr", "z", "yHz"}},
                {{}, {"yAmA"}},
                {{"mAlH", "yAcAk", "yAbil"}, {"yHm", "SHn", "yHz", "lAr"}},
                {{"mAlH", "yAcAk", "yAbil"}, {}},
                {{"mAlH", "yAcAk", "yAbil"}, {}},
                {{"mAlH", "yAcAk", "yAbil"}, {"nHn", "DA", "nA", "yA", "ylA", "DAn", "nDA", "nDAn", "'Hn", "'nHn", "'DA", "'nA", "'yA", "'ylA", "'DAn"}},
                {{"mAlH", "yAcAk", "yAbil"}, {}},
                /*TO*/{{}, {"yAcAk", "yAbil"}, {}},
                {{}, {"yDH", "DH"}},
                {{"mAk"}, {"DHr"}},
                {{}, {"DHr"}},
                {{}, {"DHr"}},
                /*VBG*/{{"Hyor"}, {}},
                /*PRP$*/{{"Hm", "Hn", "sH", "HmHz", "H", "lArH"}, {"nHn", "DA", "nA", "yA", "ylA", "DAn", "nDA", "nDAn", "'Hn", "'nHn", "'DA", "'nA", "'yA", "'ylA", "'DAn", "yHncA", "DHkCA", "yken"}},
                {{"Hm", "Hn", "sH", "HmHz", "H", "lArH"}, {"yA", "nA", "'yA", "'nA"}},
                /*PRP*/{{"lHm", "yHm", "SHn", "SHnHz", "lAr"}, {}},
                {{"m", "n", "k", "lAr", "yHm", "SHn", "yHz", "SHnHz"}, {}},
                /*POS*/{{"nHn", "'nHn"}, {}},
                {{}, {"nHn", "DA", "nA", "yA", "ylA", "DAn", "nDA", "nDAn", "'Hn", "'nHn", "'DA", "'nA", "'yA", "'ylA", "'DAn", "yHncA", "DHkCA", "yken", "yArAk"}}
        };
        try {
            int previousParseSize = 0;
            for (int i = 0; i < parseNode.getLayerInfo().getNumberOfWords() - 1; i++){
                previousParseSize += parseNode.getLayerInfo().getMetamorphicParseAt(i).size();
            }
            for (int j = 0; j < posTags.length; j++) {
                boolean match = true;
                for (int k = 0; k < posTags[j].length && k < noneNodes.size(); k++) {
                    if (!noneNodes.get(k).getParent().getData().getName().equals(posTags[j][k])) {
                        match = false;
                        break;
                    }
                }
                if (match){
                    int[] indexList = searchMetaMorphemes(metaMorphemes[j], parseNode.getLayerInfo().getMetamorphicParseAt(parseNode.getLayerInfo().getNumberOfWords() - 1));
                    if (indexList != null){
                        for (int k = indexList.length - 1; k >= 0; k--){
                            if (indexList[k] != -1 && k < noneNodes.size()){
                                LayerInfo fromLayer = parseNode.getLayerInfo(), toLayer = noneNodes.get(k).getLayerInfo();
                                toLayer.setLayerData(ViewLayerType.META_MORPHEME_MOVED, fromLayer.getMetaMorphemeFromIndex(previousParseSize + indexList[k]));
                                fromLayer.metaMorphemeRemove(previousParseSize + indexList[k]);
                            }
                        }
                        break;
                    }
                }
            }
        } catch (WordNotExistsException | LayerNotExistsException | LayerItemNotExistsException e) {
            e.printStackTrace();
        }
    }

    private void metaMorphemeMoveWithPosTags(ParseNodeDrawable parseNode, ArrayList<ParseNodeDrawable> noneNodes){
        if (noneNodes.size() == 1){
            singleMetaMorphemeMoveWithPosTags(parseNode, noneNodes.get(0));
        } else {
            multipleMetaMorphemeMoveWithPosTags(parseNode, noneNodes);
        }
    }

    protected void metaMorphemeMoveWithRules(ParseTreeDrawable parseTree) {
        ArrayList<ParseNodeDrawable> noneNodes = new ArrayList<>();
        ParseNodeDrawable previousNode = null;
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            String word = parseNode.getLayerData(ViewLayerType.TURKISH_WORD);
            String parentData = parseNode.getParent().getData().getName();
            if (word.equals("*NONE*")){
                if (!parentData.equals("DT") && !parentData.equals("-NONE-") && !parentData.equals("RP")){
                    noneNodes.add(parseNode);
                }
            } else {
                if (!word.contains("*")){
                    if (previousNode != null && noneNodes.size() > 0){
                        metaMorphemeMoveWithPosTags(previousNode, noneNodes);
                        noneNodes.clear();
                    }
                    previousNode = parseNode;
                }
            }
        }
        if (previousNode != null && noneNodes.size() > 0){
            metaMorphemeMoveWithPosTags(previousNode, noneNodes);
            noneNodes.clear();
        }
    }
}
