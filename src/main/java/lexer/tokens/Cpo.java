package lexer.tokens;

public class Cpo extends Token{
    public final String lexeme;
    public final int idx;
    public Cpo(int t, String s, int l, int c, int index){
        super(t,l,c,s);
        idx=index;
        lexeme=new String(s);
    }
}
