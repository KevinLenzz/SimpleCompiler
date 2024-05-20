package lexer.tokens;

public class Sprt extends Token{
    public final char lexeme;
    public final int idx;
    public Sprt(int t, char s, int l, int c, int index){
        super(t,l,c,s+"");
        idx=index;
        lexeme=s;
    }
}