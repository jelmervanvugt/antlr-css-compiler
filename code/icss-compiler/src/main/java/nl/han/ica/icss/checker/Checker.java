package nl.han.ica.icss.checker;

import com.google.errorprone.annotations.Var;
import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;

import static nl.han.ica.icss.ast.types.ExpressionType.*;

import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;

public class Checker {

    private HANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        variableTypes = new HANLinkedList<>();
        checkStylesheet(ast.root);
    }

    private void checkStylesheet(ASTNode stylesheet) {
        //VariableAssignments might be used in Declarations, these need to be checked first
        stylesheet.getChildren().forEach(astNode -> {
            if (astNode instanceof VariableAssignment) {
                checkValidityExpression(astNode);
            }
        });
        stylesheet.getChildren().forEach(astNode -> {
            if (astNode instanceof Stylerule) {
                checkStylerule((Stylerule) astNode);
            }
        });
    }

    //Children of stylesheet
    private void checkStylerule(Stylerule stylerule) {

    }


    //Checks validity of VariableAssignments and Declarations
    private void checkValidityExpression(ASTNode astNode) {

        String varName;
        Expression expression;
        ExpressionType exType;

        //Below if statement checks if VariableAssignment or Declaration has a nested Expression

        if (astNode instanceof VariableAssignment) {
            varName = ((VariableAssignment) astNode).name.name;
            if(((VariableAssignment) astNode).expression instanceof Literal) {
                putVariableInHashMap(varName, getExpressionType(((VariableAssignment) astNode).expression));
                return;
            }
            expression = ((VariableAssignment) astNode).expression;
        } else {
            varName = ((Declaration) astNode).property.name;
            if(((Declaration) astNode).expression instanceof VariableReference) {
                putVariableInHashMap(varName, getExpressionTypeFromHashMap(((Declaration) astNode).expression));
                return;
            } else if(((Declaration) astNode).expression instanceof Literal) {
                putVariableInHashMap(varName, getExpressionType(((Declaration) astNode).expression));
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
        if (exType != null) putVariableInHashMap(varName, exType);
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
        ExpressionType exTypeL = getExpressionType(lhs);
        if (exTypeL == BOOL || exTypeL == COLOR) {
            lhs.setError("Colors and/or Boolean DataTypes are not allowed in Expressions.");
            return true;
        }

        ExpressionType exTypeR = getExpressionType(rhs);
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
            exTypeL = getExpressionType(astNode.lhs);
        }

        if (astNode.rhs instanceof Operation) {
            exTypeR = checkFaultyOperandInExpression((Operation) astNode.lhs);
        } else {
            exTypeR = getExpressionType(astNode.rhs);
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


    //Supporting functions
    /*----------------------------------------------------------------------------------------------------------------*/

    //Checks instance of Expression and returns ExpressionType
    private ExpressionType getExpressionType(Expression expression) {
        if (expression instanceof Literal) {
            return getExpressionTypeFromLiteral(expression);
        } else if (expression instanceof VariableReference) {
            return getExpressionTypeFromHashMap(expression);
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

    //Returns ExpressionType from VariableType Hashmap
    private ExpressionType getExpressionTypeFromHashMap(Expression expression) {
        if (expression instanceof VariableReference) {
            for (int i = 0; i < variableTypes.getSize(); i++) {
                var temp = variableTypes.get(i).get(((VariableReference) expression).name);
                if (temp != null) {
                    return temp;
                }
            }
        }
        return null;
    }

    //Returns operand type from Operation
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

    //Puts VariableAssignments in HashMap
    private void putVariableInHashMap(String name, ExpressionType expressionType) {
        HashMap<String, ExpressionType> hashMap = new HashMap<>();
        hashMap.put(name, expressionType);
        variableTypes.addLast(hashMap);
    }

    //Calculates outcome of Expression

}
