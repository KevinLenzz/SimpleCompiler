package semantics;

import com.sun.source.tree.Tree;
import lexer.tokens.Token;
import parser.Store;
import parser.TreeNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.regex.Pattern;


public class CalAttr {
    public static SymbolTable SymbolTablePointer;
    static String RED = "\u001B[31m";
    static ArrayList<Quaternions> QuaternionsPointer;
    static ArrayList<Quaternions> MainQuaternionsPointer;
    static int c=0;
    static int count=0;
    private static String newtemp(){
        c++;
        return "T"+c;
    }
    public static void LRD(TreeNode parent) throws IOException {
        if(parent.children!=null) {
            ArrayList<TreeNode> tmp=new ArrayList<TreeNode>();
            for(int i=parent.children.size()-1;i>=0;i--){
                tmp.add(parent.children.get(i));
            }
            parent.children=tmp;
            for(TreeNode tn:parent.children) {
                if(Store.VN.contains(tn.V)){
                    LRD(tn);
                }
            }
        }
        count++;
        switch(parent.V){
            case "VarList"->{
                if(SymbolTablePointer.funcNames.contains(parent.parent.parent.parent.children.get(1).token.str)){
                    error(parent.parent.parent.parent.children.get(1).token,"Function Redefined");
                }
                if(parent.children.get(0).V.equals("ε")){
                    ArrayList<Quaternions> nowQuaternions = new ArrayList<Quaternions>();
                    var newTable = new SymbolTable();
                    newTable.parent = SymbolTablePointer;
                    newTable.Quaternions = nowQuaternions;
                    SymbolTablePointer.funcList.add(new Function(parent.parent.parent.parent.children.get(1).token.str,
                            parent.parent.parent.parent.children.get(0).children.get(0).token.str,
                            false,parent.parent.parent.parent.children.get(0).children.get(0).token.line,null, newTable,null));
                    SymbolTablePointer.funcNames.add(parent.parent.parent.parent.children.get(1).token.str);
                    QuaternionsPointer = nowQuaternions;
                    SymbolTablePointer = newTable;
                    if(parent.parent.parent.parent.children.get(1).token.str.equals("main")){
                        MainQuaternionsPointer=QuaternionsPointer;
                    }
                }else {
                    parent=parent.children.get(2);
                    while(!parent.children.get(0).V.equals("ε")){
                        parent=parent.children.get(3);
                    }
                    TreeNode tmpP = parent.parent;
                    ArrayList<String> parameter2add = new ArrayList<String>();
                    ArrayList<String> parameterType2add = new ArrayList<String>();
                    var newTable = new SymbolTable();
                    while (!tmpP.V.equals("VarList")) {
                        parameter2add.add(tmpP.children.get(2).token.str);
                        parameterType2add.add(tmpP.children.get(1).children.get(0).token.str);
                        newTable.records.add(new Symbol(tmpP.children.get(2).token.str,tmpP.children.get(1).children.get(0).token.str,false,"",tmpP.children.get(1).children.get(0).token.line));
                        if(newTable.recordsNames.contains(tmpP.children.get(2).token.str)){
                            error(tmpP.children.get(2).token,"Parameter Conflict");
                        }else newTable.recordsNames.add(tmpP.children.get(2).token.str);
                        tmpP = tmpP.parent;
                    }
                    parameter2add.add(tmpP.children.get(1).token.str);
                    parameterType2add.add(tmpP.children.get(0).children.get(0).token.str);
                    newTable.records.add(new Symbol(tmpP.children.get(1).token.str,tmpP.children.get(0).children.get(0).token.str,false,"",tmpP.children.get(0).children.get(0).token.line));
                    if(newTable.recordsNames.contains(tmpP.children.get(1).token.str)){
                        error(tmpP.children.get(1).token,"Parameter Conflict");
                    }else newTable.recordsNames.add(tmpP.children.get(1).token.str);
                    tmpP = tmpP.parent.parent.parent;
                    ArrayList<Quaternions> nowQuaternions = new ArrayList<Quaternions>();
                    newTable.parent = SymbolTablePointer;
                    newTable.Quaternions = nowQuaternions;
                    Collections.reverse(parameter2add);
                    Collections.reverse(parameterType2add);
                    SymbolTablePointer.funcList.add(new Function(tmpP.children.get(1).token.str, tmpP.children.get(0).children.get(0).token.str, false, tmpP.children.get(0).children.get(0).token.line, parameter2add, newTable,parameterType2add));
                    SymbolTablePointer.funcNames.add(tmpP.children.get(1).token.str);
                    QuaternionsPointer = nowQuaternions;
                    SymbolTablePointer = newTable;
                    parent=parent.parent;
                }
            }
            case "ExtDefRest1"->{
                if(parent.children.get(0).V.equals("ExtDefRest2")) {
                    if (parent.children.get(0).children.get(0).V.equals("ε")) {
                        String The2add=parent.parent.children.get(1).token.str;
                        if (SymbolTablePointer.recordsNames.contains(The2add)) {
                            error(parent.parent.children.get(0).children.get(0).token, "Variable Redefine");
                        } else {
                            SymbolTablePointer.records.add(new Symbol(The2add, parent.parent.children.get(0).children.get(0).token.str, false, "0", parent.parent.children.get(0).children.get(0).token.line));
                            SymbolTablePointer.recordsNames.add(The2add);
                        }
                    } else {
                        TreeNode tmpP = parent.children.get(0);
                        ArrayList<String> symbol2add = new ArrayList<String>();
                        symbol2add.add(parent.parent.children.get(1).token.str);
                        while (!tmpP.children.get(0).V.equals("ε")) {
                            symbol2add.add(tmpP.children.get(1).token.str);
                            tmpP = tmpP.children.get(0);
                        }
                        for (String aSymbol : symbol2add) {
                            if (SymbolTablePointer.recordsNames.contains(aSymbol)) {
                                error(parent.parent.children.get(0).children.get(0).token, "Variable Redefine");
                            } else {
                                SymbolTablePointer.records.add(new Symbol(aSymbol, parent.parent.children.get(0).children.get(0).token.str, false, "0", parent.parent.children.get(0).children.get(0).token.line));
                                SymbolTablePointer.recordsNames.add(aSymbol);
                            }
                        }
                    }
                }else if(parent.children.get(1).V.equals("CompSt")){
                    for(Symbol aSym:SymbolTablePointer.records){
                        if(!aSym.USED){
                            System.out.println("Variable "+aSym.NAME+" is unused");
                        }
                    }
                    for(Function aFunc:SymbolTablePointer.funcList){
                        if(!aFunc.USED){
                            System.out.println("Function "+aFunc.NAME+" is unused");
                        }
                    }
                    SymbolTablePointer=SymbolTablePointer.parent;
                    QuaternionsPointer=SymbolTablePointer.Quaternions;
                }
            }
            case "OrExp"->{
                if(parent.children.get(0).V.equals("ε")){
                    parent.attribute.put("place","epsl");
                }else{
                    if(parent.children.get(2).attribute.get("place").equals("epsl")){
                        parent.attribute.put("place",parent.children.get(1).attribute.get("place"));
                    }else{
                        parent.attribute.put("place",newtemp());
                        QuaternionsPointer.add(new Quaternions("or", (String) parent.children.get(1).attribute.get("place"), (String) parent.children.get(2).attribute.get("place"), (String) parent.attribute.get("place")));
                    }
                }
            }
            case "RelationExp"->{
                if(parent.children.get(1).attribute.get("place").equals("epsl")){
                    parent.attribute.put("place",parent.children.get(0).attribute.get("place"));
                }else{
                    parent.attribute.put("place",newtemp());
                    QuaternionsPointer.add(new Quaternions("and", (String) parent.children.get(0).attribute.get("place"), (String) parent.children.get(1).attribute.get("place"), (String) parent.attribute.get("place")));
                }
            }
            case "AndExp"->{
                if(parent.children.get(0).V.equals("ε")){
                    parent.attribute.put("place","epsl");
                }else{
                    if(!parent.children.get(2).attribute.get("place").equals("epsl")){
                        parent.attribute.put("place",newtemp());
                        QuaternionsPointer.add(new Quaternions("and", (String) parent.children.get(1).attribute.get("place"), (String) parent.children.get(2).attribute.get("place"), (String) parent.attribute.get("place")));
                    }else{
                        parent.attribute.put("place",parent.children.get(1).attribute.get("place"));
                    }
                }
            }
            case "CompExp"->{
                parent.attribute.put("place",newtemp());
                QuaternionsPointer.add(new Quaternions((String) parent.children.get(1).attribute.get("cmp_op"), (String) parent.children.get(0).attribute.get("place"), (String) parent.children.get(2).attribute.get("place"), (String) parent.attribute.get("place")));
            }
            case "CmpOp"->{
                parent.attribute.put("cmp_op",parent.children.get(0).V);
            }
            case "ConditionalExp"->{
                parent.attribute.put("next",QuaternionsPointer.size());
                if(parent.children.get(1).attribute.get("place").equals("epsl")){
                    parent.attribute.put("place",parent.children.get(0).attribute.get("place"));
                }else{
                    parent.attribute.put("place",newtemp());
                    QuaternionsPointer.add(new Quaternions("or", (String) parent.children.get(0).attribute.get("place"), (String) parent.children.get(1).attribute.get("place"), (String) parent.attribute.get("place")));
                }
            }
            case "Factor"->{
                switch(parent.children.get(0).V){
                    case "id":
                        parent.attribute.put("place",parent.children.get(0).token.str);
                        SymbolTable tmpPointer=SymbolTablePointer;
                        while(!tmpPointer.recordsNames.contains(parent.attribute.get("place"))){
                            tmpPointer=tmpPointer.parent;
                            if(tmpPointer==null){
                                error(parent.children.get(0).token,"No Such Variable:"+parent.attribute.get("place"));
                                break;
                            }
                        }
                        if(tmpPointer!=null){
                            for(var record:tmpPointer.records){
                                record.USED=true;
                            }
                        }
                        break;
                    case "INTC":
                    case "DECI":
                        parent.attribute.put("place",parent.children.get(0).token.str);
                        break;
                    case "\'(\'":
                        parent.attribute.put("place",parent.children.get(1).attribute.get("place"));
                        break;
                    case "\'getReturn\'":
                        if(parent.children.get(1).children.get(1).children.get(0).V.equals("CallStmtRest")){
                            parent.attribute.put("place",parent.children.get(1).attribute.get("place"));
                        }
                        break;
                }
            }
            case "TermExtend"->{
                if(parent.children.get(0).V.equals("ε")){
                    parent.attribute.put("place","epsl");
                }else{
                    if(parent.children.get(2).attribute.get("place").equals("epsl")){
                        parent.attribute.put("place",parent.children.get(1).attribute.get("place"));
                    }else{
                        parent.attribute.put("place",newtemp());
                        if(parent.children.get(2).children.get(0).V.equals("\'*\'")){
                            QuaternionsPointer.add(new Quaternions("*", (String) parent.children.get(1).attribute.get("place"), (String) parent.children.get(2).attribute.get("place"), (String) parent.attribute.get("place")));
                        }else{
                            QuaternionsPointer.add(new Quaternions("/", (String) parent.children.get(1).attribute.get("place"), (String) parent.children.get(2).attribute.get("place"), (String) parent.attribute.get("place")));
                        }
                    }
                }
            }
            case "Term"->{
                if(parent.children.get(1).attribute.get("place").equals("epsl")){
                    parent.attribute.put("place",parent.children.get(0).attribute.get("place"));
                }else{
                    parent.attribute.put("place",newtemp());
                    if(parent.children.get(1).children.get(0).V.equals("\'*\'")){
                        QuaternionsPointer.add(new Quaternions("*", (String) parent.children.get(0).attribute.get("place"), (String) parent.children.get(1).attribute.get("place"), (String) parent.attribute.get("place")));
                    }else{
                        QuaternionsPointer.add(new Quaternions("/", (String) parent.children.get(0).attribute.get("place"), (String) parent.children.get(1).attribute.get("place"), (String) parent.attribute.get("place")));
                    }
                }
            }
            case "ExpExtend"->{
                if(parent.children.get(0).V.equals("ε")){
                    parent.attribute.put("place","epsl");
                }else{
                    if(parent.children.get(2).attribute.get("place").equals("epsl")){
                        parent.attribute.put("place",parent.children.get(1).attribute.get("place"));
                    }else{
                        parent.attribute.put("place",newtemp());
                        if(parent.children.get(2).children.get(0).V.equals("\'+\'")){
                            QuaternionsPointer.add(new Quaternions("+", (String) parent.children.get(1).attribute.get("place"), (String) parent.children.get(2).attribute.get("place"), (String) parent.attribute.get("place")));
                        }else{
                            QuaternionsPointer.add(new Quaternions("-", (String) parent.children.get(1).attribute.get("place"), (String) parent.children.get(2).attribute.get("place"), (String) parent.attribute.get("place")));
                        }
                    }
                }
            }
            case "Exp"->{
                if(parent.children.get(1).attribute.get("place").equals("epsl")){
                    parent.attribute.put("place",parent.children.get(0).attribute.get("place"));
                }else{
                    parent.attribute.put("place",newtemp());
                    if(parent.children.get(1).children.get(0).V.equals("\'+\'")){
                        QuaternionsPointer.add(new Quaternions("+", (String) parent.children.get(0).attribute.get("place"), (String) parent.children.get(1).attribute.get("place"), (String) parent.attribute.get("place")));
                    }else{
                        QuaternionsPointer.add(new Quaternions("-", (String) parent.children.get(0).attribute.get("place"), (String) parent.children.get(1).attribute.get("place"), (String) parent.attribute.get("place")));
                    }
                }
            }
            case "M4"->{
                parent.parent.attribute.put("In",QuaternionsPointer.size());
            }
            case "M1"->{
                QuaternionsPointer.add(new Quaternions("jnz",(String)parent.parent.children.get(2).attribute.get("place"),"0",QuaternionsPointer.size()+2+""));
                parent.attribute.put("quad",QuaternionsPointer.size());
                QuaternionsPointer.add(new Quaternions("j","0","0","-"));
            }
            case "M5"->{
                QuaternionsPointer.add(new Quaternions("jnz", (String) parent.parent.children.get(3).attribute.get("place"),"0",QuaternionsPointer.size()+2+""));
                parent.attribute.put("quad",QuaternionsPointer.size());
                QuaternionsPointer.add(new Quaternions("j","0","0","-"));
            }
            case "M3"->{
                QuaternionsPointer.add(new Quaternions("j","0","0",(int)parent.parent.attribute.get("In")+""));
                QuaternionsPointer.set((int)parent.parent.children.get(5).attribute.get("quad"),new Quaternions("j","0","0",QuaternionsPointer.size()+""));
                if(parent.parent.attribute.containsKey("BreakIndex")){
                    QuaternionsPointer.set((int)parent.parent.attribute.get("BreakIndex"),new Quaternions("j","-","-",QuaternionsPointer.size()+""));
                }
            }
            case "M2"->{
                parent.attribute.put("quad",QuaternionsPointer.size());
            }
            case "N"-> {
                if (parent.parent.children.get(2).children.get(0).V.equals("ε")) {
                    QuaternionsPointer.set((int)parent.parent.parent.children.get(4).attribute.get("quad"), new Quaternions("j", "0", "0", QuaternionsPointer.size()+""));
                } else {
                    parent.attribute.put("TrueBreak",QuaternionsPointer.size());
                    QuaternionsPointer.add(new Quaternions("j", "0", "0","-"));
                    QuaternionsPointer.set((int)parent.parent.parent.children.get(4).attribute.get("quad"), new Quaternions("j", "0", "0", QuaternionsPointer.size()+""));
                }
            }
            case "ElseBlock"->{
                if(!parent.children.get(0).V.equals("ε")){
                    QuaternionsPointer.set((int) parent.parent.children.get(1).attribute.get("TrueBreak"), new Quaternions("j", "0", "0", QuaternionsPointer.size()+""));
                }
            }
            case "AssignmentStmtRest"->{
                QuaternionsPointer.add(new Quaternions("=", (String) parent.children.get(1).attribute.get("place"), "-",parent.parent.parent.children.get(0).token.str));
            }
            case "BreakStmt"->{
                TreeNode tmp=parent.parent;
                while(true){
                    if(tmp.V.equals("Program")){
                        error(parent.children.get(0).token,"");
                        break;
                    }else if(tmp.V.equals("LoopStmt")){
                        tmp.attribute.put("BreakIndex",QuaternionsPointer.size());
                        QuaternionsPointer.add(new Quaternions("j","-","-","-"));
                        break;
                    }
                    tmp=tmp.parent;
                }
            }
            case "ContinueStmt"->{
                TreeNode tmp=parent.parent;
                while(true){
                    if(tmp.V.equals("Program")){
                        error(parent.children.get(0).token,"");
                        break;
                    }else if(tmp.V.equals("LoopStmt")){
                        QuaternionsPointer.add(new Quaternions("j","0","0",(int)tmp.attribute.get("In")+""));
                        break;
                    }
                    tmp=tmp.parent;
                }
            }
            case "ActParamList"->{
                if(parent.children.get(0).equals("ε")){
                    SymbolTable tmpPointer=SymbolTablePointer;
                    while(!tmpPointer.funcNames.contains(parent.parent.parent.parent.children.get(0).token.str)){
                        tmpPointer=tmpPointer.parent;
                        if(tmpPointer==null){
                            error(parent.parent.parent.parent.children.get(0).token,"No Such Function");
                            break;
                        }
                    }
                    if(tmpPointer!=null) {
                        if (tmpPointer.funcNames.contains(parent.parent.parent.parent.children.get(0).token.str)) {
                            for (Function afunc : tmpPointer.funcList) {
                                if (afunc.NAME.equals(parent.parent.parent.parent.children.get(0).token.str)) {
                                    if (afunc.HEAD == null) {
                                        int offset=QuaternionsPointer.size()+1;
                                        QuaternionsPointer.add(new Quaternions("begin",afunc.NAME,afunc.RETURN_TYPE,"-"));
                                        if(!afunc.USED) {
                                            for (Quaternions a : afunc.symbolTable.Quaternions) {
                                                if (isInteger(a.four)) {
                                                    int tmp = Integer.parseInt(a.four);
                                                    a.four = String.valueOf(tmp + offset);
                                                }
                                                tmpPointer.Quaternions.add(a);
                                            }
                                        }
                                        QuaternionsPointer.add(new Quaternions("end",afunc.NAME,"-","-"));
                                        String newTemp=newtemp();
                                        QuaternionsPointer.add(new Quaternions("call",afunc.NAME,"-",newTemp));
                                        parent.parent.parent.parent.attribute.put("place",newTemp);
                                    } else {
                                        error(parent.parent.parent.parent.children.get(0).token, "Lack of Parameter");
                                    }
                                    afunc.USED=true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            case "ReturnStmt"->{
                if(parent.children.get(1).children.get(0).V.equals("Exp")){
                    QuaternionsPointer.add(new Quaternions("return",(String)parent.children.get(1).children.get(0).attribute.get("place"),"-","-"));
                }else{
                    QuaternionsPointer.add(new Quaternions("return","-","-","-"));
                }
            }
            case "ActParamListExtend"->{
                if(parent.children.get(0).V.equals("ε")) {
                    TreeNode tmp=parent.parent;
                    ArrayList<String> ActPara=new ArrayList<String>();
                    while(!tmp.V.equals("ActParamList")) {
                        ActPara.add((String)tmp.children.get(1).attribute.get("place"));
                        tmp=tmp.parent;
                    }
                    ActPara.add((String)tmp.children.get(0).attribute.get("place"));
                    SymbolTable tmpPointer=SymbolTablePointer;
                    while(!tmpPointer.funcNames.contains(tmp.parent.parent.parent.children.get(0).token.str)){
                        tmpPointer=tmpPointer.parent;
                        if(tmpPointer==null){
                            error(parent.parent.parent.parent.parent.children.get(0).token,"No Such Function: "+parent.parent.parent.parent.parent.children.get(0).token.str);
                            break;
                        }
                    }
                    if(tmpPointer!=null) {
                        for(Function afunc:tmpPointer.funcList){
                            if(afunc.NAME.equals(tmp.parent.parent.parent.children.get(0).token.str)){
                                if(afunc.HEAD!=null) {
                                    if (afunc.HEAD.size() == ActPara.size()) {
                                        if(!afunc.USED) {
                                            int offset=QuaternionsPointer.size()+1;
                                            QuaternionsPointer.add(new Quaternions("begin",afunc.NAME,afunc.RETURN_TYPE,"-"));
                                            for(int aparamIndex=0;aparamIndex<afunc.HEAD.size();aparamIndex++){
                                                QuaternionsPointer.add(new Quaternions("param",afunc.HEAD.get(aparamIndex),afunc.HEADTYPE.get(aparamIndex),"-"));
                                                offset++;
                                            }
                                            for (Quaternions a : afunc.symbolTable.Quaternions) {
                                                Quaternions newQua = a;
                                                if (isInteger(a.four)&&!a.four.equals("-")) {
                                                    int tmp1 = Integer.parseInt(a.four);
                                                    newQua = new Quaternions(a.one, a.two, a.three, String.valueOf(tmp1 + offset));
                                                }
                                                SymbolTablePointer.Quaternions.add(newQua);
                                            }
                                            QuaternionsPointer.add(new Quaternions("end",afunc.NAME,"-","-"));
                                        }
                                        Collections.reverse(ActPara);
                                        for(String anActPara:ActPara){
                                            QuaternionsPointer.add(new Quaternions("arg",anActPara,"-","-"));
                                        }
                                        String newTemp=newtemp();
                                        QuaternionsPointer.add(new Quaternions("call",afunc.NAME,""+ActPara.size(),newTemp));
                                        tmp.parent.parent.parent.attribute.put("place",newTemp);
                                    } else {
                                        error(tmp.parent.parent.parent.children.get(0).token, "Parameter Amount Mismatchs");
                                    }
                                }else {
                                    error(tmp.parent.parent.parent.children.get(0).token, "No Need Parameter");
                                }
                                afunc.USED=true;
                                break;
                            }
                        }
                    }
                }
            }
            case "P"->{
                String Type=parent.parent.children.get(0).children.get(0).token.str;
                ArrayList<String> var2Define=new ArrayList<String>();
                var2Define.add(parent.parent.children.get(1).token.str);
                TreeNode tmpTN=parent.parent.children.get(2);
                while(!tmpTN.children.get(0).V.equals("ε")){
                    var2Define.add(tmpTN.children.get(1).token.str);
                    tmpTN=tmpTN.children.get(2);
                }
                for(String aVar:var2Define){
                    if(SymbolTablePointer.records.contains(aVar)){
                        error(parent.parent.children.get(0).children.get(0).token,"Variable "+aVar+" Redefined");
                    }else{
                        SymbolTablePointer.records.add(new Symbol(aVar,Type,false,"",parent.parent.children.get(0).children.get(0).token.line));
                        SymbolTablePointer.recordsNames.add(aVar);
                    }
                }
            }
            case "Program"->{
                for(Symbol aSym:SymbolTablePointer.records){
                    if(!aSym.USED){
                        System.out.println("Global Variable "+aSym.NAME+" is unused");
                    }
                }
                for(Function aFunc:SymbolTablePointer.funcList){
                    if(!aFunc.USED&&!aFunc.NAME.equals("main")){
                        System.out.println("Global Function "+aFunc.NAME+" is unused");
                    }
                }
            }
        }
    }
    private static void error(Token errorToken,String info) throws IOException {
        System.out.println(RED+"SemanticError in "+errorToken.line+":"+errorToken.col);
        switch(errorToken.str){
            case "break"->{
                System.out.println("——The break is out of loop");
            }
            case "continue"->{
                System.out.println("——The continue is out of loop");
            }
            default -> {
                System.out.println("——"+info);
            }
        }
    }
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }
}
