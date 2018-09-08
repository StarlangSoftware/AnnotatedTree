package AnnotatedTree.AutoProcessor.AutoSemantic;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedSentence.ViewLayerType;
import MorphologicalAnalysis.FsmMorphologicalAnalyzer;
import AnnotatedTree.*;
import AnnotatedTree.Processor.Condition.IsTurkishLeafNode;
import AnnotatedTree.Processor.NodeDrawableCollector;
import WordNet.*;

import java.util.ArrayList;

public class TurkishTreeAutoSemantic extends TreeAutoSemantic {
    private WordNet turkishWordNet;
    private FsmMorphologicalAnalyzer fsm;

    public TurkishTreeAutoSemantic(WordNet turkishWordNet, FsmMorphologicalAnalyzer fsm){
        this.turkishWordNet = turkishWordNet;
        this.fsm = fsm;
    }

    protected boolean autoLabelSingleSemantics(ParseTreeDrawable parseTree) {
        boolean modified = false;
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            LayerInfo info = parseNode.getLayerInfo();
            if (info.getLayerData(ViewLayerType.INFLECTIONAL_GROUP) != null){
                try {
                    ArrayList<SynSet>[] meanings = new ArrayList[info.getNumberOfWords()];
                    for (int i = 0; i < info.getNumberOfWords(); i++){
                        meanings[i] = turkishWordNet.constructSynSets(info.getMorphologicalParseAt(i).getWord().getName(), info.getMorphologicalParseAt(i), info.getMetamorphicParseAt(i), fsm);
                    }
                    switch (info.getNumberOfWords()){
                        case 1:
                            if (meanings[0].size() == 1){
                                modified = true;
                                parseNode.getLayerInfo().setLayerData(ViewLayerType.SEMANTICS, meanings[0].get(0).getId());
                            }
                            break;
                        case 2:
                            if (meanings[0].size() == 1 && meanings[1].size() == 1){
                                modified = true;
                                parseNode.getLayerInfo().setLayerData(ViewLayerType.SEMANTICS, meanings[0].get(0).getId() + "$" + meanings[1].get(0).getId());
                            }
                            break;
                        case 3:
                            if (meanings[0].size() == 1 && meanings[1].size() == 1 && meanings[2].size() == 1){
                                modified = true;
                                parseNode.getLayerInfo().setLayerData(ViewLayerType.SEMANTICS, meanings[0].get(0).getId() + "$" + meanings[1].get(0).getId() + "$" + meanings[2].get(0).getId());
                            }
                            break;
                    }
                } catch (LayerNotExistsException | WordNotExistsException e) {
                    e.printStackTrace();
                }
            }
        }
        return modified;
    }
}
