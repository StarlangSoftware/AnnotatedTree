package AnnotatedTree;

import ParseTree.ParseNode;

import java.util.ArrayList;


public class NodePermutation {

    public ArrayList<Integer> nodePermutation;
    public int count;

    public NodePermutation(ArrayList<Integer> aPerm){
        nodePermutation = new ArrayList<>();
        nodePermutation.addAll(aPerm);
        count = 1;
    }

    public NodePermutation(int n){
        this.nodePermutation = new ArrayList<>();
        for (int i = 0; i < n; i++)
            this.nodePermutation.add(i);
    }

    public void apply(ArrayList<ParseNode> nodes){
        ArrayList<ParseNode> tmp = new ArrayList<>(nodes);
        for (int i = 0; i < nodePermutation.size(); i++)
            nodes.set(i, tmp.get(i));
    }

    public NodePermutation(ParseNode fromNode, ParseNode toNode){
        count = 1;
        nodePermutation = new ArrayList<>(fromNode.numberOfChildren());
        ArrayList<Boolean> isUsed = new ArrayList<>(toNode.numberOfChildren());
        for (int i = 0; i < toNode.numberOfChildren(); i++)
            isUsed.add(false);
        for (int i = 0; i < toNode.numberOfChildren(); i++){
            for (int j = 0; j < fromNode.numberOfChildren(); j++){
                if (!isUsed.get(j) && ((ParseNodeDrawable)fromNode.getChild(j)).isPermutation((ParseNodeDrawable)toNode.getChild(i))){
                    isUsed.set(j, true);
                    nodePermutation.add(j);
                    break;
                }
            }
        }
    }

    @Override public boolean equals (Object anObject){
        if (this == anObject)
            return true;
        if (!(anObject instanceof NodePermutation))
            return false;
        return this.nodePermutation.equals(((NodePermutation)anObject).nodePermutation);
    }

    public String toString(){
        StringBuilder result = new StringBuilder();
        for (Integer i : nodePermutation){
            result.append(" ").append(i);
        }
        return result + "-->" + count + "\n";
    }
}
