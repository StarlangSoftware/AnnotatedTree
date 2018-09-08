package AnnotatedTree.AutoProcessor.AutoTransfer;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseTreeDrawable;

public class PersianAutoTransfer extends AutoTransfer{

    public PersianAutoTransfer() {
        super(ViewLayerType.PERSIAN_WORD);
    }

    protected void autoTransferSameWords(ParseTreeDrawable parseTree, TransferredSentence sentence) {

    }

    protected void autoTransferSingles(ParseTreeDrawable parseTree, TransferredSentence sentence) {

    }

    protected void autoTransferWordsOnInterval(ParseTreeDrawable parseTree, TransferredSentence sentence){

    }

}
