package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.IfClause;
import nl.han.ica.icss.ast.Stylerule;
import nl.han.ica.icss.ast.VariableAssignment;

public class StyleruleChecker extends Checker {

    public void checkStylerule(Stylerule stylerule) {
        scopeManager.enterScope();
        stylerule.getChildren().forEach(astNode -> {
            if (astNode instanceof Declaration | astNode instanceof VariableAssignment) {
                expressionChecker.checkValidityExpression(astNode);
            } else if (astNode instanceof IfClause) {
                ifClauseChecker.checkIfClause((IfClause) astNode);
            }
        });
        scopeManager.exitScope();
    }

}
