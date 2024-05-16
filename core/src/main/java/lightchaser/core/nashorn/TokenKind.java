package lightchaser.core.nashorn;

public enum TokenKind {
    SPECIAL,
    UNARY,
    BINARY,
    BRACKET,
    KEYWORD,
    LITERAL,
    IR,
    FUTURE,
    FUTURESTRICT;

    private TokenKind() {
    }
}
