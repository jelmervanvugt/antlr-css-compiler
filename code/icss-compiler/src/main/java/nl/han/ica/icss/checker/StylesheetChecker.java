package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Stylerule;
import nl.han.ica.icss.ast.VariableAssignment;

public class StylesheetChecker extends Checker {

    public void checkStylesheet(ASTNode stylesheet) {
        scopeManager.enterScope();
        //Global VariableAssignments might be used in Declarations, these need to be checked first
        stylesheet.getChildren().forEach(astNode -> {
            if (astNode instanceof VariableAssignment) {
                expressionChecker.checkValidityExpression(astNode);
            }
        });

        //Checking Stylerules
        stylesheet.getChildren().forEach(astNode -> {
            if (astNode instanceof Stylerule) {
                styleruleChecker.styleruleChecker.checkStylerule((Stylerule) astNode);
            }
        });
        scopeManager.exitScope();
    }
}
