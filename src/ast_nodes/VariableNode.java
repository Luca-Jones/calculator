package ast_nodes;

import java.util.HashMap;
import java.util.Map;

import parser.SyntaxException;

public class VariableNode implements ASTNode {

    public static final Map<String, Number> variables = new HashMap<>(Map.of("ans", 0));

    private Number value;

    /**
     * Assigns the variable name to a value.
     * @param name The name of the variable.
     * @param value The value this variable holds.
     */
    public VariableNode(String name, Number value) {
        this.value = value;
        variables.put(name, value);
    }

    /**
     * Retrieves the value of this variable.
     * @param name The name of the variable.
     * @throws SyntaxException if the variable has not been defined.
     */
    public VariableNode(String name) {
        if (variables.containsKey(name)) {
            this.value = variables.get(name);
        } else {
            throw new SyntaxException("The variable " + name + " is undefined.");
        }
    }

    @Override
    public Number evaluate() {
        return value;
    }
    
}
