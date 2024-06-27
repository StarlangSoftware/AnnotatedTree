package AnnotatedTree;

import ParseTree.ParseTree;
import Xml.XmlElement;

public class ParseTreeSearchable extends ParseTree {

    /**
     * Construct a ParseTreeSearchable from a xml element.
     * @param rootNode XmlElement that contains the root node information.
     */
    public ParseTreeSearchable(XmlElement rootNode){
        root = new ParseNodeSearchable(null, rootNode);
    }

}
