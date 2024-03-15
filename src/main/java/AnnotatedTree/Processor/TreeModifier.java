package AnnotatedTree.Processor;

import AnnotatedSentence.LayerNotExistsException;
import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.Processor.NodeModification.NodeModifier;
import AnnotatedTree.WordNotExistsException;

public class TreeModifier {
    private final ParseTreeDrawable parseTree;
    private final NodeModifier nodeModifier;

    private void nodeModify(ParseNodeDrawable parseNode) throws LayerNotExistsException, WordNotExistsException{
        nodeModifier.modifier(parseNode);
        for (int i = 0; i < parseNode.numberOfChildren(); i++){
            nodeModify((ParseNodeDrawable)parseNode.getChild(i));
        }
    }

    public void modify(){
        try{
            nodeModify((ParseNodeDrawable)parseTree.getRoot());
        } catch (WordNotExistsException | LayerNotExistsException e) {
            System.out.println(e + " " + parseTree.getName());
        }
    }

    public TreeModifier(ParseTreeDrawable parseTree, NodeModifier nodeModifier){
        this.parseTree = parseTree;
        this.nodeModifier = nodeModifier;
    }
}
