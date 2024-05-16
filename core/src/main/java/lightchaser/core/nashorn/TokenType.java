package lightchaser.core.nashorn;


import java.util.Locale;

public enum TokenType {
    ERROR(TokenKind.SPECIAL, (String)null),
    EOF(TokenKind.SPECIAL, (String)null),
    EOL(TokenKind.SPECIAL, (String)null),
    COMMENT(TokenKind.SPECIAL, (String)null),
    DIRECTIVE_COMMENT(TokenKind.SPECIAL, (String)null),
    NOT(TokenKind.UNARY, "!", 14, false),
    NE(TokenKind.BINARY, "!=", 9, true),
    NE_STRICT(TokenKind.BINARY, "!==", 9, true),
    MOD(TokenKind.BINARY, "%", 13, true),
    ASSIGN_MOD(TokenKind.BINARY, "%=", 2, false),
    BIT_AND(TokenKind.BINARY, "&", 8, true),
    AND(TokenKind.BINARY, "&&", 5, true),
    ASSIGN_BIT_AND(TokenKind.BINARY, "&=", 2, false),
    LPAREN(TokenKind.BRACKET, "(", 16, true),
    RPAREN(TokenKind.BRACKET, ")", 0, true),
    MUL(TokenKind.BINARY, "*", 13, true),
    ASSIGN_MUL(TokenKind.BINARY, "*=", 2, false),
    ADD(TokenKind.BINARY, "+", 12, true),
    INCPREFIX(TokenKind.UNARY, "++", 15, true),
    ASSIGN_ADD(TokenKind.BINARY, "+=", 2, false),
    COMMARIGHT(TokenKind.BINARY, ",", 1, true),
    SUB(TokenKind.BINARY, "-", 12, true),
    DECPREFIX(TokenKind.UNARY, "--", 15, true),
    ASSIGN_SUB(TokenKind.BINARY, "-=", 2, false),
    PERIOD(TokenKind.BRACKET, ".", 17, true),
    DIV(TokenKind.BINARY, "/", 13, true),
    ASSIGN_DIV(TokenKind.BINARY, "/=", 2, false),
    COLON(TokenKind.BINARY, ":"),
    SEMICOLON(TokenKind.BINARY, ";"),
    LT(TokenKind.BINARY, "<", 10, true),
    SHL(TokenKind.BINARY, "<<", 11, true),
    ASSIGN_SHL(TokenKind.BINARY, "<<=", 2, false),
    LE(TokenKind.BINARY, "<=", 10, true),
    ASSIGN(TokenKind.BINARY, "=", 2, false),
    EQ(TokenKind.BINARY, "==", 9, true),
    EQ_STRICT(TokenKind.BINARY, "===", 9, true),
    BIND(TokenKind.BINARY, "=>", 9, true),
    GT(TokenKind.BINARY, ">", 10, true),
    GE(TokenKind.BINARY, ">=", 10, true),
    SAR(TokenKind.BINARY, ">>", 11, true),
    ASSIGN_SAR(TokenKind.BINARY, ">>=", 2, false),
    SHR(TokenKind.BINARY, ">>>", 11, true),
    ASSIGN_SHR(TokenKind.BINARY, ">>>=", 2, false),
    TERNARY(TokenKind.BINARY, "?", 3, false),
    LBRACKET(TokenKind.BRACKET, "[", 17, true),
    RBRACKET(TokenKind.BRACKET, "]", 0, true),
    BIT_XOR(TokenKind.BINARY, "^", 7, true),
    ASSIGN_BIT_XOR(TokenKind.BINARY, "^=", 2, false),
    LBRACE(TokenKind.BRACKET, "{"),
    BIT_OR(TokenKind.BINARY, "|", 6, true),
    ASSIGN_BIT_OR(TokenKind.BINARY, "|=", 2, false),
    OR(TokenKind.BINARY, "||", 4, true),
    RBRACE(TokenKind.BRACKET, "}"),
    BIT_NOT(TokenKind.UNARY, "~", 14, false),
    BREAK(TokenKind.KEYWORD, "break"),
    CASE(TokenKind.KEYWORD, "case"),
    CATCH(TokenKind.KEYWORD, "catch"),
    CLASS(TokenKind.FUTURE, "class"),
    CONST(TokenKind.KEYWORD, "const"),
    CONTINUE(TokenKind.KEYWORD, "continue"),
    DEBUGGER(TokenKind.KEYWORD, "debugger"),
    DEFAULT(TokenKind.KEYWORD, "default"),
    DELETE(TokenKind.UNARY, "delete", 14, false),
    DO(TokenKind.KEYWORD, "do"),
    ELSE(TokenKind.KEYWORD, "else"),
    ENUM(TokenKind.FUTURE, "enum"),
    EXPORT(TokenKind.FUTURE, "export"),
    EXTENDS(TokenKind.FUTURE, "extends"),
    FALSE(TokenKind.LITERAL, "false"),
    FINALLY(TokenKind.KEYWORD, "finally"),
    FOR(TokenKind.KEYWORD, "for"),
    FUNCTION(TokenKind.KEYWORD, "function"),
    IF(TokenKind.KEYWORD, "if"),
    IMPLEMENTS(TokenKind.FUTURESTRICT, "implements"),
    IMPORT(TokenKind.FUTURE, "import"),
    IN(TokenKind.BINARY, "in", 10, true),
    INSTANCEOF(TokenKind.BINARY, "instanceof", 10, true),
    INTERFACE(TokenKind.FUTURESTRICT, "interface"),
    LET(TokenKind.FUTURESTRICT, "let"),
    NEW(TokenKind.UNARY, "new", 17, false),
    NULL(TokenKind.LITERAL, "null"),
    PACKAGE(TokenKind.FUTURESTRICT, "package"),
    PRIVATE(TokenKind.FUTURESTRICT, "private"),
    PROTECTED(TokenKind.FUTURESTRICT, "protected"),
    PUBLIC(TokenKind.FUTURESTRICT, "public"),
    RETURN(TokenKind.KEYWORD, "return"),
    STATIC(TokenKind.FUTURESTRICT, "static"),
    SUPER(TokenKind.FUTURE, "super"),
    SWITCH(TokenKind.KEYWORD, "switch"),
    THIS(TokenKind.KEYWORD, "this"),
    THROW(TokenKind.KEYWORD, "throw"),
    TRUE(TokenKind.LITERAL, "true"),
    TRY(TokenKind.KEYWORD, "try"),
    TYPEOF(TokenKind.UNARY, "typeof", 14, false),
    VAR(TokenKind.KEYWORD, "var"),
    VOID(TokenKind.UNARY, "void", 14, false),
    WHILE(TokenKind.KEYWORD, "while"),
    WITH(TokenKind.KEYWORD, "with"),
    YIELD(TokenKind.FUTURESTRICT, "yield"),
    DECIMAL(TokenKind.LITERAL, (String)null),
    OCTAL(TokenKind.LITERAL, (String)null),
    HEXADECIMAL(TokenKind.LITERAL, (String)null),
    FLOATING(TokenKind.LITERAL, (String)null),
    STRING(TokenKind.LITERAL, (String)null),
    ESCSTRING(TokenKind.LITERAL, (String)null),
    EXECSTRING(TokenKind.LITERAL, (String)null),
    IDENT(TokenKind.LITERAL, (String)null),
    REGEX(TokenKind.LITERAL, (String)null),
    XML(TokenKind.LITERAL, (String)null),
    OBJECT(TokenKind.LITERAL, (String)null),
    ARRAY(TokenKind.LITERAL, (String)null),
    COMMALEFT(TokenKind.IR, (String)null),
    DECPOSTFIX(TokenKind.IR, (String)null),
    INCPOSTFIX(TokenKind.IR, (String)null);

