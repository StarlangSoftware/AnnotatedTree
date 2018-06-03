package AnnotatedTree.ReorderMap;

public class TestReorderMap {

    public static void main(String[] args){
        ReorderMap reorderMap;
        reorderMap = new ReorderMap("../Penn-Treebank/English", "../Penn-Treebank/Turkish", ".", true);
        System.out.println(reorderMap.toString());
    }
}
