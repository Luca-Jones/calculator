package ast_nodes;

public class NumberNode implements ASTNode {

    private Number value;

    public NumberNode(Number value) {
        this.value = value;
    }

    public NumberNode(String value) throws NumberFormatException {
        
        this.value = switch (value) {
            case "e" -> Math.E;
            case "pi" -> Math.PI;
            case "ans" -> VariableNode.variables.get("ans");
            default -> Double.parseDouble(value);
        };

    }

    @Override
    public Number evaluate() {
        return value;
    }

    public static DECIMAL_MODE decimalMode = DECIMAL_MODE.REG;

    public static String formatNumber(Number number) {
        return switch(decimalMode) {
            case DECIMAL_MODE.REG -> number.toString();
            case DECIMAL_MODE.SCI -> String.format("%6.3f e", number.doubleValue());
            case DECIMAL_MODE.ENG -> engNotation(number);
            default -> number.toString();
        };
    }

    public enum DECIMAL_MODE {
        REG, SCI, ENG;
    }

    private static String engNotation(Number number) {
        if (number.doubleValue() == 0.0) {
            return "0.000 E0";
        }
        int exponent = (int)(Math.floor(Math.log10(Math.abs(number.doubleValue())) / 3) * 3);
        double mantissa = number.doubleValue() / Math.pow(10, exponent);
        return String.format("%.3f E%+03d", mantissa, exponent);
    }

}
