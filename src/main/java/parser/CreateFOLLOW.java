package parser;
/**
 * 生成FOLLOW集
 */

import java.lang.instrument.Instrumentation;
import java.security.DrbgParameters;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateFOLLOW {
    public static void create(){
        for(String i:Store.VN){
            Store.FOLLOW.put(i,new HashSet<String>());
            if(!Store.NS.contains(i)){
                Store.S=i;
                Store.FOLLOW.get(Store.S).add("#");
            }
        }
        boolean flag=true;
        int p=0;
        while(flag) {
            p++;
            flag=false;
            for (String vn : Store.VN) {
                for (String oneSinglePright : Store.P.get(vn)) {
                    String[] prightCut = oneSinglePright.split("\\s+");
                    for (int i = 0; i < prightCut.length; i++) {
                        String a = prightCut[i];
                        if (Store.VT.contains(a)) {
                            continue;
                        }
                        int tmp=Store.FOLLOW.get(a).size();
                        String b;
                        int n = 0;
                        while ((i + 1 + n) < prightCut.length) {
                            b = prightCut[i + 1 + n];
                            if (Store.VN.contains(b)) {
                                Store.FOLLOW.get(a).addAll(Store.FIRST.get(b));
                            } else {
                                Store.FOLLOW.get(a).add(b);
                                break;
                            }
                            if (!Store.FOLLOW.get(a).contains("ε")) {
                                break;
                            }
                            Store.FOLLOW.get(a).remove("ε");
                            n++;
                        }
                        if (i + 1 + n == prightCut.length) {
                            Store.FOLLOW.get(a).addAll(Store.FOLLOW.get(vn));
                        }
                        if(tmp!=Store.FOLLOW.get(a).size()){
                            flag=true;
                        }
                    }
                }
            }
        }
    }
}
