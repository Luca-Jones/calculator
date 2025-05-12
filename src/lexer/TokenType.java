package lexer;

public enum TokenType {

    COMMAND             ("^(rad|deg|grad|reg|sci|eng|clear|cls|exit)"),
    NUMBER              ("^\\d*\\.?\\d+"),
    PLUS                ("^\\+"),
    MINUS               ("^-"),
    EXPONENT            ("^\\^"),
    PREFIX_OPERATOR     ("^(~|sqrt|sin|cos|tan)"),
    INFIX_OPERATOR      ("^([&%E\\|\\*\\/]|<<|>>)"),
    POSTFIX_OPERATOR    ("^!"),
    OPEN_BRACKET        ("^\\("),
    CLOSE_BRACKET       ("^\\)"),
    CONSTANT            ("^(e|pi|ans)"),
    ASSIGNMENT          ("^(var)"),
    VARIABLE            ("^[a-z_]+"),
    EQUALS              ("^="),
    ;

    private String regex;

    private TokenType(String regex) {
        this.regex = regex;
    }

    public String regex() {
        return new String(this.regex);
    }

}
