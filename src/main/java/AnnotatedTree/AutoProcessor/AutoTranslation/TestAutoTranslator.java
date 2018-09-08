package AnnotatedTree.AutoProcessor.AutoTranslation;

import AnnotatedSentence.ViewLayerType;
import ParseTree.ParseNode;
import ParseTree.ParseTree;
import Translation.AutomaticTranslationDictionary;
import Translation.BilingualDictionary;
import Dictionary.EnglishWordComparator;
import AnnotatedTree.*;
import AnnotatedTree.Processor.LeafConverter.LeafToTurkish;
import AnnotatedTree.Processor.NodeModification.ConvertToLayeredFormat;
import AnnotatedTree.Processor.TreeModifier;
import AnnotatedTree.Processor.TreeToStringConverter;
import Sampling.KFoldCrossValidation;
import Translation.BleuMeasure;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;

public class TestAutoTranslator {

    public static void translateForBleu(String dataFolder, String pattern, String outputFile){
        TurkishAutoTranslator translator;
        PrintWriter translated, correct;
        TreeToStringConverter treeToStringConverter;
        ParallelTreeBankDrawable treeBank = new ParallelTreeBankDrawable(new File(dataFolder + "/English"), new File(dataFolder + "/Turkish"), "." + pattern);
        KFoldCrossValidation<ParseTree> fromCrossValidation, toCrossValidation;
        fromCrossValidation = new KFoldCrossValidation<>(treeBank.fromTreeBank().getParseTrees(), 10, 1);
        toCrossValidation = new KFoldCrossValidation<>(treeBank.toTreeBank().getParseTrees(), 10, 1);
        System.out.println("Parallel Treebank read. Now translating...");
        try {
            for (int k = 0; k < 10; k++){
                translated = new PrintWriter(new File("translated" + outputFile + "-" + pattern + ".txt"));
                correct = new PrintWriter(new File("correct" + outputFile + "-" + pattern + ".txt"));
                TreeBankDrawable fromTestTreeBank = new TreeBankDrawable(fromCrossValidation.getTestFold(k));
                TreeBankDrawable toTrainTreeBank = new TreeBankDrawable(toCrossValidation.getTrainFold(k));
                TreeBankDrawable toTestTreeBank = new TreeBankDrawable(toCrossValidation.getTestFold(k));
                toTrainTreeBank.prepareTranslationDictionary(ViewLayerType.ENGLISH_WORD, ViewLayerType.TURKISH_WORD, "tmpdictionary.xml");
                translator = new TurkishAutoTranslator(new AutomaticTranslationDictionary("tmpdictionary.xml", new EnglishWordComparator()), new BilingualDictionary("Data/Dictionary/english-turkish.xml", new EnglishWordComparator()));
                for (int i = 0; i < fromTestTreeBank.size(); i++){
                    ParseTreeDrawable parseTree = fromTestTreeBank.get(i);
                    TreeModifier treeModifier = new TreeModifier(parseTree, new ConvertToLayeredFormat());
                    treeModifier.modify();
                    translator.autoTranslate(parseTree);
                    parseTree.toSentence();
                    treeToStringConverter = new TreeToStringConverter(parseTree, new LeafToTurkish());
                    String translatedSentence = treeToStringConverter.convert().toLowerCase(new Locale("tr"));
                    translated.println(translatedSentence);
                    ParseTreeDrawable correctTree = toTestTreeBank.get(i);
                    treeToStringConverter = new TreeToStringConverter(correctTree, new LeafToTurkish());
                    String correctSentence = treeToStringConverter.convert().toLowerCase(new Locale("tr"));
                    correct.println(correctSentence);
                }
                translated.close();
                correct.close();
                calculateBleu("", "");
            }
        } catch (FileNotFoundException e) {
        }
    }

