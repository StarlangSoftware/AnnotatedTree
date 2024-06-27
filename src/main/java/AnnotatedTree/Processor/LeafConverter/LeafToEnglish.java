package AnnotatedTree.Processor.LeafConverter;

import AnnotatedSentence.ViewLayerType;

public class LeafToEnglish extends LeafToLanguageConverter {

    /**
     * Constructor for LeafToEnglish. Sets viewLayerType to ENGLISH.
     */
    public LeafToEnglish(){
        viewLayerType = ViewLayerType.ENGLISH_WORD;
    }

}
