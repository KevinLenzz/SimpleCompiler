package semantics;

/**
 * 符号表（作用域）里四元式
 */
public class Quaternions {
    int index;
    String one;
    String two;
    String three;
    String four;
    Quaternions(String b,String c,String d,String e){
        one=b;
        two=c;
        three=d;
        four=e;
    }


    @Override
    public String toString() {
        return "("+one+","+two+","+three+","+four+")";
    }
}
