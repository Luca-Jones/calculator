package ast_nodes;

import java.util.function.UnaryOperator;

public class UnaryOperatorNode implements ASTNode {

    public static ANGLE_MODE angleMode = ANGLE_MODE.RAD;

    private String opToken;
    private UnaryOperator<Number> operator;
    private ASTNode operand;

    public UnaryOperatorNode(String opToken, ASTNode operand) {

        if (operand == null) {
            throw new IllegalArgumentException();
        }

        this.opToken = opToken;

        switch (opToken) {
            case "-":       this.operator = x -> -x.doubleValue();                                      break;
            case "sqrt":    this.operator = x -> Math.sqrt(x.doubleValue());                            break;
            case "~":       this.operator = x -> ~x.intValue();                                         break;
            case "!":       this.operator = x -> factorial(x.longValue());                              break;
            case "sin":     this.operator = x -> Math.sin(x.doubleValue() * angleMode.conversion);      break;
            case "cos":     this.operator = x -> Math.cos(x.doubleValue() * angleMode.conversion);      break;
            case "tan":     this.operator = x -> Math.tan(x.doubleValue() * angleMode.conversion);      break;
            default:
                throw new IllegalArgumentException("Unary operator " + opToken + " not supported.");
        }

        this.operand = operand;
    }

    @Override
    public Number evaluate() {
        return operator.apply(operand.evaluate());
    }

    private static long factorial(long n) {
        if (n < 0) throw new IllegalArgumentException();
        if (n == 0) return 1;
        if (n == 1) return 1;
        return n * factorial(n-1);
    }

    @Override
    public String toString() {
        return opToken + "\n\tOperand: " + operand;
    }

    public enum ANGLE_MODE {
        RAD(1), DEG(Math.PI / 180), GRAD(Math.PI / 200);
        public final double conversion;
        private ANGLE_MODE(double conversion) {
            this.conversion = conversion;
        }
    }

}
