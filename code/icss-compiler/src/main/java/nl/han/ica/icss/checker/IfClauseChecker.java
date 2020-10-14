package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.IfClause;
import nl.han.ica.icss.ast.VariableAssignment;

import static nl.han.ica.icss.ast.types.ExpressionType.BOOL;

public class IfClauseChecker extends Checker {

    public void checkIfClause(IfClause ifClause) {
        scopeManager.enterScope();
        ifClause.getChildren().forEach(astNode -> {
            if (astNode instanceof Declaration | astNode instanceof VariableAssignment) {
                expressionChecker.checkValidityExpression(astNode);
            } else if (astNode instanceof IfClause) {
                checkIfClause((IfClause) astNode);
            }
        });
        if (ifClause.elseClause != null) elseClauseChecker.checkElseClause(ifClause.elseClause);
        if (expressionTypeChecker.getExpressionType(ifClause.conditionalExpression) != BOOL)
            ifClause.setError("IfClause must have a conditional expression of type Boolean.");
        scopeManager.exitScope();
    }

}
