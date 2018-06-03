package AnnotatedTree.AutoProcessor.AutoTranslation.PartOfSpeech;

import Dictionary.TxtWord;
import MorphologicalAnalysis.Transition;
import AnnotatedTree.ParseNodeDrawable;

import java.util.List;

public class TurkishVBTranslator extends TurkishVerbTranslator{

    public TurkishVBTranslator(ParseNodeDrawable parseNode, List<String> parentList, List<String> englishWordList, String prefix, String lastWordForm, TxtWord lastWord) {
        super(parseNode, parentList, englishWordList, prefix, lastWordForm, lastWord);
    }

    public String translate(){
        Transition transition;
        if (parentList.size() > 1 && parentList.get(1).equals("MD")){
            if (englishWordList.get(1).equals("will")){
                if (parentList.size() > 3 && parentList.get(2).equals("RB") && parentList.get(3).equals("PRP")){
                    transition = new Transition("mAyAcAk" + personalSuffix1(englishWordList.get(3).toLowerCase()));
                } else {
                    if (parentList.size() > 2 && parentList.get(2).equals("PRP")){
                        transition = new Transition("yAcAk" + personalSuffix1(englishWordList.get(2).toLowerCase()));
                    } else {
                        if (parentList.size() > 2 && parentList.get(2).equals("RB")){
                            transition = new Transition("mAyAcAk");
                        } else {
                            transition = new Transition("yAcAk");
                        }
                    }
                }
                return prefix + transition.makeTransition(lastWord, lastWordForm);
            } else {
                if (englishWordList.get(1).equalsIgnoreCase("can") || englishWordList.get(1).equalsIgnoreCase("may") || englishWordList.get(1).equalsIgnoreCase("might") || englishWordList.get(1).equalsIgnoreCase("could")){
                    if (parentList.size() > 3 && parentList.get(2).equals("RB") && parentList.get(3).equals("PRP")){
                        transition = new Transition("mAyAbilir" + personalSuffix1(englishWordList.get(3).toLowerCase()));
                    } else {
                        if (parentList.size() > 2 && parentList.get(2).equals("PRP")){
                            transition = new Transition("yAbilir" + personalSuffix1(englishWordList.get(2).toLowerCase()));
                        } else {
                            if (parentList.size() > 2 && parentList.get(2).equals("RB")){
                                transition = new Transition("mAz");
                            } else {
                                transition = new Transition("yAbilir");
                            }
                        }
                    }
                    return prefix + transition.makeTransition(lastWord, lastWordForm);
                } else {
                    if (englishWordList.get(1).equals("would") || englishWordList.get(1).equals("wo")){
                        if (parentList.size() > 3 && parentList.get(2).equals("RB") && parentList.get(3).equals("PRP")){
                            transition = new Transition("mHyor" + personalSuffix1(englishWordList.get(3).toLowerCase()));
                        } else {
                            if (parentList.size() > 2 && parentList.get(2).equals("PRP")){
                                transition = new Transition("Hyor" + personalSuffix1(englishWordList.get(2).toLowerCase()));
                            } else {
                                if (parentList.size() > 2 && parentList.get(2).equals("RB")){
                                    transition = new Transition("mHyor");
                                } else {
                                    transition = new Transition("Hyor");
                                }
                            }
                        }
                        return prefix + transition.makeTransition(lastWord, lastWordForm);
                    }
                }
            }
        } else {
            if (parentList.size() > 2 && parentList.get(1).equals("TO") && englishWordList.get(1).equalsIgnoreCase("to") && parentList.get(2).equals("VBD") && englishWordList.get(2).equalsIgnoreCase("had")){
                transition = new Transition("mAlHyDH");
                if (parentList.size() > 3 && parentList.get(3).equals("PRP")){
                    transition = new Transition("mAlHyDH" + personalSuffix2(englishWordList.get(3).toLowerCase()));
                }
                return prefix + transition.makeTransition(lastWord, lastWordForm);
            } else {
                if (parentList.size() > 2 && parentList.get(1).equals("TO") && englishWordList.get(1).equalsIgnoreCase("to") && parentList.get(2).equals("VB") && englishWordList.get(2).equalsIgnoreCase("have")){
                    transition = new Transition("mAlH");
                    if (parentList.size() > 3 && parentList.get(3).equals("PRP")){
                        transition = new Transition("mAlH" + personalSuffix3(englishWordList.get(3).toLowerCase()));
                    }
                    return prefix + transition.makeTransition(lastWord, lastWordForm);
                } else {
                    if (parentList.size() > 2 && parentList.get(1).equals("RB") && parentList.get(2).equals("VBD") && englishWordList.get(2).equalsIgnoreCase("did")){
                        transition = new Transition("mADH");
                        if (parentList.size() > 3 && parentList.get(3).equals("PRP")){
                            transition = new Transition("mADH" + personalSuffix2(englishWordList.get(3).toLowerCase()));
                        }
                        return prefix + transition.makeTransition(lastWord, lastWordForm);
                    } else {
                        if (parentList.size() > 2 && parentList.get(1).equals("RB") && parentList.get(2).equals("VBP") && englishWordList.get(2).equalsIgnoreCase("do")){
                            transition = new Transition("mAz");
                            if (parentList.size() > 3 && parentList.get(3).equals("PRP")){
                                transition = new Transition("mA" + personalSuffix4(englishWordList.get(3).toLowerCase()));
                            }
                            return prefix + transition.makeTransition(lastWord, lastWordForm);
                        } else {
                            if (parentList.size() > 2 && parentList.get(1).equals("RB") && parentList.get(2).equals("VBZ") && englishWordList.get(2).equalsIgnoreCase("does")){
                                transition = new Transition("mAz");
                                if (parentList.size() > 3 && parentList.get(3).equals("PRP")){
                                    transition = new Transition("mA" + personalSuffix4(englishWordList.get(3).toLowerCase()));
                                }
                                return prefix + transition.makeTransition(lastWord, lastWordForm);
                            }
                        }
                    }
                }
            }
        }
        return addSuffix("mAk", prefix, lastWord, lastWordForm);
    }
}
