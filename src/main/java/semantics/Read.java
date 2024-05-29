package semantics;
/**
 * 语义分析的调用接口（该版本main函数所在，调用Check生成树）
 */

import parser.Check;
import parser.Store;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class Read {
    static String BLUE = "\u001B[34m";
    static String RED = "\u001B[31m";
    static String CYAN = "\u001B[36m";
    public static String sourceText;
    public static void main(String[] args) throws IOException {
        Scanner scn=new Scanner(System.in);
        try {
            System.out.println("请输入文本文件绝对路径");
            sourceText=scn.next().replace("\\","\\\\");
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
            System.out.println("\033[0m");
        }catch (Exception e){
            System.out.println(RED+"未定义错误");
        }
    }
}
