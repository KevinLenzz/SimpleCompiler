package semantics;

/**
 * 符号表中的单个变量记录模子
 */
public class Symbol {
    String NAME;
    String TYPE;
    boolean USED;
    String CTNT;
    int LINE_NUM;
    Symbol(String a,String b,boolean c,String d,int e){
        NAME=a;
        TYPE=b;
        USED=c;
        CTNT=d;
        LINE_NUM=e;
    }
}