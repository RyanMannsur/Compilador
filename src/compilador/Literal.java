package compilador;

public class Literal extends Token{

    public final String value;

    public Literal(String value) {
        super(Tag.LIT);
        this.value = value;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
