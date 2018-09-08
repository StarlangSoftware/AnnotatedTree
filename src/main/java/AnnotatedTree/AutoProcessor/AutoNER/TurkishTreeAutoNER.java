package AnnotatedTree.AutoProcessor.AutoNER;

import AnnotatedSentence.ViewLayerType;
import Dictionary.Word;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.Processor.Condition.IsTurkishLeafNode;
import AnnotatedTree.Processor.NodeDrawableCollector;

import java.util.ArrayList;
import java.util.Locale;

public class TurkishTreeAutoNER extends TreeAutoNER {

    public TurkishTreeAutoNER(){
        super(ViewLayerType.TURKISH_WORD);
    }

    protected void autoDetectPerson(ParseTreeDrawable parseTree) {
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            if (!parseNode.layerExists(ViewLayerType.NER)){
                String word = parseNode.getLayerData(ViewLayerType.TURKISH_WORD).toLowerCase(new Locale("tr"));
                if (Word.isHonorific(word) && parseNode.getParent().getData().getName().equals("NNP")){
                    parseNode.getLayerInfo().setLayerData(ViewLayerType.NER, "PERSON");
                }
                parseNode.checkGazetteer(personGazetteer, word);
            }
        }
    }

    protected void autoDetectLocation(ParseTreeDrawable parseTree) {
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            if (!parseNode.layerExists(ViewLayerType.NER)){
                String word = parseNode.getLayerData(ViewLayerType.TURKISH_WORD).toLowerCase(new Locale("tr"));
                parseNode.checkGazetteer(locationGazetteer, word);
            }
        }
    }

    protected void autoDetectOrganization(ParseTreeDrawable parseTree) {
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            if (!parseNode.layerExists(ViewLayerType.NER)){
                String word = parseNode.getLayerData(ViewLayerType.TURKISH_WORD).toLowerCase(new Locale("tr"));
                if (Word.isOrganization(word)){
                    parseNode.getLayerInfo().setLayerData(ViewLayerType.NER, "ORGANIZATION");
                }
                parseNode.checkGazetteer(organizationGazetteer, word);
            }
        }
    }

    protected void autoDetectMoney(ParseTreeDrawable parseTree) {
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (int i = 0; i < leafList.size(); i++) {
            ParseNodeDrawable parseNode = leafList.get(i);
            if (!parseNode.layerExists(ViewLayerType.NER)){
                String word = parseNode.getLayerData(ViewLayerType.TURKISH_WORD).toLowerCase(new Locale("tr"));
                if (Word.isMoney(word)) {
                    parseNode.getLayerInfo().setLayerData(ViewLayerType.NER, "MONEY");
                    int j = i - 1;
                    while (j >= 0){
                        ParseNodeDrawable previous = leafList.get(j);
                        if (previous.getParent().getData().getName().equals("CD")){
                            previous.getLayerInfo().setLayerData(ViewLayerType.NER, "MONEY");
                        } else {
                            break;
                        }
                        j--;
                    }
                }
            }
        }
    }

    protected void autoDetectTime(ParseTreeDrawable parseTree) {
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTurkishLeafNode());
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (int i = 0; i < leafList.size(); i++){
            ParseNodeDrawable parseNode = leafList.get(i);
            if (!parseNode.layerExists(ViewLayerType.NER)){
                String word = parseNode.getLayerData(ViewLayerType.TURKISH_WORD).toLowerCase(new Locale("tr"));
                if (Word.isTime(word)){
                    parseNode.getLayerInfo().setLayerData(ViewLayerType.NER, "TIME");
                    if (i > 0){
                        ParseNodeDrawable previous = leafList.get(i - 1);
                        if (previous.getParent().getData().getName().equals("CD")){
                            previous.getLayerInfo().setLayerData(ViewLayerType.NER, "TIME");
                        }
                    }
                }
            }
        }
    }
}
