package ast_nodes;

import java.util.function.BinaryOperator;

public class BinaryOperatorNode implements ASTNode {

    private String opToken;
    private BinaryOperator<Number> operator;
    private ASTNode left;
    private ASTNode right;

    public BinaryOperatorNode(String opToken, ASTNode left, ASTNode right) {

        if (left == null || right == null) {
            throw new IllegalArgumentException();
        }

        this.opToken = opToken;

        switch(opToken) {
            case "+":   this.operator = (x,y) -> x.doubleValue() + y.doubleValue();                 break;
            case "-":   this.operator = (x,y) -> x.doubleValue() - y.doubleValue();                 break;
            case "*":   this.operator = (x,y) -> x.doubleValue() * y.doubleValue();                 break;
            case "/":   this.operator = (x,y) -> x.doubleValue() / y.doubleValue();                 break;
            case "^":   this.operator = (x,y) -> Math.pow(x.doubleValue(), y.doubleValue());        break;
            case "<<":  this.operator = (x,y) -> x.intValue() << y.intValue();                      break;
            case ">>":  this.operator = (x,y) -> x.intValue() >> y.intValue();                      break;
            case "&":   this.operator = (x,y) -> x.intValue() & y.intValue();                       break;
            case "|":   this.operator = (x,y) -> x.intValue() | y.intValue();                       break;
            case "%":   this.operator = (x,y) -> x.doubleValue() % y.doubleValue();                 break;
            case "E":   this.operator = (x,y) -> x.doubleValue() * Math.pow(10, y.doubleValue()); break;
            default:
                throw new IllegalArgumentException("Binary operator " + opToken + " not supported.");
        }

        this.left = left;
        this.right = right;
    }

    @Override
    public Number evaluate() {
        return operator.apply(left.evaluate(), right.evaluate());
    }
    
    @Override
    public String toString() {
        return opToken + "\n\tLeft: " + left + "\n\tRight: " + right;
    }
    
}
