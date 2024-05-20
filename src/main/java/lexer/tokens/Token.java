package lexer.tokens;

public class Token {
    public final int type;
    public final int line;
    public final int col;
    public String str;
    public Token(int t, int l, int c, String str){
        type=t;
        line=l;
        col=c;
        this.str = "\'"+str+"\'";
    }
}
