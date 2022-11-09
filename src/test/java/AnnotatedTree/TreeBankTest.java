package AnnotatedTree;

import AnnotatedSentence.AnnotatedCorpus;
import AnnotatedSentence.ViewLayerType;

import java.io.File;

public class TreeBankTest {

    public void testPenn() {
        TreeBankDrawable treeBank = new TreeBankDrawable(new File("../../Penn-Treebank/Turkish/"));
        AnnotatedCorpus corpus = treeBank.createAnnotatedCorpus();
        corpus.exportSequenceDataSet("disambiguation-penn.txt", ViewLayerType.INFLECTIONAL_GROUP);
        corpus.exportSequenceDataSet("metamorpheme-penn.txt", ViewLayerType.META_MORPHEME);
        corpus.exportSequenceDataSet("postag-penn.txt", ViewLayerType.POS_TAG);
        corpus.exportSequenceDataSet("ner-penn.txt", ViewLayerType.NER);
        corpus.exportSequenceDataSet("semantics-penn.txt", ViewLayerType.SEMANTICS);
        corpus.exportSequenceDataSet("semanticrolelabeling-penn.txt", ViewLayerType.PROPBANK);
    }

}
