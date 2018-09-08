package AnnotatedTree;

import ParseTree.ParseTree;

import java.util.Comparator;

public class ParseTreeComparator implements Comparator<ParseTree> {

    public int compare(ParseTree o1, ParseTree o2) {
        if (o1 instanceof ParseTreeDrawable && o2 instanceof ParseTreeDrawable){
            return ((ParseTreeDrawable) o1).getFileDescription().getFileName().compareTo(((ParseTreeDrawable) o2).getFileDescription().getFileName());
        } else {
            return 0;
        }
    }
}
