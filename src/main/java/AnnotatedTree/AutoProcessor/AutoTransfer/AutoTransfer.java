package AnnotatedTree.AutoProcessor.AutoTransfer;

import AnnotatedSentence.ViewLayerType;
import Dictionary.Word;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.AutoProcessor.AutoTranslation.AutoPreprocessor;

public abstract class AutoTransfer {
    protected ViewLayerType secondLanguage;
    protected AutoPreprocessor autoPreprocessor;

    protected abstract void autoTransferSameWords(ParseTreeDrawable parseTree, TransferredSentence sentence);
    protected abstract void autoTransferSingles(ParseTreeDrawable parseTree, TransferredSentence sentence);
    protected abstract void autoTransferWordsOnInterval(ParseTreeDrawable parseTree, TransferredSentence sentence);

    protected AutoTransfer(ViewLayerType secondLanguage){
        this.secondLanguage = secondLanguage;
    }

    public void autoTransfer(ParseTreeDrawable parseTree, TransferredSentence sentence){
        autoPreprocessor.autoFillPunctuation(parseTree);
        for (int i = 0; i < sentence.wordCount(); i++){
            String data = sentence.getWord(i).getName();
            if (Word.isPunctuation(data) && !data.equals("$")){
                sentence.transfer(i);
            }
        }
        autoPreprocessor.autoFillNullElements(parseTree);
        autoPreprocessor.autoFillWithNoneTags(parseTree);
        autoPreprocessor.autoSwap(parseTree);
        autoTransferSameWords(parseTree, sentence);
        autoTransferSingles(parseTree, sentence);
        autoTransferWordsOnInterval(parseTree, sentence);
    }

}
