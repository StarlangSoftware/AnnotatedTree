package AnnotatedTree;

import ParseTree.ParseTree;

import java.util.Comparator;

public class ParseTreeComparator implements Comparator<ParseTree> {

    /**
     * Comparator method that compares two parse treess according to their file names.
     * @param o1 First parse tree to be compared.
     * @param o2 Second parse tree to be compared.
     * @return -1 if the first file name comes before the second file name lexicographically, 1 otherwise, and 0 if
     * both file names are equal.
     */
    public int compare(ParseTree o1, ParseTree o2) {
        if (o1 instanceof ParseTreeDrawable && o2 instanceof ParseTreeDrawable){
            return ((ParseTreeDrawable) o1).getFileDescription().getFileName().compareTo(((ParseTreeDrawable) o2).getFileDescription().getFileName());
        } else {
            return 0;
        }
    }
}
