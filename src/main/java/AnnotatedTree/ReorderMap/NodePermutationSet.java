package AnnotatedTree.ReorderMap;


import java.util.ArrayList;
import java.util.Collections;

public class NodePermutationSet {

    public ArrayList<NodePermutation> nodePermutations;

    public NodePermutationSet(){
        nodePermutations = new ArrayList<NodePermutation>();
    }

    public void insert(NodePermutation aPerm){
        if (nodePermutations.contains(aPerm)){
            NodePermutation oldPerm = nodePermutations.get(nodePermutations.indexOf(aPerm));
            oldPerm.count = oldPerm.count + 1;
        } else{
            nodePermutations.add(aPerm);
        }
    }

    public NodePermutation mlPermutation(){
        Collections.sort(nodePermutations, new NodePermutationComparator());
        return nodePermutations.get(0);
    }

    public int size(){
        return nodePermutations.size();
    }

    public int count(){
        int total = 0;
        for (NodePermutation nodePermutation : nodePermutations){
            total += nodePermutation.count;
        }
        return total;
    }

    public String toString(){
        String result = "";
        for (NodePermutation nodePermutation : nodePermutations){
            result = result + nodePermutation.toString();
        }
        return result;
    }

}