    public static void translateBaseLineForBleu(String dataFolder, String pattern, String outputFile){
        BaseLineTurkishAutoTranslator translator;
        PrintWriter translated, correct;
        TreeToStringConverter treeToStringConverter;
        ParallelTreeBankDrawable treeBank = new ParallelTreeBankDrawable(new File(dataFolder + "/English"), new File(dataFolder + "/Turkish"), "." + pattern);
        KFoldCrossValidation<ParseTree> fromCrossValidation, toCrossValidation;
        fromCrossValidation = new KFoldCrossValidation<>(treeBank.fromTreeBank().getParseTrees(), 10, 1);
        toCrossValidation = new KFoldCrossValidation<>(treeBank.toTreeBank().getParseTrees(), 10, 1);
        System.out.println("Parallel Treebank read. Now translating...");
        try {
            for (int k = 0; k < 10; k++){
                translated = new PrintWriter(new File("translated" + outputFile + "-" + pattern + ".txt"));
                correct = new PrintWriter(new File("correct" + outputFile + "-" + pattern + ".txt"));
                TreeBankDrawable fromTestTreeBank = new TreeBankDrawable(fromCrossValidation.getTestFold(k));
                TreeBankDrawable toTrainTreeBank = new TreeBankDrawable(toCrossValidation.getTrainFold(k));
                TreeBankDrawable toTestTreeBank = new TreeBankDrawable(toCrossValidation.getTestFold(k));
                toTrainTreeBank.prepareTranslationDictionary(ViewLayerType.ENGLISH_WORD, ViewLayerType.TURKISH_WORD, "tmpdictionary.xml");
                translator = new BaseLineTurkishAutoTranslator(new AutomaticTranslationDictionary("tmpdictionary.xml", new EnglishWordComparator()), new BilingualDictionary("Data/Dictionary/english-turkish.xml", new EnglishWordComparator()));
                for (int i = 0; i < fromTestTreeBank.size(); i++){
                    ParseTreeDrawable parseTree = fromTestTreeBank.get(i);
                    TreeModifier treeModifier = new TreeModifier(parseTree, new ConvertToLayeredFormat());
                    treeModifier.modify();
                    translator.autoTranslate(parseTree);
                    parseTree.toSentence();
                    treeToStringConverter = new TreeToStringConverter(parseTree, new LeafToTurkish());
                    String translatedSentence = treeToStringConverter.convert().toLowerCase(new Locale("tr"));
                    translated.println(translatedSentence);
                    ParseTreeDrawable correctTree = toTestTreeBank.get(i);
                    treeToStringConverter = new TreeToStringConverter(correctTree, new LeafToTurkish());
                    String correctSentence = treeToStringConverter.convert().toLowerCase(new Locale("tr"));
                    correct.println(correctSentence);
                }
                translated.close();
                correct.close();
                calculateBleu("", "");
            }
        } catch (FileNotFoundException e) {
        }
    }

    public static void structureAgreement(String dataFolder, String pattern){
        TurkishAutoTranslator translator;
        ParallelTreeBankDrawable treeBank = new ParallelTreeBankDrawable(new File(dataFolder + "/English"), new File(dataFolder + "/Turkish"), "." + pattern);
        KFoldCrossValidation<ParseTree> fromCrossValidation, toCrossValidation;
        fromCrossValidation = new KFoldCrossValidation<>(treeBank.fromTreeBank().getParseTrees(), 10, 1);
        toCrossValidation = new KFoldCrossValidation<>(treeBank.toTreeBank().getParseTrees(), 10, 1);
        System.out.println("Parallel Treebank read. Now translating...");
        for (int k = 0; k < 10; k++){
            int total = 0, agreement = 0;
            TreeBankDrawable fromTestTreeBank = new TreeBankDrawable(fromCrossValidation.getTestFold(k));
            TreeBankDrawable toTrainTreeBank = new TreeBankDrawable(toCrossValidation.getTrainFold(k));
            TreeBankDrawable toTestTreeBank = new TreeBankDrawable(toCrossValidation.getTestFold(k));
            toTrainTreeBank.prepareTranslationDictionary(ViewLayerType.ENGLISH_WORD, ViewLayerType.TURKISH_WORD, "tmpdictionary.xml");
            translator = new TurkishAutoTranslator(new AutomaticTranslationDictionary("tmpdictionary.xml", new EnglishWordComparator()), new BilingualDictionary("Data/Dictionary/english-turkish.xml", new EnglishWordComparator()));
            for (int i = 0; i < fromTestTreeBank.size(); i++){
                ParseTreeDrawable parseTree = fromTestTreeBank.get(i);
                TreeModifier treeModifier = new TreeModifier(parseTree, new ConvertToLayeredFormat());
                treeModifier.modify();
                translator.autoTranslate(parseTree);
                ParseTreeDrawable correctTree = toTestTreeBank.get(i);
                correctTree.structureAgreementCount(parseTree);
                total += correctTree.nodeCountWithMultipleChildren();
                agreement += correctTree.structureAgreementCount(parseTree);
            }
            System.out.println(agreement / (total + 0.0));
        }
    }

