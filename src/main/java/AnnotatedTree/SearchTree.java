package AnnotatedTree;

import ParseTree.ParseNode;
import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.util.ArrayList;

public class SearchTree {

    ArrayList<ParseTreeSearchable> searchTrees;

    public SearchTree(String fileName){
        Node parseNode, rootNode, nextNode;
        DOMParser parser = new DOMParser();
        Document doc;
        try {
            parser.parse(fileName);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        searchTrees = new ArrayList<ParseTreeSearchable>();
        doc = parser.getDocument();
        rootNode = doc.getFirstChild();
        parseNode = rootNode.getFirstChild();
        while (parseNode != null){
            if (parseNode.getNodeName().equalsIgnoreCase("tree")){
                nextNode = parseNode.getFirstChild();
                while (!nextNode.getNodeName().equalsIgnoreCase("node") && !nextNode.getNodeName().equalsIgnoreCase("leaf")){
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
