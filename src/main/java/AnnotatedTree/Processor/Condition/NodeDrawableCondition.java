package AnnotatedTree.Processor.Condition;

import AnnotatedTree.ParseNodeDrawable;

public interface NodeDrawableCondition {
    boolean satisfies(ParseNodeDrawable parseNode);
}
