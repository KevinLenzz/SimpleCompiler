package parser;

import java.util.HashSet;

public class PanicMode {
    public static void createSet(){
        for(String vn:Store.VN){
            HashSet<String> tmp=new HashSet<String>();
            Store.synchronizeSet.put(vn,tmp);
            Store.synchronizeSet.get(vn).addAll(Store.FOLLOW.get(vn));
        }
    }
}
