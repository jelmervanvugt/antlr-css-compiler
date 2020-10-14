package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.*;

public class Checker {

    //IDE's deadlock warning will never occur due to the single thread nature of the application
    protected static ScopeManager scopeManager = new ScopeManager();
    private static StylesheetChecker stylesheetChecker = new StylesheetChecker();
    protected static StyleruleChecker styleruleChecker = new StyleruleChecker();
    protected static ExpressionChecker expressionChecker = new ExpressionChecker();
    protected static IfClauseChecker ifClauseChecker = new IfClauseChecker();
    protected static ElseClauseChecker elseClauseChecker = new ElseClauseChecker();
    protected static ExpressionTypeChecker expressionTypeChecker = new ExpressionTypeChecker();

    public void check(AST ast) {
        stylesheetChecker.checkStylesheet(ast.root);
    }
}
