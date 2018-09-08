package AnnotatedTree.Statistics;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedSentence.Statistics.LayerStatistics;
import AnnotatedSentence.ViewLayerType;
import DataStructure.CounterHashMap;
import AnnotatedTree.*;
import AnnotatedTree.Processor.Condition.IsLeafNode;
import AnnotatedTree.Processor.NodeDrawableCollector;
import ParseTree.TreeBank;

import java.util.ArrayList;
import java.util.function.BiFunction;

public class TreeLayerStatistics extends LayerStatistics{
    private TreeBank treeBank;

    public TreeLayerStatistics(TreeBank treeBank){
        this.treeBank = treeBank;
        counts = new CounterHashMap<>();
    }

    private void calculateStatisticsWithParse(BiFunction<LayerInfo, Integer, String> wordProperty){
        counts = new CounterHashMap<>();
        for (int i = 0; i < treeBank.size(); i++) {
            ParseTreeDrawable parseTree = (ParseTreeDrawable) treeBank.get(i);
            NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsLeafNode());
            ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
            for (ParseNodeDrawable leafNode : leafList) {
                LayerInfo layerInfo = leafNode.getLayerInfo();
                try {
                    for (int j = 0; j < layerInfo.getNumberOfWords(); j++) {
                        String info = wordProperty.apply(layerInfo, j);
                        if (info != null){
                            counts.put(info);
                        }
                    }
                } catch (LayerNotExistsException e) {
                }
            }
        }
    }

    private void calculateStatisticsWithLayerInfo(ViewLayerType viewLayerType){
        counts = new CounterHashMap<>();
        for (int i = 0; i < treeBank.size(); i++) {
            ParseTreeDrawable parseTree = (ParseTreeDrawable) treeBank.get(i);
            NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsLeafNode());
            ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
            for (ParseNodeDrawable leafNode : leafList) {
                LayerInfo layerInfo = leafNode.getLayerInfo();
                if (leafNode.layerExists(viewLayerType)) {
                    switch (viewLayerType) {
                        case ENGLISH_WORD:
                            counts.put(layerInfo.getLayerData(ViewLayerType.ENGLISH_WORD));
                            break;
                        case ENGLISH_SEMANTICS:
                            counts.put(layerInfo.getLayerData(ViewLayerType.ENGLISH_SEMANTICS));
                            break;
                    }
                }
            }
        }
    }

    public void calculateStatistics(ViewLayerType viewLayerType){
        if (viewLayerType.equals(ViewLayerType.ENGLISH_WORD) || viewLayerType.equals(ViewLayerType.ENGLISH_SEMANTICS)){
            calculateStatisticsWithLayerInfo(viewLayerType);
        } else {
            switch (viewLayerType){
                case NER:
                    calculateStatisticsWithParse((layerInfo, integer) -> layerInfo.getLayerData(ViewLayerType.NER));
                    break;
                case PROPBANK:
                    calculateStatisticsWithParse((layerInfo, integer) -> layerInfo.getLayerData(ViewLayerType.PROPBANK));
                    break;
                case INFLECTIONAL_GROUP:
                    calculateStatisticsWithParse((layerInfo, integer) -> {
                        try {
                            return layerInfo.getMorphologicalParseAt(integer).toString();
                        } catch (LayerNotExistsException | WordNotExistsException e) {
                            return null;
                        }
                    });
                    break;
                case TURKISH_WORD:
                    calculateStatisticsWithParse((layerInfo, integer) -> {
                        try {
                            return layerInfo.getTurkishWordAt(integer);
                        } catch (LayerNotExistsException | WordNotExistsException e) {
                            return null;
                        }
                    });
                    break;
                case SEMANTICS:
                    calculateStatisticsWithParse((layerInfo, integer) -> {
                        try {
                            return layerInfo.getSemanticAt(integer);
                        } catch (LayerNotExistsException | WordNotExistsException e) {
                            return null;
                        }
                    });
                    break;
                case META_MORPHEME:
                    calculateStatisticsWithParse((layerInfo, integer) -> {
                        try {
                            return layerInfo.getMetamorphicParseAt(integer).toString();
                        } catch (LayerNotExistsException | WordNotExistsException e) {
                            return null;
                        }
                    });
                    break;
            }
        }
    }

    public void calculatePosStatistics(){
        calculateStatisticsWithParse((layerInfo, integer) -> {
            try {
                return layerInfo.getMorphologicalParseAt(integer).getPos();
            } catch (LayerNotExistsException | WordNotExistsException e) {
                return null;
            }
        });
    }

    public void calculateRootPosStatistics(){
        calculateStatisticsWithParse((layerInfo, integer) -> {
            try {
                return layerInfo.getMorphologicalParseAt(integer).getRootPos();
            } catch (LayerNotExistsException | WordNotExistsException e) {
                return null;
            }
        });
    }

    public void calculateRootWithPosStatistics(){
        calculateStatisticsWithParse((layerInfo, integer) -> {
            try {
                return layerInfo.getMorphologicalParseAt(integer).getWordWithPos().getName();
            } catch (LayerNotExistsException | WordNotExistsException e) {
                return null;
            }
        });
    }

    public void calculateRootWordStatistics(){
        calculateStatisticsWithParse((layerInfo, integer) -> {
            try {
                return layerInfo.getMorphologicalParseAt(integer).getWord().getName();
            } catch (LayerNotExistsException | WordNotExistsException e) {
                return null;
            }
        });
    }

}
