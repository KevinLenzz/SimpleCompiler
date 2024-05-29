package lexer;

import lexer.tokens.*;
import semantics.Read;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 读单词，词法分析器的调用接口
 */
public class GetToken {
    static FileReader fis;

    static {
        try {
            fis = new FileReader(Read.sourceText);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static Lexer lex=new Lexer();

    public GetToken() throws FileNotFoundException {
    }

    public static Token getToken() throws IOException {
        //FileReader fis=new FileReader(args[0]);
//        while(true) {
            Token a = lex.scan(fis);
            if(a instanceof LexerError)System.out.println(((LexerError) a).lexerErrorInf);
            return a;
//            System.out.println(switch(a){
//                case Num t->"("+t.type + "," + t.lexeme+","+t.line+","+t.col+")";
//                case Word t->"(" +t.type + "," + t.lexeme+","+t.line+","+t.col+")";
//                case Asmt t->"("+t.type + "," + t.lexeme+","+t.line+","+t.col+")";
//                case Str t->"("+t.type+","+ t.lexeme+","+t.line+","+t.col+")";
//                case Flt t->"("+t.type+","+ t.lexeme+","+t.line+","+t.col+")";
//                case KWord t->"("+t.type + "," + t.lexeme+","+t.line+","+t.col+","+t.idx+")";
//                case Cpo t->"("+t.type + "," + t.lexeme+","+t.line+","+t.col+","+t.idx+")";
//                case Oprt t->"("+t.type + "," + t.lexeme+","+t.line+","+t.col+","+t.idx+")";
//                case Sprt t->"("+t.type + "," + t.lexeme+","+t.line+","+t.col+" "+t.idx+")";
//                case End t->"end";
//                case ComplierError t->t.errorInf;
//                default -> "Undeclared Identifier in "+a.line+":"+a.col;
//            });
//            if(a instanceof End)break;
//        }
    }
}