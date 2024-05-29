package lexer;
/**
 * 对象：词法分析错误
 */

import lexer.tokens.Token;

public class LexerError extends Token {
    public String lexerErrorInf;
    public LexerError(String s, int l, int c){
        super(Type.ERR,l,c,s);
        lexerErrorInf=new String(s);
    }
}
