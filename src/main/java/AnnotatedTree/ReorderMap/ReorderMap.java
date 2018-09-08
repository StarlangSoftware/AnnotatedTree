package AnnotatedTree.ReorderMap;

import ContextFreeGrammar.Rule;
import AnnotatedTree.ParseTreeDrawable;
import AnnotatedTree.TreeBankDrawable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ReorderMap {

    private HashMap <Rule,NodePermutationSet> reorders;

    public ReorderMap(){
        reorders = new HashMap<Rule, NodePermutationSet>();
    }

    public void constructReorderMap(TreeBankDrawable fromTreeBank, TreeBankDrawable toTreeBank){
        reorders = new HashMap<Rule, NodePermutationSet>();
        for (int i = 0; i < fromTreeBank.size(); i++){
            ParseTreeDrawable fromTree = fromTreeBank.get(i);
            ParseTreeDrawable toTree = toTreeBank.get(i);
            if (fromTree.isPermutation(toTree))
                fromTree.addReorder(toTree, this);
            else
                System.out.println("Permutation Error with file name: " + fromTree.getName());
        }
        for (Map.Entry<Rule,NodePermutationSet> entry:reorders.entrySet()){
            NodePermutationSet ps = entry.getValue();
            int total = 0;
            for (NodePermutation p:ps.nodePermutations)
                total += p.count;
            for (NodePermutation p:ps.nodePermutations)
                p.logProb = Math.log(((double)(p.count))/((double)total));
        }
    }

    public ReorderMap(String fromDirectory, String toDirectory, String pattern, boolean includePunctuation){
        TreeBankDrawable fromTreeBank = new TreeBankDrawable(new File(fromDirectory), pattern);
        TreeBankDrawable toTreeBank = new TreeBankDrawable(new File(toDirectory), pattern);
        if (!includePunctuation){
            fromTreeBank.stripPunctuation();
            toTreeBank.stripPunctuation();
        }
        constructReorderMap(fromTreeBank, toTreeBank);
    }

    public void addReorder(Rule rule, NodePermutation nodePermutation){
        if (!reorders.containsKey(rule))
            reorders.put(rule, new NodePermutationSet());
        reorders.get(rule).insert(nodePermutation);
    }

    public NodePermutationSet permutationSet(Rule rule){
        if (reorders.containsKey(rule))
            return reorders.get(rule);
        return null;
    }

    public String toString(){
        String result = "";
        for (Map.Entry<Rule,NodePermutationSet> entry:reorders.entrySet()){
            NodePermutationSet ps = entry.getValue();
            result = result + entry.getKey().toString() + "\n";
            result = result + ps.toString();
        }
        return result;
    }
}


