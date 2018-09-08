package AnnotatedTree.AutoProcessor.AutoTranslation.PartOfSpeech;

import Dictionary.TxtWord;
import MorphologicalAnalysis.Transition;
import AnnotatedTree.ParseNodeDrawable;

import java.util.List;

public class TurkishVerbTranslator extends TurkishPartOfSpeechTranslator{

    public TurkishVerbTranslator(ParseNodeDrawable parseNode, List<String> parentList, List<String> englishWordList, String prefix, String lastWordForm, TxtWord lastWord) {
        super(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord);
    }

    protected String personalSuffix1(String personalPronoun){
        switch (personalPronoun){
            case "i":
                return "Hm";
            case "you":
                return "SHn";
            case "we":
                return "Hz";
            case "they":
                return "lAr";
        }
        return "";
    }

    protected String personalSuffix2(String personalPronoun){
        switch (personalPronoun){
            case "i":
                return "m";
            case "you":
                return "n";
            case "we":
                return "k";
            case "they":
                return "lAr";
        }
        return "";
    }

    protected String personalSuffix3(String personalPronoun){
        switch (personalPronoun){
            case "i":
                return "yHm";
            case "you":
                return "SHn";
            case "we":
                return "yHz";
            case "they":
                return "lAr";
        }
        return "";
    }

    protected String personalSuffix4(String personalPronoun){
        switch (personalPronoun){
            case "i":
                return "m";
            case "you":
                return "zSHn";
            case "we":
                return "yHz";
            case "they":
                return "zlAr";
        }
        return "";
    }

    protected String addSuffix(String suffix, String prefix, TxtWord root, String stem, String personalPronoun){
        Transition transition;
        transition = new Transition(suffix + personalSuffix2(personalPronoun));
        return prefix + transition.makeTransition(root, stem);
    }

    protected String addPassiveSuffix(String suffix1, String suffix2, String suffix3, String prefix, TxtWord root, String stem){
        Transition transition;
        switch (root.verbType()){
            case "F4PW"://"nDH"
            case "F4PW-NO-REF":
                transition = new Transition(suffix1);
                break;
            case "F5PR-NO-REF"://"DH"
            case "F5PL-NO-REF":
                transition = new Transition(suffix2);
                break;
            case "F4PR":
            case "F5PR":
            default://"HlDH"
                transition = new Transition(suffix3);
                break;
        }
        return prefix + transition.makeTransition(root, stem);
    }

    protected boolean isLastWordOfVerbAsNoun(ParseNodeDrawable parseNode){
        ParseNodeDrawable parent = (ParseNodeDrawable) parseNode.getParent();
        ParseNodeDrawable grandParent = (ParseNodeDrawable) parent.getParent();
        if (parent.isLastChild(parseNode) && parent.getData().getName().equals("VP")){
            if (grandParent != null && grandParent.getData().getName().equals("NP") && grandParent.getChild(0).equals(parent)
                    && grandParent.numberOfChildren() == 2 && grandParent.getChild(1).getData().getName().equals("NP")){
                return true;
            }
        }
        return false;
    }

}
