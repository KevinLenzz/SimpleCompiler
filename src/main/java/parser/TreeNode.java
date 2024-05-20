package parser;

import lexer.tokens.Token;

import java.util.ArrayList;
import java.util.HashMap;

public class TreeNode {
    public String V;
    public int x;
    public int y;
    public Token token;
    public TreeNode parent=null;
    public HashMap<String,Object> attribute=new HashMap<String,Object>();
    public TreeNode(String v){
        V=v;
    }
    public ArrayList<TreeNode> children=new ArrayList<TreeNode>();
}
