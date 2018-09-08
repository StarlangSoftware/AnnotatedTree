package AnnotatedTree.AutoProcessor.AutoTranslation.PartOfSpeech;

import AnnotatedSentence.ViewLayerType;
import Dictionary.TxtDictionary;
import Dictionary.TxtWord;
import AnnotatedTree.ParseNodeDrawable;

import java.util.List;

public class TurkishNounTranslator extends TurkishPartOfSpeechTranslator{
    protected TxtDictionary txtDictionary;

    public TurkishNounTranslator(ParseNodeDrawable parseNode, List<String> parentList, List<String> englishWordList, String prefix, String lastWordForm, TxtWord lastWord, TxtDictionary txtDictionary) {
        super(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord);
        this.txtDictionary = txtDictionary;
    }

    protected String translateNouns(String[] posArray, String[][] wordArray, String[][] suffixArray, List<String> parentList, List<String> englishWordList, String prefix, TxtWord currentRoot, String nounRoot){
        for (int i = 0; i < posArray.length; i++){
            if (parentList.get(1).equals(posArray[i])){
                if (wordArray[i].length == 0){
                    return addSuffix(suffixArray[i][0], prefix, currentRoot, nounRoot);
                } else {
                    for (int j = 0; j < wordArray[i].length; j++){
                        if (englishWordList.get(1).equalsIgnoreCase(wordArray[i][j])){
                            return addSuffix(suffixArray[i][j], prefix, currentRoot, nounRoot);
                        }
                    }
                }
            }
        }
        return null;
    }

    protected boolean isLastWordOfNounPhrase(ParseNodeDrawable parseNode){
        ParseNodeDrawable parent = (ParseNodeDrawable) parseNode.getParent();
        ParseNodeDrawable grandParent = (ParseNodeDrawable) parent.getParent();
        ParseNodeDrawable next = (ParseNodeDrawable) parseNode.nextSibling();
        ParseNodeDrawable previous = (ParseNodeDrawable) parseNode.previousSibling();
        if (parent.isLastChild(parseNode)){
            if (previous != null && previous.getData().getName().startsWith("J") && previous.lastChild().isLeaf()){
                String word = ((ParseNodeDrawable) previous.lastChild()).getLayerData(ViewLayerType.TURKISH_WORD);
                if (word != null && txtDictionary.getWord(word) != null && ((TxtWord)txtDictionary.getWord(word)).isNominal()){
                    return true;
                }
            }
            if (previous != null && previous.getData().getName().startsWith("N")){
                return true;
            } else {
                if (grandParent != null && grandParent.isLastChild(parent) && grandParent.numberOfChildren() == 2){
                    ParseNodeDrawable parentPrevious = (ParseNodeDrawable) parent.previousSibling();
                    if (parentPrevious.getData().getName().equals("PP") && parentPrevious.lastChild().getData().getName().equals("IN")){
                        ParseNodeDrawable inNode = (ParseNodeDrawable) parentPrevious.lastChild().lastChild();
                        if (inNode != null && inNode.getLayerData(ViewLayerType.ENGLISH_WORD) != null && inNode.getLayerData(ViewLayerType.ENGLISH_WORD).equals("of")){
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } else {
            if (next != null && previous != null){
                return !(next.getData().getName().startsWith("N")) && previous.getData().getName().startsWith("N");
            } else {
                return false;
            }
        }
    }

}
