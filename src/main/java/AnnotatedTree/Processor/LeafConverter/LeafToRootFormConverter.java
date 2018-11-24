package AnnotatedTree.Processor.LeafConverter;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedTree.*;
import AnnotatedTree.Processor.TreeToStringConverter;
import Corpus.Corpus;
import Corpus.Sentence;

import java.io.File;

public class LeafToRootFormConverter implements LeafToStringConverter  {

    @Override
    public String leafConverter(ParseNodeDrawable parseNodeDrawable) {
        LayerInfo layerInfo = parseNodeDrawable.getLayerInfo();
        int nrOfWords = 0;
        try {
            nrOfWords = layerInfo.getNumberOfWords();
        } catch (LayerNotExistsException e) {
            //throw new RuntimeException("Could not get layerinfo.",e);
        }

        //surface conversion for backup plan
        //String surface = new LeafToTurkish().leafConverter(parseNodeDrawable);

        //get the root form
        String rootWords = " ";
        try {
            for (int i = 0; i < nrOfWords; i++) {
                String root = layerInfo.getMorphologicalParseAt(i).getWord().getName();
                if (root != null && !root.isEmpty()){
                    rootWords = rootWords + " " + root;
                }
            }
        } catch (LayerNotExistsException | WordNotExistsException e) {
            //e.printStackTrace();
        }
        return rootWords;
    }

    //Sample usage.
    public static void main(String[] args) throws Exception {
        TreesToFlatSentences();
    }

    public static void TreesToFlatSentences(){
        String dataFolder =  "data/LeafToRootSampleCorpus/";
        TreeBankDrawable treeBank = new TreeBankDrawable(new File(dataFolder));
        Corpus corpusSurface = new Corpus();
        Corpus corpusRoot = new Corpus();
        int skipped = 0;
        for (int i = 0; i < treeBank.size(); i++){      //each sentence tree
            try {
                ParseTreeDrawable tree = treeBank.get(i);

                //surface
                TreeToStringConverter converter = new TreeToStringConverter(tree, new LeafToTurkish());
                String str = converter.convert();
                Sentence sSurface = new Sentence(str);

                //root
                TreeToStringConverter converter2 = new TreeToStringConverter(tree, new LeafToRootFormConverter());
                String str2 = converter2.convert();
                Sentence sRoot = new Sentence(str2);

                //filter out
                if (sRoot.wordCount() == sSurface.wordCount()) {            //Aynı sayıda kelime içermiyorsa geç.
                    corpusRoot.addSentence(sRoot);
                    corpusSurface.addSentence(sSurface);
                }else{
                    System.out.println("\nSentence skipped due to the word count mismatch!. Tree: " + tree.getName());
                    System.out.println("Surface:" + str);
                    System.out.println("Root:" + str2);
                    skipped++;
                }
            }
            catch (Exception e){
                skipped++;
            }
        }

        //persist
        String surfacePath = "Corpus.txt";
        corpusSurface.writeToFile(surfacePath);
        String rootPath = "CorpusRoot.txt";
        corpusRoot.writeToFile(rootPath);
    }

}
