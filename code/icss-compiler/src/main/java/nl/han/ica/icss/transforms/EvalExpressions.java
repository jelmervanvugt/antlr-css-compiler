package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.checker.ScopeManager;


public class EvalExpressions implements Transform {

    private ExpressionChecker expressionChecker;
    private ScopeManager<Literal> scopeManager;

    @Override
    public void apply(AST ast) {
        expressionChecker = new ExpressionChecker();
        scopeManager = new ScopeManager<>();
        evalStylesheet(ast.root);
    }

    //Checks for Stylerules and VariableAssignments
    private void evalStylesheet(Stylesheet stylesheet) {
        scopeManager.enterScope();
        stylesheet.getChildren().forEach(astNode -> {
            if (astNode instanceof VariableAssignment) expressionChecker.check(astNode, scopeManager);
            else evalStylerule((Stylerule) astNode);
        });
        scopeManager.exitScope();
    }

    //Stylerules and If- and ElseClauses contain only VariableAssignments, Declarations and other If- and ElseClauses
    //Everything can be ExpressionChecked directly except the If- ElseClauses.
    private void evalStylerule(Stylerule stylerule) {
        scopeManager.enterScope();
        stylerule.getChildren().forEach(astNode -> {
            if(astNode instanceof IfClause) evalIfClause((IfClause) astNode);
            else if(astNode instanceof ElseClause) evalElseClause((ElseClause) astNode);
            else if(astNode instanceof VariableAssignment | astNode instanceof Declaration) expressionChecker.check(astNode, scopeManager);
        });
        scopeManager.exitScope();
    }

    private void evalIfClause(IfClause ifClause) {
        scopeManager.enterScope();
        ifClause.getChildren().forEach(astNode -> {
            if(astNode instanceof IfClause) evalIfClause((IfClause) astNode);
            else if(astNode instanceof ElseClause) evalElseClause((ElseClause) astNode);
            else if(astNode instanceof VariableAssignment | astNode instanceof Declaration) expressionChecker.check(astNode, scopeManager);
        });
        scopeManager.exitScope();
    }

    private void evalElseClause(ElseClause elseClause) {
        scopeManager.enterScope();
        elseClause.getChildren().forEach(astNode -> {
            if(astNode instanceof IfClause) evalIfClause((IfClause) astNode);
            else if(astNode instanceof ElseClause) evalElseClause((ElseClause) astNode);
            else if(astNode instanceof VariableAssignment | astNode instanceof Declaration) expressionChecker.check(astNode, scopeManager);
        });
        scopeManager.exitScope();
    }

}
