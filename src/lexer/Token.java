package lexer;

public record Token(TokenType tokenType, String value) {

    @Override
    public final String toString() {
        return "type: " + tokenType.name() + ", value: " + value;
    }

}
