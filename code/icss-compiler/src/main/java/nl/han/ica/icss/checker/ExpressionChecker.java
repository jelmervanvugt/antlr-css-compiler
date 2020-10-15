package nl.han.ica.icss.checker;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import static nl.han.ica.icss.ast.types.ExpressionType.*;
import static nl.han.ica.icss.ast.types.ExpressionType.SCALAR;

public class ExpressionChecker extends Checker {

    //Checks validity of VariableAssignments and Declarations
    public void checkValidityExpression(ASTNode astNode) {

        String varName;
        Expression expression;
        ExpressionType exType;

        //Checks if Declaration or VariableAssignment has a nested expression. If FALSE these do not need to be checked.
        if (astNode instanceof VariableAssignment) {
            varName = ((VariableAssignment) astNode).name.name;
            if (((VariableAssignment) astNode).expression instanceof Literal) {
                ExpressionType expressionTypeReference = expressionTypeChecker.getExpressionType(((VariableAssignment) astNode).expression);
                if(expressionTypeReference != null) {
                    scopeManager.addVariable(varName, expressionTypeReference);
                    return;
                }
                astNode.setError("Variable is not declared or yet undefined within scope.");
                return;
            } else if (((VariableAssignment) astNode).expression instanceof VariableReference) {
                scopeManager.addVariable(varName, expressionTypeChecker.getExpressionType(((VariableAssignment) astNode).expression));
                return;
            }
            expression = ((VariableAssignment) astNode).expression;
        } else {
            varName = ((Declaration) astNode).property.name;
            if (((Declaration) astNode).expression instanceof VariableReference) {
                ExpressionType expressionTypeReference = expressionTypeChecker.getExpressionType(((Declaration) astNode).expression);
                if(expressionTypeReference != null) return;
                astNode.setError("Variable is not declared or yet undefined within scope.");
                return;
            } else if (((Declaration) astNode).expression instanceof Literal) {
                return;
            }
            expression = ((Declaration) astNode).expression;
        }

        //If Expression contains a Boolean of Color an error is set on concerned node
        if (checkColorBoolInExpression((Operation) expression)) {
            return;
        }

        //Checks if Expression contains an operand which will result in a syntax error
        //If Expression is valid returns resulting ExpressionType
        exType = checkFaultyOperandInExpression((Operation) expression);

        //Declares variable in HashMap if Expression is valid
        if (exType != null) scopeManager.addVariable(varName, exType);
    }

    private Operation getOperand(Operation astNode) {
        if (astNode instanceof AddOperation) {
            return new AddOperation();
        } else if (astNode instanceof SubtractOperation) {
            return new SubtractOperation();
        } else if (astNode instanceof MultiplyOperation) {
            return new MultiplyOperation();
        } else {
            //foutafhandeling??
            return null;
        }
    }

    //Returns true if Expression contains Color or Boolean
    private boolean checkColorBoolInExpression(Operation node) {
        Expression lhs = node.lhs;
        Expression rhs = node.rhs;
        if (node.lhs instanceof Operation) {
            return checkColorBoolInExpression((Operation) node.lhs);
        }
        if (node.rhs instanceof Operation) {
            return checkColorBoolInExpression((Operation) node.rhs);
        }
        ExpressionType exTypeL = expressionTypeChecker.getExpressionType(lhs);
        if (exTypeL == BOOL || exTypeL == COLOR) {
            lhs.setError("Colors and/or Boolean DataTypes are not allowed in Expressions.");
            return true;
        }

        ExpressionType exTypeR = expressionTypeChecker.getExpressionType(rhs);
        if (exTypeR == BOOL || exTypeR == COLOR) {
            rhs.setError("Colors and/or Boolean DataTypes are not allowed in Expressions.");
            return true;
        }
        return false;
    }

    //Implements logic when comparing outcome of Operation
    private ExpressionType checkFaultyOperandInExpression(Operation astNode) {

        ExpressionType exTypeL;
        ExpressionType exTypeR;

        if (astNode.lhs instanceof Operation) {
            exTypeL = checkFaultyOperandInExpression((Operation) astNode.lhs);
        } else {
            exTypeL = expressionTypeChecker.getExpressionType(astNode.lhs);
        }

        if (astNode.rhs instanceof Operation) {
            exTypeR = checkFaultyOperandInExpression((Operation) astNode.rhs);
        } else {
            exTypeR = expressionTypeChecker.getExpressionType(astNode.rhs);
        }

        if (exTypeL == null || exTypeL == UNDEFINED) {
            astNode.setError("Variable is not declared or yet undefined within scope.");
        } else if (exTypeR == null || exTypeR == UNDEFINED) {
            astNode.setError("Variable is not declared or yet undefined within scope.");
        } else {

            Operation operand = getOperand(astNode);

            if (operand instanceof AddOperation || operand instanceof SubtractOperation) {
                if (exTypeL.compareTo(exTypeR) != 0) {
                    astNode.setError("Values must be of compatible types.");
                    return null;
                } else {
                    return exTypeL;
                }
            } else if (operand instanceof MultiplyOperation) {
                if (exTypeL == SCALAR || exTypeR == SCALAR) {
                    if (exTypeL == SCALAR) return exTypeR;
                    return exTypeL;
                } else {
                    astNode.setError("Multiply operations must have a minimun of one scalar value.");
                }
            } else {
                //foutafhandeling??
            }
        }
        return null;
    }

}