    public static void noneReplacement(String dataFolder, String pattern){
        TurkishAutoTranslator translator;
        ParallelTreeBankDrawable treeBank = new ParallelTreeBankDrawable(new File(dataFolder + "/English"), new File(dataFolder + "/Turkish"), "." + pattern);
        KFoldCrossValidation<ParseTree> fromCrossValidation, toCrossValidation;
        fromCrossValidation = new KFoldCrossValidation<>(treeBank.fromTreeBank().getParseTrees(), 10, 1);
        toCrossValidation = new KFoldCrossValidation<>(treeBank.toTreeBank().getParseTrees(), 10, 1);
        System.out.println("Parallel Treebank read. Now translating...");
        for (int k = 0; k < 10; k++){
            int tn = 0, tp = 0, fp = 0, fn = 0;
            TreeBankDrawable fromTestTreeBank = new TreeBankDrawable(fromCrossValidation.getTestFold(k));
            TreeBankDrawable toTrainTreeBank = new TreeBankDrawable(toCrossValidation.getTrainFold(k));
            TreeBankDrawable toTestTreeBank = new TreeBankDrawable(toCrossValidation.getTestFold(k));
            toTrainTreeBank.prepareTranslationDictionary(ViewLayerType.ENGLISH_WORD, ViewLayerType.TURKISH_WORD, "tmpdictionary.xml");
            translator = new TurkishAutoTranslator(new AutomaticTranslationDictionary("tmpdictionary.xml", new EnglishWordComparator()), new BilingualDictionary("Data/Dictionary/english-turkish.xml", new EnglishWordComparator()));
            for (int i = 0; i < fromTestTreeBank.size(); i++){
                ParseTreeDrawable parseTree = fromTestTreeBank.get(i);
                TreeModifier treeModifier = new TreeModifier(parseTree, new ConvertToLayeredFormat());
                treeModifier.modify();
                translator.autoTranslate(parseTree);
                ParseTreeDrawable correctTree = toTestTreeBank.get(i);
                HashMap<ParseNode, ParseNodeDrawable> map = correctTree.mapTree(parseTree);
                for (ParseNode node : map.keySet()){
                    ParseNodeDrawable parseNode = (ParseNodeDrawable) node;
                    ParseNodeDrawable mappedNode = map.get(node);
                    if (parseNode.numberOfChildren() == 0 && mappedNode.numberOfChildren() == 0 && parseNode.getLayerData(ViewLayerType.TURKISH_WORD) != null && mappedNode.getLayerData(ViewLayerType.TURKISH_WORD) != null){
                        String correct = parseNode.getLayerData(ViewLayerType.TURKISH_WORD);
                        String translated = mappedNode.getLayerData(ViewLayerType.TURKISH_WORD);
                        if (correct.equalsIgnoreCase("*NONE*")){
                            if (translated.equalsIgnoreCase("*NONE*")){
                                tp++;
                            } else {
                                fn++;
                            }
                        } else {
                            if (translated.equalsIgnoreCase("*NONE*")){
                                fp++;
                            } else {
                                tn++;
                            }
                        }
                    }
                }
            }
            System.out.println("TP-Rate:" + (tp / (tp + fn + 0.0)) + " FP-Rate:" + (fp / (fp + tn + 0.0)));
        }
    }

    public static void translatePennTreeBankForBleu(String folder, String pattern){
        translateForBleu("../Penn-Treebank" + folder, pattern, folder);
    }

    public static void translateBaseLinePennTreeBankForBleu(String folder, String pattern){
        translateBaseLineForBleu("../Penn-Treebank" + folder, pattern, folder);
    }

    public static void translatePennTreeBankForStructureAgreement(String folder, String pattern){
        structureAgreement("../Penn-Treebank" + folder, pattern);
    }

    public static void translatePennTreeBankForNoneReplacement(String folder, String pattern){
        noneReplacement("../Penn-Treebank" + folder, pattern);
    }

    public static void translateAlcatelForBleu(){
        translateForBleu("/Users/olcay/alcatel/Program", "", "alcatel");
    }

    public static void calculateBleu(String folder, String pattern){
        BleuMeasure bleuMeasure;
        bleuMeasure = new BleuMeasure();
        try {
            System.out.println(bleuMeasure.execute("correct" + folder + "-" + pattern + ".txt", "translated" + folder + "-"  + pattern + ".txt", false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[]args){
        /*
        Penn-Treebank-15
        21.19+-0.92
        0.819+-0.005
        TPR:0.9150+-0.0051 FPR:0.0471+-0.0026
        */
        translateBaseLinePennTreeBankForBleu("", "");
        //translatePennTreeBankForStructureAgreement("", "");
        //translatePennTreeBankForNoneReplacement("", "");
    }
}
