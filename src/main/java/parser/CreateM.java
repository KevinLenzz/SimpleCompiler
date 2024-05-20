package parser;

import java.util.*;

public class CreateM {
    public static void create(){
        for(String i:Store.VN){
            HashMap<String,ArrayList<String>> tmp=new HashMap<String,ArrayList<String>>();
            tmp.put("#",new ArrayList<String>());
            for(String j:Store.VT){
                tmp.put(j,new ArrayList<String>());
            }
            Store.M.put(i,tmp);
        }
        for(String i:Store.VN){
            for(String j:Store.P.get(i)){
                String[] oneSingleP=j.split(" ");
                boolean flag;
                int k;
                for(k=0;k<oneSingleP.length;k++){
                    flag=false;
                    for(String m:Store.FIRST.get(oneSingleP[k])){
                        if(!m.equals("ε")&&!Store.FIRST.get(m).toString().contains("ε")){
                            Store.M.get(i).get(m).add(j);
                        }else{
                            flag=true;
                        }
                    }
                    if(!flag)break;
                }
                if(k==oneSingleP.length){
                    for(String b:Store.FOLLOW.get(i)){
                        Store.M.get(i).get(b).add(j);
                    }
                }
//                if(Store.FIRST.get(oneSingleP[0]).toString().contains("ε")){
//                    for(String n:Store.FOLLOW.get(i)){
//                        Store.M.get(i).get(n).add(j);
//                    }
//                }
            }
        }
        for(String i:Store.VN) {
            for(String j:Store.VT) {
                if(Store.M.get(i).get(j).size()>1) {
                    System.out.println("文法具有二义性"+i+" TO2 "+j);
                }
                if(Store.M.get(i).get(j).size()==0){
                    Store.M.get(i).get(j).add("Error");
                }
            }
        }
    }
}
