package parser;
/**
 * 画语法分析树
 */

import com.sun.source.tree.Tree;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
public class DrawTree {
//    public static ArrayList<Integer> width=new ArrayList<Integer>();
    public static HashMap<Integer,ArrayList<TreeNode>> layer=new HashMap<Integer,ArrayList<TreeNode>>();
    public static ArrayList<int[]> line=new ArrayList<int[]>();
    static int finalwidth;
    public static int depth=0;
    public static void create(TreeNode superNode,int depth){
        for(int i=0;i<superNode.children.size();i++){
            int[] tmp=new int[4];
            tmp[0]=superNode.x;
            tmp[1]=superNode.y;
            TreeNode tn=superNode.children.get(i);
            tn.x=(layer.get(depth+1).size()-1-layer.get(depth+1).indexOf(tn))*100;
            tn.y=(depth+1)*100;
            tmp[2]=tn.x;
            tmp[3]=tn.y;
            line.add(tmp);
            create(tn,depth+1);
        }
    }
//    private static void prepare(){
//        for(int i=1;i<width.size()-1;i++){
//            width.set(i,width.get(i-1)*width.get(i));
//        }
//        finalwidth=width.get(width.size()-2)*100;
//    }
    public static void draw(){
//        prepare();
        create(Check.S.children.get(1),1);
        LargeCanvas.paint();
    }
}
