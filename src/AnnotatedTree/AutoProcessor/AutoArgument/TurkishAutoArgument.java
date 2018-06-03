package AnnotatedTree.AutoProcessor.AutoArgument;

import AnnotatedSentence.ViewLayerType;
import ParseTree.ParseNode;
import AnnotatedTree.ParseNodeDrawable;
import PropBank.ArgumentType;

public class TurkishAutoArgument extends AutoArgument{

    public TurkishAutoArgument() {
        super(ViewLayerType.TURKISH_WORD);
    }

    private boolean checkAncestors(ParseNode parseNode, String name){
        while (parseNode != null){
            if (parseNode.getData().getName().equals(name)){
                return true;
            }
            parseNode = parseNode.getParent();
        }
        return false;
    }

    private boolean checkAncestorsUntil(ParseNode parseNode, String suffix){
        while (parseNode != null){
            if (parseNode.getData().getName().contains("-" + suffix)){
                return true;
            }
            parseNode = parseNode.getParent();
        }
        return false;
    }

    protected boolean autoDetectArgument(ParseNodeDrawable parseNode, ArgumentType argumentType) {
        ParseNode parent = parseNode.getParent();
        switch (argumentType){
            case ARG0:
                if (checkAncestorsUntil(parent, "SBJ")){
                    return true;
                }
                break;
            case ARG1:
                if (checkAncestorsUntil(parent, "OBJ")){
                    return true;
                }
                break;
            case ARGMADV:
                if (checkAncestorsUntil(parent, "ADV")){
                    return true;
                }
                break;
            case ARGMTMP:
                if (checkAncestorsUntil(parent, "TMP")){
                    return true;
                }
                break;
            case ARGMMNR:
                if (checkAncestorsUntil(parent, "MNR")){
                    return true;
                }
                break;
            case ARGMLOC:
                if (checkAncestorsUntil(parent, "LOC")){
                    return true;
                }
                break;
            case ARGMDIR:
                if (checkAncestorsUntil(parent, "DIR")){
                    return true;
                }
                break;
            case ARGMDIS:
                if (checkAncestors(parent, "CC")){
                    return true;
                }
                break;
            case ARGMEXT:
                if (checkAncestorsUntil(parent, "EXT")){
                    return true;
                }
                break;
        }
        return false;
    }
}
