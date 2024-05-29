package parser;
/**
 * LL1栈语法分析（循环调用GetToken）
 * 语法分析器的调用接口
 */

import lexer.GetToken;
import lexer.Type;
import lexer.tokens.Token;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

public class Check {
    static String RED = "\u001B[31m";
    static String GREEN = "\u001B[32m";
    static String YELLOW = "\u001B[33m";
    static String BLUE = "\u001B[34m";
    static String CYAN = "\u001B[36m";
    public static TreeNode S=new TreeNode(Store.S);
    static Stack<String> CheckStack = new Stack<String>();
    static Stack<String> AnalizingPStack=new Stack<String>();
    static Stack<Integer> AnalizingIdxStack=new Stack<Integer>();
    static LinkedList<TreeNode> TreeLayerList=new LinkedList<TreeNode>();
    static Token p;
    static boolean errorStart=false;
    private static void error(Token errorToken,String correctWord,String analyzingVN) throws IOException {
        //System.out.println(AnalizingPStack);
        //System.out.println(AnalizingIdxStack);
        System.out.println(RED+"ParserError in "+errorToken.line+":"+errorToken.col+" Deriving:"+analyzingVN+",Probably need:"+correctWord+" But provided:"+errorToken.str+" Its type is "+errorToken.type);
    }
    private static void errorDefined(Token errorToken,String dfedStr){
        System.out.println(RED+"ParserError in "+errorToken.line+":"+errorToken.col+" "+dfedStr);
    }
    private static void reuseSynthetic() throws IOException {//用于终结符的某些复用的语句
        CheckStack.pop();
        TreeNode now=TreeLayerList.getFirst();
        if(now.V.equals("id")||now.V.equals("INTC")||now.V.equals("DECI")||now.V.equals("STR")||now.V.equals("\'break\'")||now.V.equals("\'continue\'")||now.V.equals("\'return\'")||now.V.equals("\'int\'")||now.V.equals("\'float\'")||now.V.equals("\'string\'")||now.V.equals("\'void\'")) {
            now.token = p;
            now.token.str=now.token.str.replace("\'","");
        }
        while(CheckStack.size()<=AnalizingIdxStack.peek()){
            AnalizingPStack.pop();
            AnalizingIdxStack.pop();
        }
        //System.out.println(AnalizingIdxStack);
        TreeLayerList.removeFirst();
        p=GetToken.getToken();
        //System.out.println(YELLOW+"now check \t\t\t\t"+p.str);
    }
    private static void word_synchronize() throws IOException {
        while(CheckStack.size()>AnalizingIdxStack.peek()){
            CheckStack.pop();
        }
        AnalizingIdxStack.pop();
        AnalizingPStack.pop();
        p=GetToken.getToken();
    }
    public static void check() throws IOException {
        try{
            ReadP.read(ClassLoader.getSystemResource("Grammar.txt").getPath());
        }catch (Exception e){
            try{
                ReadP.read("Grammar.txt");
            }catch (Exception exp){
                System.out.println(RED+"请把Grammar.txt放在和Compiler.jar同一个目录下");
            }
        }
        CreateFIRST.create();
        CreateFOLLOW.create();
        CreateM.create();
        try {
            CreateErrorMap.create(ClassLoader.getSystemResource("ErrorMapping.txt").getPath());
        }catch (Exception e){
            try{
                ReadP.read("ErrorMapping.txt");
            }catch (Exception exp){
                System.out.println(RED+"请把ErrorMapping.txt放在和Compiler.jar同一个目录下");
            }
        }
        PanicMode.createSet();
        /*System.out.println("P:"+Store.P);
        System.out.println("VN:"+Store.VN);*/
        /*System.out.println("VT:"+Store.VT);
        System.out.println("FIRST:"+Store.FIRST);
        System.out.println("S:"+Store.S);
        System.out.println("FOLLOW:"+Store.FOLLOW);
        System.out.println("M:"+Store.M);
        System.out.println("ErrorMap:"+Store.ErrorMap);*/
        CheckStack.add(Store.S);
        String X=CheckStack.peek();
        p=GetToken.getToken();
        TreeNode now=null;
        TreeLayerList.addFirst(S);
        S.V=Store.S;
        //System.out.println(YELLOW+"now check "+p.str);
        while(!CheckStack.isEmpty()){
            if(p.type==Type.ERR){
                System.out.println(RED+"LexerError:"+p.str);
                p=GetToken.getToken();
                continue;
            }
            if(Store.VT.contains(X)){
                if(X.equals("id")&&p.type== Type.ID){
                    reuseSynthetic();
                }else if(X.equals("INTC")&&p.type== Type.INT){
                    reuseSynthetic();
                }else if(X.equals("STR")&&p.type== Type.STR){
                    reuseSynthetic();
                }else if(X.equals("DECI")&&p.type== Type.FLT){
                    reuseSynthetic();
                }else if(p.str.equals(X)){
                    reuseSynthetic();
                }else if(X.equals("#")&&p.type== Type.END){
                    CheckStack.pop();
                    TreeLayerList.removeFirst();
                    System.out.println(CYAN+"Parser End".formatted());
                    break;
                }else{
                    if(errorStart){
                        if(Store.synchronizeSet.get(AnalizingPStack.peek().split("→")[0]).contains(p.str)){
                            word_synchronize();
                            errorStart=false;
                        }
                        p=GetToken.getToken();
                    }else {
                        if (Store.ErrorMap.toString().contains(AnalizingPStack.peek().split("→")[0] + " " + X)) {
                            errorDefined(p, Store.ErrorMap.get(AnalizingPStack.peek().split("→")[0] + " " + X));
                        } else error(p, X, AnalizingPStack.peek());
                        p = GetToken.getToken();
                        errorStart = true;
                    }
                }
            }else if(Store.VN.contains(X)){
                String tmpStr;
                //写死的对接词法分析器的部分
                if(p.type== Type.ID){
                    tmpStr="id";
                }else if(p.type== Type.INT){
                    tmpStr="INTC";
                }else if(p.type== Type.FLT){
                    tmpStr="DECI";
                }else if(p.type== Type.STR){
                    tmpStr="STR";
                }else if (p.type==Type.END){
                    tmpStr="#";
                    System.out.println(CYAN+"Parser End".formatted());
                    break;
                }else{
                    tmpStr=p.str;
                }
                //写死的对接词法分析器的部分
                if (Store.M.get(X).get(tmpStr).toString().contains("Error")) {
                    if(Store.ErrorMap.toString().contains(AnalizingPStack.peek().split("→")[0]+" "+X)){
                        errorDefined(p,Store.ErrorMap.get(AnalizingPStack.peek().split("→")[0]+" "+X));
                    }else {
                        ArrayList<String> tmp = new ArrayList<String>();
                        tmp.addAll(Store.FIRST.get(X));
                        if (tmp.toString().contains("ε")) {
                            tmp.remove("ε");
                            if (!(AnalizingIdxStack.peek() < CheckStack.size())) {
                                for (int i = 0; i < CheckStack.size() - AnalizingIdxStack.peek(); i++) {
                                    tmp.remove("ε");
                                    tmp.addAll(Store.FIRST.get(Store.P.get(AnalizingPStack.peek()).get(Store.P.get(AnalizingPStack.peek()).indexOf(X) + 1 + i)));
                                    if (!tmp.toString().contains("ε")) break;
                                }
                            } else {
                                tmp.addAll(Store.FOLLOW.get(AnalizingPStack.peek().substring(0, AnalizingPStack.peek().indexOf('→'))));
                            }
                        }
                        error(p, tmp.toString(), AnalizingPStack.peek());
                    }
                    p=GetToken.getToken();
                }else {
                    String targetP = Store.M.get(X).get(tmpStr).get(0);
                    //System.out.println(BLUE+"Derive:\t\t\t\t\t"+X + "→" + targetP);
                    CheckStack.pop();
                    if(!targetP.equals("ε")) {
                        AnalizingPStack.push(X + "→" + targetP);
                        AnalizingIdxStack.push(CheckStack.size());
                    }
                    now=TreeLayerList.getFirst();
                    TreeLayerList.removeFirst();
                    String[] rightList = targetP.split("\\s+");
                    for (int i = rightList.length - 1; i >= 0; i--) {
                        TreeNode tmp=new TreeNode(rightList[i]);
                        TreeLayerList.addFirst(tmp);
                        CheckStack.push(rightList[i]);
                        tmp.parent=now;
                        now.children.add(tmp);
                    }
                }
            }
            while(CheckStack.peek().equals("ε")){
                TreeLayerList.removeFirst();
                CheckStack.pop();
            }
            X=CheckStack.peek();
            //System.out.println(GREEN+"CheckStackState:\t\t"+CheckStack);
        }
        int depth=0;
        CalWidth.compute(S,0);
        DrawTree.draw();
        return;
    }
}
