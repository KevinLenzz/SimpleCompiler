package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class CreateFIRST {
    private static void oneGet(String vn,HashSet<String> theFIRST){
        for(String oneSinglePright: Store.P.get(vn)){
            String[] tmp2=oneSinglePright.split(" ");
            String tmp1=tmp2[0];
            if(Store.VN.contains(tmp1)){
                oneGet(tmp1,theFIRST);
            }else{
                theFIRST.add(tmp1);
            }
        }
    }
    public static void create(){
        for(String vt: Store.VT){
            HashSet<String> theFIRST=new HashSet<String>();
            theFIRST.add(vt);
            Store.FIRST.put(vt,theFIRST);
        }
        for(String vn: Store.VN){
            HashSet<String> theFIRST=new HashSet<String>();
            oneGet(vn,theFIRST);
            Store.FIRST.put(vn,theFIRST);
        }
    }
}
