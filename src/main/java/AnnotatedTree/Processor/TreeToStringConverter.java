package AnnotatedTree.Processor;

import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.Processor.LeafConverter.LeafToStringConverter;

public class TreeToStringConverter {
    private final LeafToStringConverter converter;
    private final ParseTreeDrawable parseTree;

    /**
     * Constructor of the TreeToStringConverter class. Sets the attributes.
     * @param parseTree Parse tree to be converted.
     * @param converter Node to string converter interface.
     */
    public TreeToStringConverter(ParseTreeDrawable parseTree, LeafToStringConverter converter){
        this.parseTree = parseTree;
        this.converter = converter;
    }

    /**
     * Converts recursively a parse node to a string. If it is a leaf node, calls the converter's leafConverter method,
     * otherwise concatenates the converted strings of its children.
     * @param parseNode Parse node to convert to string.
     * @return String form of the parse node and all of its descendants.
     */
    private String convertToString(ParseNodeDrawable parseNode){
        if (parseNode.isLeaf()){
            return converter.leafConverter(parseNode);
        } else {
            StringBuilder st = new StringBuilder();
            for (int i = 0; i < parseNode.numberOfChildren(); i++) {
                st.append(convertToString((ParseNodeDrawable) parseNode.getChild(i)));
            }
            return st.toString();
        }
    }

    /**
     * Calls the convertToString method with root of the tree to convert the parse tree to string.
     * @return String form of the parse tree.
     */
    public String convert(){
        return convertToString((ParseNodeDrawable)parseTree.getRoot());
    }

}
