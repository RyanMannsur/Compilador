package compilador;

public class Token {

    public final int tag; //constante que representa o token

    public Token(int t) {
        tag = t;
    }

    @Override
    public String toString() {
        return "" + tag;
    }
}
