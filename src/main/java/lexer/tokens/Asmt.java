package lexer.tokens;

public class Asmt extends Token{
    public final String lexeme;
    public Asmt(int t, String s, int l, int c){
        super(t,l,c,s);
        lexeme=new String(s);
    }
}
