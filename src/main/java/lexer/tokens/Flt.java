package lexer.tokens;

import lexer.Type;

public class Flt extends Token{
    public final String ori_lexeme;
    public final float lexeme;
    public Flt(String s,int l,int c){
        super(Type.FLT,l,c,"");
        ori_lexeme=s;
        lexeme=Float.valueOf(s);
        str= "\'"+String.valueOf(lexeme)+"\'";
    }
}
