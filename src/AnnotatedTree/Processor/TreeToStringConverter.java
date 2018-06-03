package AnnotatedTree.Processor;

import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.Processor.LeafConverter.LeafToStringConverter;

public class TreeToStringConverter {
    private LeafToStringConverter converter;
    private ParseTreeDrawable parseTree;

    private String convertToString(ParseNodeDrawable parseNode){
        if (parseNode.isLeaf()){
            return converter.leafConverter(parseNode);
        } else {
            String st = "";
            for (int i = 0; i < parseNode.numberOfChildren(); i++) {
                st = st + convertToString((ParseNodeDrawable)parseNode.getChild(i));
            }
            return st;
        }
    }

    public String convert(){
        return convertToString((ParseNodeDrawable)parseTree.getRoot());
    }

    public TreeToStringConverter(ParseTreeDrawable parseTree, LeafToStringConverter converter){
        this.parseTree = parseTree;
        this.converter = converter;
    }

}
