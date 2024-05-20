package semantics;

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
