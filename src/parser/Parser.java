package parser;

import ast_nodes.ASTNode;
import ast_nodes.BinaryOperatorNode;
import ast_nodes.CommandNode;
import ast_nodes.NumberNode;
import ast_nodes.UnaryOperatorNode;
import ast_nodes.VariableNode;
import lexer.Lexer;
import lexer.Token;
import lexer.TokenType;

/**
 * Takes tokens from the Lexer and produces an Abstract Syntax Tree (AST)
 * using recusrive descent parsing (LL1 parser).
 * 
 * The grammar is roughly as follows (terminals denoted with <>):
 * 
 * Expression ::= Term {-|+ Term}
 * Term       ::= Factor {<InfixBinaryOperator> Term} | Factor {<PostfixOperator>} | Factor
 * Factor     ::= <PrefixOperator> Factor | base {^ Exponent} | Base
 * Exponent   ::= (Expression) | <Number>
 * Base       ::= (Expression) | <Number>
 * 
 * 
 * More Sinply:
 * E -> E [(-|+) T]*
 * T -> T [(*|/) F]*
 * F -> F ^ X | B
 * B -> i | (E)
 * X -> i | (E)
 */
public class Parser {

    private Lexer lexer;
    private Token nextToken;

    public Parser() {
        this.nextToken = null;
        this.lexer = null;
    }
    
    public ASTNode parse(String input) throws SyntaxException {

        lexer = new Lexer(input);
        nextToken = lexer.getNextToken();

        // Check if a command is being used
        if (checkNextTokenType(TokenType.COMMAND)) {
            Token commandToken = eat(TokenType.COMMAND);
            return new CommandNode(commandToken.value());
        }

        // build the expression
        // return parseExpression();
        return new VariableNode("ans", parseExpression().evaluate());
    }

    /*
     * Expression
     * : Variable = Expression
     * : Term {-|+ Term}
     * ;
     */
    private ASTNode parseExpression() {

        if (checkNextTokenType(TokenType.ASSIGNMENT)) {
            return parseVariableAssignment();
        }

        ASTNode term = parseTerm();

        while(true) {

            if (checkNextTokenType(TokenType.PLUS)) {
                Token plusToken = eat(TokenType.PLUS);
                ASTNode rightTerm = parseTerm();
                term = new BinaryOperatorNode(plusToken.value(), term, rightTerm); // case 1 and 2
            } else if (checkNextTokenType(TokenType.MINUS)) {
                Token minusToken = eat(TokenType.MINUS);
                ASTNode rightTerm = parseTerm();
                term = new BinaryOperatorNode(minusToken.value(), term, rightTerm); // case 1 and 2
            } else {
                return term; // case 3
            }

        }
    }

    /*
     * Variable
     * : variable literal = 
     */
    private ASTNode parseVariableAssignment() {
        eat(TokenType.ASSIGNMENT);
        Token variableToken = eat(TokenType.VARIABLE);
        String name = variableToken.value();
        if (checkNextTokenType(TokenType.EQUALS)) {
            eat(TokenType.EQUALS);
            return new VariableNode(name, parseExpression().evaluate());
        } else {
            throw new SyntaxException("Assignments must be of the form: ver <var_name> = <expression>");
        }
    }

    /*
     * Term
     * : Factor {<InfixBinaryOperator> Factor} 
     * | Factor {<PostfixOperator>} 
     * | Factor
     */
    private ASTNode parseTerm() {
        
        ASTNode factor = parseFactor();

        while (true) {

            if (checkNextTokenType(TokenType.INFIX_OPERATOR)) {
                Token binaryOperatorToken = eat(TokenType.INFIX_OPERATOR);
                ASTNode rightFactor = parseFactor();
                factor = new BinaryOperatorNode(binaryOperatorToken.value(), factor, rightFactor); // case 1
            } else if (checkNextTokenType(TokenType.POSTFIX_OPERATOR)) {
                Token postfixOperatorToken = eat(TokenType.POSTFIX_OPERATOR);
                factor = new UnaryOperatorNode(postfixOperatorToken.value(), factor); // case 2
            } else {
                return factor; // case 3
            }
        }
    }

    /*
     * Factor 
     * : <PrefixOperator> Factor 
     * | Base {^ Exponent} 
     * | Base
     */
    private ASTNode parseFactor() {

        if (checkNextTokenType(TokenType.PREFIX_OPERATOR)) {
            Token prefixOperatorToken = eat(TokenType.PREFIX_OPERATOR);
            ASTNode factor = parseFactor();
            return new UnaryOperatorNode(prefixOperatorToken.value(), factor); // case 1
        }

        // MINUS is also a prefix operator
        if (checkNextTokenType(TokenType.MINUS)) {
            Token prefixOperatorToken = eat(TokenType.MINUS);
            ASTNode factor = parseFactor();
            return new UnaryOperatorNode(prefixOperatorToken.value(), factor); // case 1
        }
        
        ASTNode base = parseBase();

        while (true) {
            if (checkNextTokenType(TokenType.EXPONENT)) {
                Token exponentToken = eat(TokenType.EXPONENT);
                ASTNode exponent = parseExponent();
                base = new BinaryOperatorNode(exponentToken.value(), base, exponent); // case 2
            } else {
                return base; // case 3
            }
        }

    }

    /*
     * Exponent
     * : (Expression) 
     * | <Number>
     */
    private ASTNode parseExponent() {

        if (checkNextTokenType(TokenType.OPEN_BRACKET)) {
            
            eat(TokenType.OPEN_BRACKET);

            ASTNode expression = parseExpression();

            if (!checkNextTokenType(TokenType.CLOSE_BRACKET)) {
                throw new SyntaxException("Missing token: )");
            }
            
            eat(TokenType.CLOSE_BRACKET);
            return expression; // case 1

        }

        return parseNumber(); // case 2   
    }

    /*
     * Exponent
     * : (Expression) 
     * | <Number> 
     * | <Constant>
     */
    private ASTNode parseBase() {
        return parseExponent();
    }

    /*
     * Number
     * : number literal
     * : variable
     * | Constant i.e. pi or e
     */
    private ASTNode parseNumber() {

        if (checkNextTokenType(TokenType.NUMBER)) {
            Token numberToken = eat(TokenType.NUMBER);
            return new NumberNode(numberToken.value());
        }

        if (checkNextTokenType(TokenType.CONSTANT)) {
            Token constantToken = eat(TokenType.CONSTANT);
            return new NumberNode(constantToken.value());
        }

        if (checkNextTokenType(TokenType.VARIABLE)) {
            Token varToken = eat(TokenType.VARIABLE);
            return new VariableNode(varToken.value());
        }

        throw new SyntaxException("Expected a number like 5 or 1.2 or a constant like pi or e.");
    }

    private Token eat(TokenType tokenType) {

        if (nextToken == null) {
            throw new SyntaxException("Unexpected end of input, expected: " + tokenType.name());
        }

        if (nextToken.tokenType() != tokenType) {
            throw new SyntaxException("Unexpected Token at index " + lexer.getCursor() + ": " + nextToken.tokenType().name() + ", expected: " + tokenType.name());
        }

        Token token = nextToken;
        nextToken = lexer.getNextToken();
        return token;

    }

    private boolean checkNextTokenType(TokenType tokenType) {
        return nextToken != null && nextToken.tokenType() == tokenType;
    }

}
