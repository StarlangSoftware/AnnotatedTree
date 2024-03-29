package AnnotatedTree.Processor;

import AnnotatedTree.ParseNodeDrawable;
import AnnotatedTree.Processor.Condition.NodeDrawableCondition;

import java.util.ArrayList;

public class NodeDrawableCollector {
    private final NodeDrawableCondition condition;
    private final ParseNodeDrawable rootNode;

    public NodeDrawableCollector(ParseNodeDrawable rootNode, NodeDrawableCondition condition){
        this.rootNode = rootNode;
        this.condition = condition;
    }

    private void collectNodes(ParseNodeDrawable parseNode, ArrayList<ParseNodeDrawable> collected){
        if (condition == null || condition.satisfies(parseNode)){
            collected.add(parseNode);
        }
        for (int i = 0; i < parseNode.numberOfChildren(); i++){
            collectNodes((ParseNodeDrawable)parseNode.getChild(i), collected);
        }
    }

    public ArrayList<ParseNodeDrawable> collect(){
        ArrayList<ParseNodeDrawable> result = new ArrayList<>();
        collectNodes(rootNode, result);
        return result;
    }
}
