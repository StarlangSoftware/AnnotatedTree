package AnnotatedTree.Processor.LayerExist;

import AnnotatedTree.ParseNodeDrawable;

import java.util.ArrayList;

public interface LeafListCondition {
    boolean satisfies(ArrayList<ParseNodeDrawable> leafList);
}
