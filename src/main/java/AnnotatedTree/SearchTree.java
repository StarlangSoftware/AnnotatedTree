package AnnotatedTree;

import ParseTree.ParseNode;
import Xml.XmlDocument;
import Xml.XmlElement;

import java.util.ArrayList;

public class SearchTree {

    ArrayList<ParseTreeSearchable> searchTrees;

    /**
     * Constructs a set of ParseTreeSearchables from the given file name. It reads the xml file and for each xml element
     * that contains ParseTreeSearchable, it calls its constructor.
     * @param fileName File that contains the search info.
     */
    public SearchTree(String fileName){
        XmlElement parseNode, rootNode, nextNode;
        XmlDocument doc = new XmlDocument(fileName);
        doc.parse();
        searchTrees = new ArrayList<>();
        rootNode = doc.getFirstChild();
        parseNode = rootNode.getFirstChild();
        while (parseNode != null){
            if (parseNode.getName().equalsIgnoreCase("tree")){
                nextNode = parseNode.getFirstChild();
                while (!nextNode.getName().equalsIgnoreCase("node") && !nextNode.getName().equalsIgnoreCase("leaf")){
                    nextNode = nextNode.getNextSibling();
                }
                searchTrees.add(new ParseTreeSearchable(nextNode));
            }
            parseNode = parseNode.getNextSibling();
        }
    }

    /**
     * Returns the ParseNodes in the given tree that satisfy all conditions given in the search trees.
     * @param tree Tree in which search operation will be done
     * @return ParseNodes in the given tree that satisfy all conditions given in the search trees.
     */
    public ArrayList<ParseNode> satisfy(ParseTreeDrawable tree){
        ArrayList<ParseNodeDrawable> tmpResult;
        for (ParseTreeSearchable treeSearchable:searchTrees){
            tmpResult = tree.satisfy(treeSearchable);
            if (!tmpResult.isEmpty()){
                return new ArrayList<>(tmpResult);
            }
        }
        return new ArrayList<>();
    }
}
