package semantics;

import java.util.ArrayList;
import java.util.HashSet;

public class SymbolTable {
    SymbolTable parent;
    ArrayList<Quaternions> Quaternions;
    ArrayList<Symbol> records=new ArrayList<Symbol>();
    HashSet<String> recordsNames=new HashSet<>();
    ArrayList<Function> funcList=new ArrayList<Function>();
    HashSet<String> funcNames=new HashSet<>();
}