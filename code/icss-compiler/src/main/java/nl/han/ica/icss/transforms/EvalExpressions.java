package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.checker.ScopeManager;

import java.util.HashMap;

public class EvalExpressions implements Transform {

    protected IHANLinkedList<HashMap<String, Literal>> variableValues;
    private ExpressionChecker expressionChecker;
    protected ScopeManager<Literal> scopeManager;

    public EvalExpressions() {
        variableValues = new HANLinkedList<>();
        scopeManager = new ScopeManager<>();
    }

    @Override
    public void apply(AST ast) {
        expressionChecker = new ExpressionChecker();
        evalStylesheet(ast.root);
    }

    //Checks for Stylerules and VariableAssignments
    private void evalStylesheet(Stylesheet stylesheet) {
        scopeManager.enterScope();
        stylesheet.getChildren().forEach(astNode -> {
            if (astNode instanceof VariableAssignment) expressionChecker.check(astNode);
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
            else if(astNode instanceof VariableAssignment | astNode instanceof Declaration) expressionChecker.check(astNode);
        });
        scopeManager.exitScope();
    }

    private void evalIfClause(IfClause ifClause) {
        scopeManager.enterScope();
        ifClause.getChildren().forEach(astNode -> {
            if(astNode instanceof IfClause) evalIfClause((IfClause) astNode);
            else if(astNode instanceof ElseClause) evalElseClause((ElseClause) astNode);
            else expressionChecker.check(astNode);
        });
        scopeManager.exitScope();
    }

    private void evalElseClause(ElseClause elseClause) {
        scopeManager.enterScope();
        elseClause.getChildren().forEach(astNode -> {
            if(astNode instanceof IfClause) evalIfClause((IfClause) astNode);
            else if(astNode instanceof ElseClause) evalElseClause((ElseClause) astNode);
            else if(astNode instanceof VariableAssignment | astNode instanceof Declaration) expressionChecker.check(astNode);
        });
        scopeManager.exitScope();
    }

}
