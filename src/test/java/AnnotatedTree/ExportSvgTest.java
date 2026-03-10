package AnnotatedTree;

import AnnotatedSentence.ViewLayerType;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ExportSvgTest {

    public void exportNode(PrintWriter output, ParseNodeDrawable node, int maxDepth, int nodeWidth, int nodeHeight){
        String s;
        if (node.numberOfChildren() != 0){
            s = node.getData().getName();
        } else {
            s = node.getLayerData(ViewLayerType.TURKISH_WORD);
        }
        int addY;
        if (node.getDepth() == 0){
            addY = 15;
        } else {
            if (node.getDepth() == maxDepth){
                addY = -5;
            } else {
                addY = 5;
            }
        }
        int x = (node.getInOrderTraversalIndex() + 1) * nodeWidth - 20 / 2;
        int y = node.getDepth() * nodeHeight + addY;
        output.println("<text x=\"" + x + "\" y=\"" + y + "\">" + s + "</text>");
        for (int i = 0; i < node.numberOfChildren(); i++){
            ParseNodeDrawable child = (ParseNodeDrawable) node.getChild(i);
            output.println("<line x1=\"" + ((node.getInOrderTraversalIndex() + 1) * nodeWidth) + "\" y1=\"" + (node.getDepth() * nodeHeight + 20) + "\" x2=\"" + ((child.getInOrderTraversalIndex() + 1) * nodeWidth) + "\" y2=\"" + (child.getDepth() * nodeHeight - 20) + "\" style=\"stroke:black;stroke-width:2\"/>");
            exportNode(output, child, maxDepth, nodeWidth, nodeHeight);
        }
    }

    public void exportTreeBank(TreeBankDrawable treeBank) throws FileNotFoundException {
        PrintWriter output = new PrintWriter(new File("output.html"));
        int nodeWidth = 70;
        int nodeHeight = 80;
        for (int i = 0; i < treeBank.size(); i++){
            ParseTreeDrawable tree = treeBank.get(i);
            output.println("<svg width=\"" + ((tree.getMaxInOrderTraversalIndex() + 2) * nodeWidth) + "\" height=\"" + ((tree.maxDepth() + 1) * nodeHeight) + "\">");
            exportNode(output, (ParseNodeDrawable) tree.getRoot(), tree.maxDepth(), nodeWidth, nodeHeight);
            output.println("</svg>");
        }
        output.close();
    }

    @Test
    public void exportPennDataSet() throws FileNotFoundException {
        TreeBankDrawable treeBank = new TreeBankDrawable(new File("../../Penn-Treebank-20/Turkish2/"));
        exportTreeBank(treeBank);
    }
}
