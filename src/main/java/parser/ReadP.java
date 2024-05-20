package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadP {
    public static void read(String path) throws IOException {
        FileReader fr=new FileReader(path);
        BufferedReader br=new BufferedReader(fr);
        String line;
        while((line=br.readLine())!=null){
            int arrowIndex=line.indexOf((int)'→');
            String left=line.substring(0,arrowIndex);
            String right=line.substring(arrowIndex+1,line.length());
            ArrayList<String> rightList= new ArrayList<String>(Arrays.asList(right.split("\\|")));
            ArrayList<String> rightAll= new ArrayList<String>(Arrays.asList(right.split("\\||\\s+")));
            Store.VN.add(left);
            Store.VT.addAll(rightAll);
            Store.NS.addAll(rightAll);
            Store.P.put(left,rightList);
        }
        Store.VT.removeAll(Store.VN);
    }
}
