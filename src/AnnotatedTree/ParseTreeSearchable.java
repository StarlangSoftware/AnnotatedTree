package AnnotatedTree;

import ParseTree.ParseTree;
import org.w3c.dom.Node;

public class ParseTreeSearchable extends ParseTree {

    public ParseTreeSearchable(Node rootNode){
        root = new ParseNodeSearchable(null, rootNode);
    }

}
