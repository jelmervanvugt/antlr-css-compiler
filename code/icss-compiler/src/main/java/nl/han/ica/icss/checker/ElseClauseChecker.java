package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.ElseClause;
import nl.han.ica.icss.ast.IfClause;

public class ElseClauseChecker extends Checker {

    public void checkElseClause(ElseClause elseClause) {
        scopeManager.enterScope();
        elseClause.getChildren().forEach(astNode -> {
            if (astNode instanceof Declaration) {
                expressionChecker.checkValidityExpression(astNode);
            } else if (astNode instanceof IfClause) {
                ifClauseChecker.checkIfClause((IfClause) astNode);
            }
        });
        scopeManager.exitScope();
    }

}
