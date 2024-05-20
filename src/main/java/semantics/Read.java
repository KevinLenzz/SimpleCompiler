package semantics;

import parser.Check;
import parser.Store;

import java.io.IOException;

public class Read {
    static String BLUE = "\u001B[34m";
    static String RED = "\u001B[31m";
    static String CYAN = "\u001B[36m";
    public static void main(String[] args) throws IOException {
        try {
            Check.check();
            SymbolTable globalTable = new SymbolTable();
            CalAttr.SymbolTablePointer = globalTable;
            CalAttr.LRD(Check.S);
            if (CalAttr.MainQuaternionsPointer == null) {
                System.out.println("Can't not find main");
            } else {
                for (int i = 0; i < CalAttr.MainQuaternionsPointer.size(); i++) {
                    System.out.println(BLUE + i + " " + CalAttr.MainQuaternionsPointer.get(i));
                }
            }
            System.out.println(CYAN + "Sementic Check End");
        }catch (Exception e){
            System.out.println(RED+"未定义错误");
        }
    }
}
