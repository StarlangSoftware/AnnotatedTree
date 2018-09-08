package AnnotatedTree.AutoProcessor.AutoTransfer;

import Corpus.Sentence;

import java.io.File;
import java.util.ArrayList;

public class TransferredSentence extends Sentence{
    protected ArrayList<Boolean> isTransferred;

    public TransferredSentence(File file){
        super(file);
        isTransferred = new ArrayList<>();
        for (int i = 0; i < words.size(); i++){
            isTransferred.add(false);
        }
    }

    public void transfer(int index){
        isTransferred.set(index, true);
    }

    public boolean isTransferred(int index){
        return isTransferred.get(index);
    }

}
