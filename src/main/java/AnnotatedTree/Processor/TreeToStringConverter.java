package AnnotatedTree.Processor;

import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.Processor.LeafConverter.LeafToStringConverter;

public class TreeToStringConverter {
    private final LeafToStringConverter converter;
    private final ParseTreeDrawable parseTree;

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

    public String convert(){
        return convertToString((ParseNodeDrawable)parseTree.getRoot());
    }

    public TreeToStringConverter(ParseTreeDrawable parseTree, LeafToStringConverter converter){
        this.parseTree = parseTree;
        this.converter = converter;
    }

}
