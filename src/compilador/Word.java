package compilador;

public class Word extends Token {

    private String type = "";
    private String lexeme = "";
    public static final Word add = new Word("+", Tag.ADD);
    public static final Word sub = new Word("-", Tag.SUB);
    public static final Word mul = new Word("*", Tag.MUL);
    public static final Word div = new Word("/", Tag.DIV);
    public static final Word not = new Word("!", Tag.NOT);
    public static final Word ach = new Word("{", Tag.ACH);
    public static final Word fch = new Word("}", Tag.FCH);
    public static final Word apa = new Word("(", Tag.APA);
    public static final Word fpa = new Word(")", Tag.FPA);
    public static final Word vir = new Word(",", Tag.VIR);
    public static final Word pvi = new Word(";", Tag.PVI);
    public static final Word and = new Word("&&", Tag.AND);
    public static final Word or = new Word("||", Tag.OR);
    public static final Word eq = new Word("==", Tag.EQ);
    public static final Word ne = new Word("!=", Tag.NE);
    public static final Word le = new Word("<=", Tag.LE);
    public static final Word ge = new Word(">=", Tag.GE);
    public static final Word atr = new Word("=", Tag.ATR);
    public static final Word men = new Word("<", Tag.MEN);
    public static final Word mai = new Word(">", Tag.MAI);
    //adicionar as palavras reservadas
    public static final Word If = new Word("if", Tag.IF);
    public static final Word Else = new Word("else", Tag.ELS);
    public static final Word Read = new Word("read", Tag.READ);
    public static final Word Write = new Word("write", Tag.WRI);
    public static final Word While = new Word("while", Tag.WHI);
    public static final Word Int = new Word("int", Tag.INT);
    public static final Word Float = new Word("float", Tag.FLOAT);
    public static final Word String = new Word("string", Tag.STR);
    public static final Word Do = new Word("do", Tag.DO);
    public static final Word Class = new Word("class", Tag.CLAS);

    public Word(String s, int tag) {
        super(tag);
        lexeme = s;
    }
    
    public Word(String s,String t, int tag) {
        super(tag);
        lexeme = s;
        type = t;
    }

    public String getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
        
    }
    
    @Override
    public String toString() {
        return "" + lexeme;
    }
    
    
}
