package lexer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.SyntaxException;

public class Lexer {

    private final String input;
    private int cursor;

    public Lexer(String input) {
        this.input = input.replaceAll("\\s+","");
        this.cursor = 0;
    }

    public boolean hasMoreTokens() {
        return cursor < input.length();
    }

    public Token getNextToken() throws SyntaxException {

        if (!hasMoreTokens()) {
            return null;
        }

        String tokenValue = "";

        for (TokenType tokenType : TokenType.values()) {

            Pattern pattern = Pattern.compile(tokenType.regex());
            String substring = input.substring(cursor);
            Matcher matcher = pattern.matcher(substring);

            if (matcher.find()) {
                // System.out.println("match found in " + substring + " for regex " + tokenType.regex() + ": " + matcher.group());
                tokenValue = matcher.group();
                cursor += tokenValue.length();
                return new Token(tokenType, tokenValue);
            }

        }

        throw new SyntaxException("Unexpected token at index " + cursor + ": " + input.charAt(cursor));
    }

    public int getCursor() {
        return cursor;
    }
    
}
