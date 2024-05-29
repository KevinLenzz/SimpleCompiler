package semantics;
/**
 * 单个符号表（同时发挥作用域功能）
 */

import java.util.ArrayList;
import java.util.HashSet;

public class SymbolTable {
    String ReturnTYPE;
    boolean hasReturn=false;
    SymbolTable parent;
    ArrayList<Quaternions> Quaternions=new ArrayList<Quaternions>();
    ArrayList<Symbol> records=new ArrayList<Symbol>();
    HashSet<String> recordsNames=new HashSet<>();
    ArrayList<Function> funcList=new ArrayList<Function>();
    HashSet<String> funcNames=new HashSet<>();
}