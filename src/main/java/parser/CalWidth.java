package parser;


import java.util.ArrayList;

public class CalWidth {
    public static void compute(TreeNode superNode,int depth){
        if(DrawTree.layer.containsKey(depth)){
            DrawTree.layer.get(depth).add(superNode);
        }else {
            ArrayList<TreeNode> tmp=new ArrayList<TreeNode>();
            tmp.add(superNode);
            DrawTree.layer.put(depth,tmp);
        }
//        if(DrawTree.width.size()>=depth+1){
//            if(DrawTree.width.get(depth)<superNode.children.size()){
//                DrawTree.width.set(depth,superNode.children.size());
//            }
//        }else{
//            DrawTree.width.add(superNode.children.size());
//        }
        for(TreeNode tn:superNode.children){
            compute(tn,depth+1);
        }
        if(depth>DrawTree.depth){
            DrawTree.depth=depth;
        }
    }
}
