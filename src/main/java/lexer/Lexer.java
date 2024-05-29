package lexer;
/**
 * 词法分析器主要部分
 */

import lexer.tokens.*;

import java.io.FileReader;
import java.io.IOException;

public class Lexer {
    private int line=1;
    private int peekLidx=0;
    private char peek=' ';
    public Token scan(FileReader a) throws IOException{
        if(peek==65535){
            return new End(Type.END,line,peekLidx);
        }//读到结尾
        for(;;peek= (char) a.read()){
            if(peek==65535){
                return new End(Type.END,line,peekLidx);
            }else if(peek==' '||peek=='\t') {
                peekLidx++;
            }
            else if(peek=='\r'){
                peek= (char) a.read();
                peekLidx=1;
                line=line+1;
            }else if(peek=='/'){
                peek=(char) a.read();
                peekLidx++;
                if(peek=='/'){
                    for(;;peek= (char) a.read()) {
                        if(peek==65535){
                            return new End(Type.END,line,peekLidx);
                        }else if(peek=='\r'){
                            peek= (char) a.read();
                            peekLidx=1;
                            line=line+1;
                            break;
                        }
                    }
                }else if(peek=='*'){
                    while(true) {
                        if(peek==65535){
                            return new End(Type.END,line,peekLidx);
                        }else if(peek=='\r'){
                            peek= (char) a.read();
                            peekLidx=1;
                            line=line+1;
                        }else if(peek=='*'){
                            peek= (char) a.read();
                            peekLidx++;
                            if(peek=='/'){
                                peekLidx++;
                                break;
                            }
                        }else{
                            peek= (char) a.read();
                            peekLidx++;
                        }
                    }
                }else{
                    return new Oprt(Type.OPRT,'/',line,peekLidx,Idxs.oprtidx.get('/'));
                }
            }
            else break;
        }//跳过空白与注释
        int tmpPeekLidx=peekLidx;//暂存首字符
        if(Character.isLetter(peek)||peek=='_'){
            StringBuilder b=new StringBuilder();
            int numCount=0;
            do {
                peekLidx++;
                if(Character.isDigit(peek))numCount++;
                if(numCount>10)break;
                b.append(peek);
                peek=(char) a.read();
            }while((Character.isLetterOrDigit(peek)||peek=='_')&&b.length()<32);
            if(b.length()==32||numCount>10) {
                skip(a);
            }
            String s=b.toString();
            if(Idxs.kwdidx.containsKey(s)){
                KWord w=new KWord(Type.KWD,s,Idxs.kwdidx.get(s),line,tmpPeekLidx);
                return w;
            }else{
                Word w=new Word(Type.ID,s,line,tmpPeekLidx);
                return w;
            }
        }//关键字标识符
        if(Character.isDigit(peek)){
            if(peek=='0'){
                peekLidx++;
                peek=(char) a.read();
                if(Character.isDigit(peek)){
                    int tmp=peekLidx-1;
                    skip(a);
                    return new LexerError("DigitZeroLeadError:in "+line+":"+tmpPeekLidx,line,tmp);
                }else if(peek=='.'){
                    StringBuilder stringBuilder=new StringBuilder();
                    stringBuilder.append("0");
                    stringBuilder.append(peek);
                    peek=(char) a.read();
                    peekLidx++;
                    if(!Character.isDigit(peek))return new LexerError("Unacceptable float in "+line+":"+tmpPeekLidx,line,tmpPeekLidx);
                    do{
                        stringBuilder.append(peek);
                        peek=(char) a.read();
                        peekLidx++;
                    }while(Character.isDigit(peek));
                    return new Flt(stringBuilder.toString(),line,tmpPeekLidx);
                }
                else{
                    return new Num(0,line,tmpPeekLidx);
                }
            }else {
                int v = 0;
                while(true){
                    v = 10 * v + Character.digit(peek, 10);
                    peek=(char) a.read();
                    peekLidx++;
                    if(v>65536){
                        int tmp2=peekLidx-1;
                        skip(a);
                        return new LexerError("DigitOverError:in "+line+":"+tmpPeekLidx,line,tmp2);
                    }
                    if(!Character.isDigit(peek)){
                        if(peek=='.'){
                            StringBuilder stringBuilder=new StringBuilder();
                            stringBuilder.append(v+"");
                            stringBuilder.append(peek);
                            peek=(char) a.read();
                            peekLidx++;
                            if(!Character.isDigit(peek))return new LexerError("Unacceptable float in "+line+":"+tmpPeekLidx,line,tmpPeekLidx);
                            do{
                                stringBuilder.append(peek);
                                peek=(char) a.read();
                                peekLidx++;
                            }while(Character.isDigit(peek));
                            return new Flt(stringBuilder.toString(),line,tmpPeekLidx);
                        }else return new Num(v,line,tmpPeekLidx);
                    }
                }
            }
        }//数字
        if(peek=='.') {
            peek=(char)a.read();
            peekLidx++;
            skip(a);
            return new LexerError("Unacceptable float in "+line+":"+tmpPeekLidx,line,tmpPeekLidx);
        }
        if(Idxs.oprtidx.containsKey(peek)){
            int tmpOprtIdx=Idxs.oprtidx.get(peek);
            char tmpOprt=peek;
            peek = (char) a.read();
            peekLidx++;
            return new Oprt(Type.OPRT,tmpOprt,line,tmpPeekLidx,tmpOprtIdx);
        }//四则运算符
        if(Idxs.sprtidx.containsKey(peek)){
            int tmpSprtIdx=Idxs.sprtidx.get(peek);
            char tmpSprt=peek;
            peek = (char) a.read();
            peekLidx++;
            return new Sprt(Type.SPRT,tmpSprt,line,tmpPeekLidx,tmpSprtIdx);
        }//分隔符
        switch(peek){
            case '='-> {
                peek = (char) a.read();
                peekLidx++;
                if (peek == '=') {
                    peek = (char) a.read();
                    peekLidx++;
                    return new Cpo(Type.CPO,"==",line,tmpPeekLidx,Idxs.cmpidx.get("=="));
                } else {
                    return new Asmt(Type.ASMT,"=",line,tmpPeekLidx);
                }
            }
            case '>'-> {
                peek = (char) a.read();
                peekLidx++;
                if (peek == '=') {
                    peek = (char) a.read();
                    peekLidx++;
                    return new Cpo(Type.CPO,">=",line,tmpPeekLidx,Idxs.cmpidx.get(">="));
                } else {
                    return new Cpo(Type.CPO,">",line,tmpPeekLidx,Idxs.cmpidx.get(">"));
                }
            }
            case '<'-> {
                peek = (char) a.read();
                peekLidx++;
                if (peek == '=') {
                    peek = (char) a.read();
                    peekLidx++;
                    return new Cpo(Type.CPO,"<=",line,tmpPeekLidx,Idxs.cmpidx.get("<="));
                } else {
                    return new Cpo(Type.CPO,"<",line,tmpPeekLidx,Idxs.cmpidx.get("<"));
                }
            }
            case '!'-> {
                peek = (char) a.read();
                peekLidx++;
                if (peek == '=') {
                    peek = (char) a.read();
                    peekLidx++;
                    return new Cpo(Type.CPO, "!=", line, tmpPeekLidx, Idxs.cmpidx.get("!="));
                }else{
                    return new LexerError("Undeclared Identifier in "+line+":"+(peekLidx-1),line,tmpPeekLidx);
                }
            }
        }//比较运算符
        if(peek=='\"'){
            peek= (char) a.read();
            peekLidx++;
            StringBuilder stringBuilder=new StringBuilder();
            while(true) {
                if(peek==65535){
                    return new LexerError("Undeclared Identifier in "+line+":"+(peekLidx-1),line,peekLidx-1);
                }else if(peek=='\n'){
                    stringBuilder.append('\n');
                    peek= (char) a.read();
                    peekLidx=1;
                    line=line+1;
                }else if(peek=='\"'){
                    peek= (char) a.read();
                    peekLidx++;
                    return new Str(Type.STR,stringBuilder.toString(),line,tmpPeekLidx);
                }else{
                    stringBuilder.append(peek);
                    peek= (char) a.read();
                    peekLidx++;
                }
            }
        }//字符串
        Token t=new LexerError("Undeclared Identifier in "+line+":"+tmpPeekLidx,line,tmpPeekLidx);
        peek=' ';
        return t;
    }
    private void skip(FileReader a) throws IOException {
        for(;;peek= (char) a.read()){
            if(peek==' '||peek=='\t') {
                break;
            }
            else if(peek=='\n'){
                break;
            }else if(Character.isLetterOrDigit(peek)||peek=='_'){
                peekLidx++;
            }else{
                break;
            }
        }
    }//截断
}
