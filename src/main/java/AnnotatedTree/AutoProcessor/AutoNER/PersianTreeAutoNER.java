package AnnotatedTree.AutoProcessor.AutoNER;

import AnnotatedSentence.ViewLayerType;
import AnnotatedTree.ParseTreeDrawable;

public class PersianTreeAutoNER extends TreeAutoNER {

    protected PersianTreeAutoNER() {
        super(ViewLayerType.PERSIAN_WORD);
    }

    protected void autoDetectPerson(ParseTreeDrawable parseTree) {

    }

    protected void autoDetectLocation(ParseTreeDrawable parseTree) {

    }

    protected void autoDetectOrganization(ParseTreeDrawable parseTree) {

    }

    protected void autoDetectMoney(ParseTreeDrawable parseTree) {

    }

    protected void autoDetectTime(ParseTreeDrawable parseTree) {

    }
}
