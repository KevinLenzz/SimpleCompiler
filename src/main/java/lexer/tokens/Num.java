package lexer.tokens;

import lexer.Type;

public class Num extends Token{
    public final int lexeme;
    public Num(int v,int l,int c){
        super(Type.INT,l,c,v+"");
        lexeme=v;
    }
}
