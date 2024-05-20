package lexer.tokens;

public class Word extends Token{
    public final String lexeme;
    public Word(int t,String s,int l,int c){
        super(t,l,c,s);
        lexeme=new String(s);
    }
}
