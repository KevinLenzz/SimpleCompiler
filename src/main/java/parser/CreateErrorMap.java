package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CreateErrorMap {
    public static void create(String path) throws IOException {
        FileReader fr=new FileReader(path);
        BufferedReader br=new BufferedReader(fr);
        String line;
        while((line=br.readLine())!=null) {
            String[] tmp=line.split("\\s+");
            Store.ErrorMap.put(tmp[0]+" "+tmp[1],tmp[2]);
        }
    }
}
