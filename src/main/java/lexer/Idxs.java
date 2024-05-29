package lexer;
import java.util.HashMap;
import java.util.Map;

/**
 * 关键字与符号集合
 */
public class Idxs {
    public final static Map<String, Integer> kwdidx = Map.ofEntries (
            Map.entry("int", 1),
            Map.entry("float", 2),
            Map.entry("string", 3),
            Map.entry("if", 4),
            Map.entry("else", 5),
            Map.entry("while", 6),
            Map.entry("return", 7),
            Map.entry("break", 8),
            Map.entry("and", 9),
            Map.entry("or", 10),
            Map.entry("continue", 11),
            Map.entry("getReturn", 12),
            Map.entry("void", 13)
    );
    public final static Map<Character, Integer> sprtidx = Map.of (
        '(', 1,
        ')', 2,
        '{', 3,
        '}', 4,
        ';', 5,
        ',', 6
    );
    public final static Map<Character, Integer> oprtidx = Map.of(
            '+', 1,
            '-', 2,
            '*', 3,
            '/', 4
    );
    public final static Map<String,Integer> cmpidx=Map.of (
        "==",1,
        "<",2,
        ">",3,
        "<=",4,
        ">=",5,
        "!=",6
    );
}
