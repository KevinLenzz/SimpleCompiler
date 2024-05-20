package lexer.tokens;

public class Str extends Token{
    public final String lexeme;
    public Str(int t,String s,int l,int c){
        super(t,l,c,s);
        lexeme=new String("\""+s+"\"");
    }
}
