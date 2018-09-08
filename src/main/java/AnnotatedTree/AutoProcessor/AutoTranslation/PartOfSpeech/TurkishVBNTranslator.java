package AnnotatedTree.AutoProcessor.AutoTranslation.PartOfSpeech;

import Dictionary.TxtWord;
import AnnotatedTree.ParseNodeDrawable;

import java.util.List;

public class TurkishVBNTranslator extends TurkishVerbTranslator{

    public TurkishVBNTranslator(ParseNodeDrawable parseNode, List<String> parentList, List<String> englishWordList, String prefix, String lastWordForm, TxtWord lastWord) {
        super(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord);
    }

    public String translate(){
        if (parentList.size() > 1 && (parentList.get(1).equals("VBZ") || parentList.get(1).equals("VBP")) && (englishWordList.get(1).equalsIgnoreCase("is") || englishWordList.get(1).equalsIgnoreCase("are") || englishWordList.get(1).equalsIgnoreCase("'s") || englishWordList.get(1).equalsIgnoreCase("'re"))){
            return addSuffix("Hr", prefix, lastWord, lastWordForm);
            /*is/are done*/
        } else {
            if (parentList.size() > 2 && parentList.get(1).equals("RB") && (parentList.get(2).equals("VBZ") || parentList.get(2).equals("VBP")) && (englishWordList.get(2).equalsIgnoreCase("is") || englishWordList.get(2).equalsIgnoreCase("are") || englishWordList.get(2).equalsIgnoreCase("'s") || englishWordList.get(2).equalsIgnoreCase("'re"))){
                return addSuffix("mAz", prefix, lastWord, lastWordForm);
                /*is/are not done*/
            } else {
                if (parentList.size() > 2 && parentList.get(1).equals("VB") && parentList.get(2).equals("MD") && (englishWordList.get(2).equalsIgnoreCase("can") || englishWordList.get(2).equalsIgnoreCase("will"))) {
                    return addSuffix("Abilir", prefix, lastWord, lastWordForm);
                    /*can/will be done*/
                } else {
                    if (parentList.size() > 3 && parentList.get(1).equals("VB") && parentList.get(2).equals("MD") && (englishWordList.get(2).equalsIgnoreCase("can") || englishWordList.get(2).equalsIgnoreCase("will")) && parentList.get(3).equals("RB")) {
                        return addSuffix("AmAz", prefix, lastWord, lastWordForm);
                        /*can/will not be done*/
                    } else {
                        if (parentList.size() > 3 && parentList.get(1).equals("VB") && parentList.get(2).equals("RB") && parentList.get(3).equals("MD") && (englishWordList.get(3).equalsIgnoreCase("can") || englishWordList.get(3).equalsIgnoreCase("will"))) {
                            return addSuffix("AmAz", prefix, lastWord, lastWordForm);
                            /*can/will not be done*/
                        } else {
                            if (parentList.size() > 1 && parentList.get(1).equals("VBD") && (englishWordList.get(1).equalsIgnoreCase("was") || englishWordList.get(1).equalsIgnoreCase("were"))) {
                                return addSuffix("DH", prefix, lastWord, lastWordForm);
                                /*was/were done*/
                            } else {
                                if (parentList.size() > 2 && parentList.get(1).equals("RB") && parentList.get(2).equals("VBD") && (englishWordList.get(2).equalsIgnoreCase("was") || englishWordList.get(2).equalsIgnoreCase("were"))) {
                                    return addSuffix("mADH", prefix, lastWord, lastWordForm);
                                    /*was/were not done*/
                                } else {
                                    if (parentList.size() > 2 && parentList.get(1).equals("VBN") && parentList.get(2).equals("VBD")) {
                                        return addSuffix("Hyordu", prefix, lastWord, lastWordForm);
                                        /*had been done*/
                                    } else {
                                        if (parentList.size() > 3 && parentList.get(1).equals("VBN") && parentList.get(2).equals("RB") && parentList.get(3).equals("VBD")){
                                            return addSuffix("mHyordu", prefix, lastWord, lastWordForm);
                                            /*had not been done*/
                                        } else {
                                            if (parentList.size() > 2 && parentList.get(1).equals("VBN") && parentList.get(2).equals("VBZ")) {
                                                /*has been done*/
                                                if (parentList.get(3).equals("PRP")){
                                                    return addSuffix("DH", prefix, lastWord, lastWordForm, englishWordList.get(3).toLowerCase());
                                                } else {
                                                    return addSuffix("DH", prefix, lastWord, lastWordForm);
                                                }
                                            } else {
                                                if (parentList.size() > 3 && parentList.get(1).equals("VBN") && parentList.get(2).equals("RB") && parentList.get(3).equals("VBZ")) {
                                                    /*has not been done*/
                                                    if (parentList.get(4).equals("PRP")){
                                                        return addSuffix("mADH", prefix, lastWord, lastWordForm, englishWordList.get(4).toLowerCase());
                                                    } else {
                                                        return addSuffix("mADH", prefix, lastWord, lastWordForm);
                                                    }
                                                } else {
                                                    if (parentList.size() > 3 && parentList.get(1).equals("VB") && parentList.get(2).equals("MD") && (englishWordList.get(2).equalsIgnoreCase("could") || englishWordList.get(2).equalsIgnoreCase("would")) && parentList.get(3).equals("RB")) {
                                                        return addSuffix("mADH", prefix, lastWord, lastWordForm);
                                                        /*could/would not be done*/
                                                    } else {
                                                        if (parentList.size() > 2 && parentList.get(1).equals("VB") && parentList.get(2).equals("MD") && (englishWordList.get(2).equalsIgnoreCase("could") || englishWordList.get(2).equalsIgnoreCase("would"))) {
                                                            return addSuffix("AbilirDH", prefix, lastWord, lastWordForm);
                                                            /*could/would be done*/
                                                        } else {
                                                            if (parentList.size() > 3 && parentList.get(1).equals("VB") && parentList.get(2).equals("RB") && parentList.get(3).equals("MD") && (englishWordList.get(3).equalsIgnoreCase("could") || englishWordList.get(3).equalsIgnoreCase("would"))) {
                                                                return addSuffix("AmAzDH", prefix, lastWord, lastWordForm);
                                                                /*could/would not be done*/
                                                            } else {
                                                                if (parentList.size() > 1 && (parentList.get(1).equals("VBZ") || parentList.get(1).equals("VBP")) && (englishWordList.get(1).equalsIgnoreCase("has") || englishWordList.get(1).equalsIgnoreCase("have") || englishWordList.get(1).equalsIgnoreCase("'s") || englishWordList.get(1).equalsIgnoreCase("'ve"))) {
                                                                    /*has/have done*/
                                                                    if (parentList.get(2).equals("PRP")){
                                                                        return addSuffix("mHştH", prefix, lastWord, lastWordForm, englishWordList.get(2).toLowerCase());
                                                                    } else {
                                                                        return addSuffix("mHştH", prefix, lastWord, lastWordForm);
                                                                    }
                                                                } else {
                                                                    if (parentList.size() > 2 && parentList.get(1).equals("RB") && (parentList.get(2).equals("VBZ") || parentList.get(2).equals("VBP")) && (englishWordList.get(2).equalsIgnoreCase("has") || englishWordList.get(2).equalsIgnoreCase("have"))) {
                                                                        /*has/have not done*/
                                                                        if (parentList.get(3).equals("PRP")){
                                                                            return addSuffix("mADH", prefix, lastWord, lastWordForm, englishWordList.get(3).toLowerCase());
                                                                        } else {
                                                                            return addSuffix("mADH", prefix, lastWord, lastWordForm);
                                                                        }
                                                                    } else {
                                                                        if (parentList.size() > 1 && parentList.get(1).equals("VBD") && englishWordList.get(1).equals("had")) {
                                                                            /*had done*/
                                                                            if (parentList.get(2).equals("PRP")){
                                                                                return addSuffix("mHştH", prefix, lastWord, lastWordForm, englishWordList.get(2).toLowerCase());
                                                                            } else {
                                                                                return addSuffix("mHştH", prefix, lastWord, lastWordForm);
                                                                            }
                                                                        } else {
                                                                            if (parentList.size() > 1 && parentList.get(1).equals("RB") && parentList.get(2).equals("VBD") && englishWordList.get(2).equals("had")){
                                                                                /*had not done*/
                                                                                if (parentList.get(3).equals("PRP")){
                                                                                    return addSuffix("mAmHştH", prefix, lastWord, lastWordForm, englishWordList.get(3).toLowerCase());
                                                                                } else {
                                                                                    return addSuffix("mAmHştH", prefix, lastWord, lastWordForm);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
