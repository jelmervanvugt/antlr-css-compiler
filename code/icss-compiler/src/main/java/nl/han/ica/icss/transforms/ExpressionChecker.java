package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.checker.ScopeManager;

public class ExpressionChecker {

    ScopeManager<Literal> scopeManager;

    public void check(ASTNode astNode, ScopeManager<Literal> scopeManager) {

        this.scopeManager = scopeManager;

        String varName;
        Literal outcome;

        //Checks if Declaration or VariableAssignment has a nested expression. If FALSE these do not need to be checked.
        if (astNode instanceof VariableAssignment) {
            varName = ((VariableAssignment) astNode).name.name;
            if (((VariableAssignment) astNode).expression instanceof Literal) {
                outcome = (Literal) ((VariableAssignment) astNode).expression;
                scopeManager.addVariable(varName, outcome);
                return;
            } else if (((VariableAssignment) astNode).expression instanceof VariableReference) {
                outcome = scopeManager.getVariable(((VariableAssignment) astNode).name.name);
                scopeManager.addVariable(varName, outcome);
                return;
            }
            outcome = getOutcomeOfOperation((Operation) ((VariableAssignment) astNode).expression);
        } else {
            varName = ((Declaration) astNode).property.name;
            if (((Declaration) astNode).expression instanceof VariableReference) {
                outcome = scopeManager.getVariable(((VariableReference) ((Declaration) astNode).expression).name);
                return;
            } else if (((Declaration) astNode).expression instanceof Literal) {
                outcome = (Literal) ((Declaration) astNode).expression;
                return;
            }
            outcome = getOutcomeOfOperation((Operation) ((Declaration) astNode).expression);
        }

        //Replace Expression with Literal
        if (astNode instanceof Declaration)
            ((Declaration) astNode).expression = outcome;
        else
            ((VariableAssignment) astNode).expression = outcome;
    }

    //Traverses Expression and calculates outcome
    private Literal getOutcomeOfOperation(Operation astNode) {

        Literal literalLeft;
        Literal literalRight;

        if (astNode.lhs instanceof Operation) {
            literalLeft = getOutcomeOfOperation((Operation) astNode.lhs);
        } else if (astNode.lhs instanceof VariableReference) {
            literalLeft = scopeManager.getVariable(((VariableReference) astNode.lhs).name);
        } else {
            literalLeft = (Literal) astNode.lhs;
        }

        if (astNode.rhs instanceof Operation) {
            literalRight = getOutcomeOfOperation((Operation) astNode.rhs);
        } else if (astNode.lhs instanceof VariableReference) {
            literalRight = scopeManager.getVariable(((VariableReference) astNode.lhs).name);
        } else {
            literalRight = (Literal) astNode.rhs;
        }

        return calculateOutcome(literalLeft, literalRight, astNode);

    }

    //Tja, wat kan ik hierover zeggen...
    private Literal calculateOutcome(Literal lhs, Literal rhs, Operation operand) {
        if (operand instanceof AddOperation) {
            if (lhs instanceof PercentageLiteral || rhs instanceof PercentageLiteral)
                return new PercentageLiteral(lhs.getValue() + rhs.getValue());
            if (lhs instanceof PixelLiteral || rhs instanceof PixelLiteral)
                return new PixelLiteral(lhs.getValue() + rhs.getValue());
            else
                return new ScalarLiteral(lhs.getValue() + rhs.getValue());
        } else if (operand instanceof SubtractOperation) {
            if (lhs instanceof PercentageLiteral || rhs instanceof PercentageLiteral)
                return new PercentageLiteral(lhs.getValue() - rhs.getValue());
            if (lhs instanceof PixelLiteral || rhs instanceof PixelLiteral)
                return new PixelLiteral(lhs.getValue() - rhs.getValue());
            else
                return new ScalarLiteral(lhs.getValue() - rhs.getValue());
        } else {
            if (lhs instanceof PercentageLiteral || rhs instanceof PercentageLiteral)
                return new PercentageLiteral(lhs.getValue() * rhs.getValue());
            if (lhs instanceof PixelLiteral || rhs instanceof PixelLiteral)
                return new PixelLiteral(lhs.getValue() * rhs.getValue());
            else
                return new ScalarLiteral(lhs.getValue() * rhs.getValue());
        }
    }

}
