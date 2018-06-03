package AnnotatedTree.AutoProcessor.AutoNER;

import AnnotatedSentence.ViewLayerType;
import NamedEntityRecognition.AutoNER;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.Processor.Condition.IsTransferable;
import AnnotatedTree.Processor.NodeDrawableCollector;

import java.util.ArrayList;

public abstract class TreeAutoNER extends AutoNER{
    protected ViewLayerType secondLanguage;

    protected abstract void autoDetectPerson(ParseTreeDrawable parseTree);
    protected abstract void autoDetectLocation(ParseTreeDrawable parseTree);
    protected abstract void autoDetectOrganization(ParseTreeDrawable parseTree);
    protected abstract void autoDetectMoney(ParseTreeDrawable parseTree);
    protected abstract void autoDetectTime(ParseTreeDrawable parseTree);

    protected TreeAutoNER(ViewLayerType secondLanguage){
        this.secondLanguage = secondLanguage;
    }

    public void autoNER(ParseTreeDrawable parseTree){
        autoDetectPerson(parseTree);
        autoDetectLocation(parseTree);
        autoDetectOrganization(parseTree);
        autoDetectMoney(parseTree);
        autoDetectTime(parseTree);
        NodeDrawableCollector nodeDrawableCollector = new NodeDrawableCollector((ParseNodeDrawable) parseTree.getRoot(), new IsTransferable(secondLanguage));
        ArrayList<ParseNodeDrawable> leafList = nodeDrawableCollector.collect();
        for (ParseNodeDrawable parseNode : leafList){
            if (!parseNode.layerExists(ViewLayerType.NER)){
                parseNode.getLayerInfo().setLayerData(ViewLayerType.NER, "NONE");
            }
        }
        parseTree.save();
    }

}
