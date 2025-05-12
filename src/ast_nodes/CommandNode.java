package ast_nodes;

import ast_nodes.NumberNode.DECIMAL_MODE;
import ast_nodes.UnaryOperatorNode.ANGLE_MODE;

public class CommandNode implements ASTNode {

    public CommandNode(String command) {
        switch (command) {
            case "rad":
                UnaryOperatorNode.angleMode = ANGLE_MODE.RAD;
                break;
            case "deg":
                UnaryOperatorNode.angleMode = ANGLE_MODE.DEG;
                break;
            case "grad":
                UnaryOperatorNode.angleMode = ANGLE_MODE.GRAD;
                break;
            case "reg":
                NumberNode.decimalMode = DECIMAL_MODE.REG;
                break;
            case "sci":
                NumberNode.decimalMode = DECIMAL_MODE.SCI;
                break;
            case "eng":
                NumberNode.decimalMode = DECIMAL_MODE.ENG;
                break;
            case "cls":
            case "clear":
                System.out.print("\033[H\033[2J");
                System.out.flush();
                break;
            case "exit":
                System.exit(0);
            default:
                break;
        }
    }

    @Override
    public Number evaluate() {
        return 0;
    }
    
}
