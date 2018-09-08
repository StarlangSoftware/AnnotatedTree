package AnnotatedTree.AutoProcessor.AutoTranslation.PartOfSpeech;

import Dictionary.TxtWord;
import MorphologicalAnalysis.Transition;
import AnnotatedTree.ParseNodeDrawable;

import java.util.List;

public class TurkishPartOfSpeechTranslator implements PartOfSpeechTranslator{

    protected ParseNodeDrawable parseNode;
    protected List<String> parentList;
    protected List<String> englishWordList;
    protected String prefix;
    protected String lastWordForm;
    protected TxtWord lastWord;

    public TurkishPartOfSpeechTranslator(ParseNodeDrawable parseNode, List<String> parentList, List<String> englishWordList, String prefix, String lastWordForm, TxtWord lastWord){
        this.parseNode = parseNode;
        this.parentList = parentList;
        this.englishWordList = englishWordList;
        this.prefix = prefix;
        this.lastWordForm = lastWordForm;
        this.lastWord = lastWord;
    }

    protected String addSuffix(String suffix, String prefix, TxtWord root, String stem){
        Transition transition;
        transition = new Transition(suffix);
        return prefix + transition.makeTransition(root, stem);
    }

    public String translate(){
        return "";
    }

}
