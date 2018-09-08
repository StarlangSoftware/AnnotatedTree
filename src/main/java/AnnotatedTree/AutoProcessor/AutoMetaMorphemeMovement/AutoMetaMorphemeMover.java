package AnnotatedTree.AutoProcessor.AutoMetaMorphemeMovement;

import AnnotatedTree.ParseTreeDrawable;

public abstract class AutoMetaMorphemeMover {
    protected abstract void metaMorphemeMoveWithRules(ParseTreeDrawable parseTree);

    public void autoPosMove(ParseTreeDrawable parseTree){
        metaMorphemeMoveWithRules(parseTree);
        parseTree.save();
    }

}
