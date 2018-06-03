package AnnotatedTree.AutoProcessor.AutoSemantic;

import AnnotatedTree.ParseTreeDrawable;

public abstract class TreeAutoSemantic {
    protected abstract boolean autoLabelSingleSemantics(ParseTreeDrawable parseTree);

    public void autoSemantic(ParseTreeDrawable parseTree){
        if (autoLabelSingleSemantics(parseTree)){
            parseTree.save();
        }
    }

}
