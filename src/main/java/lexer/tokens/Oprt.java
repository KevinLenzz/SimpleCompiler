package lexer.tokens;

public class Oprt extends Token{
    public final char lexeme;
    public final int idx;
    public Oprt(int t, char s, int l, int c, int index){
        super(t,l,c,s+"");
        idx=index;
        lexeme=s;
    }
}
