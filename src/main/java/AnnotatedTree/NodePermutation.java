package AnnotatedTree;

import ParseTree.ParseNode;

import java.util.ArrayList;


public class NodePermutation {

    public ArrayList<Integer> nodePermutation;
    public int count;

    /**
     * Constructor for NodePermutation class. NodePermutation stores a permutation of indexes of nodes starting from 0
     * to number of nodes - 1. Sets the permutation to the given permutation.
     * @param aPerm Permutation of nodes.
     */
    public NodePermutation(ArrayList<Integer> aPerm){
        nodePermutation = new ArrayList<>();
        nodePermutation.addAll(aPerm);
        count = 1;
    }

    /**
     * Constructor for NodePermutation class. NodePermutation stores a permutation of indexes of nodes starting from 0
     * to number of nodes - 1. Sets permutation to initial identity permutation, i.e., 0, 1, ..., n - 1.
     * @param n Number of nodes.
     */
    public NodePermutation(int n){
        this.nodePermutation = new ArrayList<>();
        for (int i = 0; i < n; i++)
            this.nodePermutation.add(i);
    }

    /**
     * Changes the order in the array list according to the node permutation. For example, if the node permutation is
     * 0, 2, 1; then the second node becomes the third node and the third node becomes the second node.
     * @param nodes List of nodes whose order will be changed according to the permutation.
     */
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
