package AnnotatedTree;

import ParseTree.ParseNode;
import Xml.XmlDocument;
import Xml.XmlElement;

import java.util.ArrayList;

public class SearchTree {

    ArrayList<ParseTreeSearchable> searchTrees;

    public SearchTree(String fileName){
        XmlElement parseNode, rootNode, nextNode;
        XmlDocument doc = new XmlDocument(fileName);
        doc.parse();
        searchTrees = new ArrayList<ParseTreeSearchable>();
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

    public ArrayList<ParseNode> satisfy(ParseTreeDrawable tree){
        ArrayList<ParseNodeDrawable> tmpResult;
        for (ParseTreeSearchable treeSearchable:searchTrees){
            tmpResult = tree.satisfy(treeSearchable);
            if (tmpResult.size() > 0){
                ArrayList<ParseNode> result = new ArrayList<ParseNode>();
                for (ParseNodeDrawable parseNodeDrawable: tmpResult){
                    result.add(parseNodeDrawable);
                }
                return result;
            }
        }
        return new ArrayList<ParseNode>();
    }
}
