package AnnotatedTree.Processor;

import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.Processor.Condition.NodeDrawableCondition;

import java.util.ArrayList;

public class NodeDrawableCollector {
    private final NodeDrawableCondition condition;
    private final ParseNodeDrawable rootNode;

    /**
     * Constructor for the NodeDrawableCollector class. NodeDrawableCollector's main aim is to collect a set of
     * ParseNode's from a subtree rooted at rootNode, where the ParseNode's satisfy a given NodeCondition, which is
     * implemented by other interface class.
     * @param rootNode Root node of the subtree
     * @param condition The condition interface for which all nodes in the subtree rooted at rootNode will be checked
     */
    public NodeDrawableCollector(ParseNodeDrawable rootNode, NodeDrawableCondition condition){
        this.rootNode = rootNode;
        this.condition = condition;
    }

    /**
     * Private recursive method to check all descendants of the parseNode, if they ever satisfy the given node condition
     * @param parseNode Root node of the subtree
     * @param collected The {@link ArrayList} where the collected ParseNode's will be stored.
     */
    private void collectNodes(ParseNodeDrawable parseNode, ArrayList<ParseNodeDrawable> collected){
        if (condition == null || condition.satisfies(parseNode)){
            collected.add(parseNode);
        }
        for (int i = 0; i < parseNode.numberOfChildren(); i++){
            collectNodes((ParseNodeDrawable)parseNode.getChild(i), collected);
        }
    }

    /**
     * Collects and returns all ParseNodes satisfying the node condition.
     * @return All ParseNodes satisfying the node condition.
     */
    public ArrayList<ParseNodeDrawable> collect(){
        ArrayList<ParseNodeDrawable> result = new ArrayList<>();
        collectNodes(rootNode, result);
        return result;
    }
}