    private TokenType next = null;
    private final TokenKind kind;
    private final String name;
    private final int precedence;
    private final boolean isLeftAssociative;
    private static final TokenType[] values = values();

    private TokenType(TokenKind kind, String name) {
        this.kind = kind;
        this.name = name;
        this.precedence = 0;
        this.isLeftAssociative = false;
    }

    private TokenType(TokenKind kind, String name, int precedence, boolean isLeftAssociative) {
        this.kind = kind;
        this.name = name;
        this.precedence = precedence;
        this.isLeftAssociative = isLeftAssociative;
    }

    public boolean needsParens(TokenType other, boolean isLeft) {
        return other.precedence != 0 && (this.precedence > other.precedence || this.precedence == other.precedence && this.isLeftAssociative && !isLeft);
    }

    public boolean isOperator(boolean noIn) {
        return this.kind == TokenKind.BINARY && (!noIn || this != IN) && this.precedence != 0;
    }

    public int getLength() {
        assert this.name != null : "Token name not set";

        return this.name.length();
    }

    public String getName() {
        return this.name;
    }

    public String getNameOrType() {
        return this.name == null ? super.name().toLowerCase(Locale.ENGLISH) : this.name;
    }

    public TokenType getNext() {
        return this.next;
    }

    public void setNext(TokenType next) {
        this.next = next;
    }

    public TokenKind getKind() {
        return this.kind;
    }

    public int getPrecedence() {
        return this.precedence;
    }

    public boolean isLeftAssociative() {
        return this.isLeftAssociative;
    }

    boolean startsWith(char c) {
        return this.name != null && this.name.length() > 0 && this.name.charAt(0) == c;
    }

    static TokenType[] getValues() {
        return values;
    }

    public String toString() {
        return this.getNameOrType();
    }
}
