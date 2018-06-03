package AnnotatedTree.Processor.NodeModification;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.WordNotExistsException;

public interface NodeModifier {
    void modifier(ParseNodeDrawable parseNode) throws LayerNotExistsException, WordNotExistsException;
}
