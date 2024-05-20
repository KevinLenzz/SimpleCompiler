package semantics;

import java.util.ArrayList;
import java.util.Collections;

public class Function {
    String NAME;
    String RETURN_TYPE;
    boolean USED;
    int LINE_NUM;
    ArrayList<String> HEAD;
    ArrayList<String> HEADTYPE;
    SymbolTable symbolTable;
    Function(String a,String b,boolean c,int d,ArrayList<String> e,SymbolTable f,ArrayList<String> g){
        NAME=a;
        RETURN_TYPE=b;
        USED=c;
        LINE_NUM=d;
        HEAD=e;
        symbolTable=f;
        HEADTYPE=g;
    }
}