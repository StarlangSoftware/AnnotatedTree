package AnnotatedTree.Processor.Condition;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseNodeDrawable;

public class IsNoneReplaceable extends IsLeafNode{

    public boolean satisfies(ParseNodeDrawable parseNode) {
        if (super.satisfies(parseNode)){
            String data = parseNode.getLayerData(ViewLayerType.ENGLISH_WORD);
            String parentData = parseNode.getParent().getData().getName();
            if (parentData.equals("DT")){
                return data.equalsIgnoreCase("the");
            } else {
                if (parentData.equals("IN")){
                    return data.equalsIgnoreCase("in") || data.equalsIgnoreCase("than") || data.equalsIgnoreCase("from") || data.equalsIgnoreCase("on") || data.equalsIgnoreCase("with") || data.equalsIgnoreCase("of") || data.equalsIgnoreCase("at") || data.equalsIgnoreCase("if") || data.equalsIgnoreCase("by");
                } else {
                    if (parentData.equals("TO")){
                        return data.equalsIgnoreCase("to");
                    } else {
                        if (parentData.equals("VBZ")){
                            return data.equalsIgnoreCase("has") || data.equalsIgnoreCase("does") || data.equalsIgnoreCase("is") || data.equalsIgnoreCase("'s");
                        } else {
                            if (parentData.equals("MD")){
                                return data.equalsIgnoreCase("will") || data.equalsIgnoreCase("'d") || data.equalsIgnoreCase("'ll") || data.equalsIgnoreCase("ca") || data.equalsIgnoreCase("can") || data.equalsIgnoreCase("could") || data.equalsIgnoreCase("would") || data.equalsIgnoreCase("should") || data.equalsIgnoreCase("wo") || data.equalsIgnoreCase("may") || data.equalsIgnoreCase("might");
                            } else {
                                if (parentData.equals("VBP")){
                                    return data.equalsIgnoreCase("'re") || data.equalsIgnoreCase("is") || data.equalsIgnoreCase("are") || data.equalsIgnoreCase("am") || data.equalsIgnoreCase("'m") || data.equalsIgnoreCase("do") || data.equalsIgnoreCase("have") || data.equalsIgnoreCase("has") || data.equalsIgnoreCase("'ve");
                                } else {
                                    if (parentData.equals("VBD")){
                                        return data.equalsIgnoreCase("had") || data.equalsIgnoreCase("did") || data.equalsIgnoreCase("were") || data.equalsIgnoreCase("was");
                                    } else {
                                        if (parentData.equals("VBN")){
                                            return data.equalsIgnoreCase("been");
                                        } else {
                                            if (parentData.equals("VB")){
                                                return data.equalsIgnoreCase("have") || data.equalsIgnoreCase("be");
                                            } else {
                                                if (parentData.equals("RB")){
                                                    return data.equalsIgnoreCase("n't") || data.equalsIgnoreCase("not");
                                                } else {
                                                    if (parentData.equals("POS")){
                                                        return data.equalsIgnoreCase("'s") || data.equalsIgnoreCase("'");
                                                    } else {
                                                        if (parentData.equals("WP")){
                                                            return data.equalsIgnoreCase("who") || data.equalsIgnoreCase("where") || data.equalsIgnoreCase("which") || data.equalsIgnoreCase("what") || data.equalsIgnoreCase("why");
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
        return false;
    }

}
