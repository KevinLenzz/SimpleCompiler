package lexer.tokens;

public class KWord extends Token{
    public final String lexeme;
    public final int idx;
    public KWord(int t,String s,int i,int l,int c){
        super(t,l,c,s);
        lexeme=new String(s);
        idx=i;
    }
}
