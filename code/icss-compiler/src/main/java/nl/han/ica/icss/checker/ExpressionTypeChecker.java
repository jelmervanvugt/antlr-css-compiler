package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Literal;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.types.ExpressionType;

import static nl.han.ica.icss.ast.types.ExpressionType.*;
import static nl.han.ica.icss.ast.types.ExpressionType.UNDEFINED;

public class ExpressionTypeChecker extends Checker {

    //Checks instance of Expression and returns ExpressionType
    public ExpressionType getExpressionType(Expression expression) {
        if (expression instanceof Literal) {
            return getExpressionTypeFromLiteral(expression);
        } else if (expression instanceof VariableReference) {
            return scopeManager.getVariable(((VariableReference) expression).name);
        } else {
            // errorhandling
            return null;
        }
    }

    //Returns ExpressionType based on Literal
    private ExpressionType getExpressionTypeFromLiteral(Expression expression) {
        if (expression instanceof BoolLiteral) {
            return BOOL;
        } else if (expression instanceof ColorLiteral) {
            return COLOR;
        } else if (expression instanceof PercentageLiteral) {
            return PERCENTAGE;
        } else if (expression instanceof PixelLiteral) {
            return PIXEL;
        } else if (expression instanceof ScalarLiteral) {
            return SCALAR;
        } else {
            return UNDEFINED;
        }
    }
}
