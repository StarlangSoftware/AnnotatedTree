package AnnotatedTree;

import ParseTree.ParseTree;
import Xml.XmlElement;

public class ParseTreeSearchable extends ParseTree {

    public ParseTreeSearchable(XmlElement rootNode){
        root = new ParseNodeSearchable(null, rootNode);
    }

}
