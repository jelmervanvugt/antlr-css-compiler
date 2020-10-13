package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;

import static nl.han.ica.icss.ast.types.ExpressionType.*;

import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
import java.util.HashMap;

/*

TO DO

-   extraheren van subklassen uit checker
-   if else clauses fixen
-   nadat expressions in variableassignments zijn gechecked moeten degene zonder error worden toegevoegd in hashmap

 */


public class Checker {

    private HANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        variableTypes = new HANLinkedList<>();

        //Gets all ASTNodes which declare a variable. In this case it'll be Declaration and VariableAssignment
        ArrayList<ASTNode> variableDeclarations = getAllDeclarations(ast.root);

        //Removes all nodes from ArrayList containing either a Color or Boolean and sets error on concerned node
        variableDeclarations = trimExpressionsFromDeclarations(variableDeclarations);

        //Checks the validity of the operands in remaining expressions
        checkValidityOperandsInExpressions(variableDeclarations);

    }

    //Checks if the combination of ExpressionTypes and an operand produce a valid result
    private void checkValidityOperandsInExpressions(ArrayList<ASTNode> astNodes) {
        //Making two separate arrays, one containing VariableAssignments and one Declarations
        ArrayList<VariableAssignment> variableAssignments = new ArrayList<>();
        ArrayList<Declaration> variableDeclarations = new ArrayList<>();

        astNodes.forEach(astNode -> {
            if (astNode instanceof VariableAssignment) {
                variableAssignments.add((VariableAssignment) astNode);
            } else {
                variableDeclarations.add((Declaration) astNode);
            }
            ;
        });


        //VariableAssignments might be used in Declarations, these need to be checked first
        variableAssignments.forEach(astNode -> checkValidityOperands((Operation) astNode.expression));
        //Putting VariableAssignments in HashMap if their expression is correct
        variableAssignments.forEach(astNode -> {
            if ((Operation) astNode.expression == null) {
                setVariableAssignmentInHashMap(astNode);
            }
        });
        //Checking Expressions contained by Declarations
        variableDeclarations.forEach(astNode -> checkValidityOperands((Operation) astNode.expression));

    }

    //Implements logic when comparing outcome of Operation
    private ExpressionType checkValidityOperands(Operation astNode) {

        ExpressionType exTypeL;
        ExpressionType exTypeR;

        if (astNode.lhs instanceof Operation) {
            exTypeL = checkValidityOperands((Operation) astNode.lhs);
        } else {
            exTypeL = getExpressionType(astNode.lhs);
        }

        if (astNode.rhs instanceof Operation) {
            exTypeR = checkValidityOperands((Operation) astNode.lhs);
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
                }
            } else if (operand instanceof MultiplyOperation) {
                if (exTypeL != SCALAR || exTypeR != SCALAR) {
                    astNode.setError("Multiply operations must have a minimun of one scalar value.");
                }
            } else {
                //foutafhandeling??
            }
        }
        return null;
    }

    //Calls function underneath
    private ArrayList<ASTNode> trimExpressionsFromDeclarations(ArrayList<ASTNode> variableDeclarations) {
        variableDeclarations.removeIf(node ->
        {
            if (node instanceof Declaration) {
                if (((Declaration) node).expression instanceof Operation) {
                    return !checkValidityLiteralsInExpression((Operation) ((Declaration) node).expression);
                }
            } else if (node instanceof VariableAssignment) {
                if (((VariableAssignment) node).expression instanceof Operation) {
                    return !checkValidityLiteralsInExpression((Operation) ((VariableAssignment) node).expression);
                }
            }
            return true;
        });
        return variableDeclarations;
    }


    //Returns ArrayList containing all Declaration nodes within AST
    private ArrayList<ASTNode> getAllDeclarations(ASTNode node) {
        ArrayList<ASTNode> temp = new ArrayList<>();
        if (node instanceof Declaration | node instanceof VariableAssignment) {
            temp.add((ASTNode) node);
        } else {
            for (ASTNode child : node.getChildren()) {
                var temp2 = getAllDeclarations(child);
                if (!temp2.isEmpty()) {
                    temp.addAll(temp2);
                }
            }
        }
        return temp;
    }


    //Returns true if Declaration contains Color or Boolean
    private boolean checkValidityLiteralsInExpression(Operation node) {
        Expression lhs = node.lhs;
        Expression rhs = node.rhs;
        if (node.lhs instanceof Operation) {
            checkValidityLiteralsInExpression((Operation) node.lhs);
        }
        if (node.rhs instanceof Operation) {
            checkValidityLiteralsInExpression((Operation) node.rhs);
        }
        ExpressionType exTypeL = getExpressionType(lhs);
        if (exTypeL == BOOL || exTypeL == COLOR) {
            lhs.setError("Colors and/or Boolean DataTypes are not allowed in Expressions.");
            return false;
        }

        ExpressionType exTypeR = getExpressionType(rhs);
        if (exTypeR == BOOL || exTypeR == COLOR) {
            rhs.setError("Colors and/or Boolean DataTypes are not allowed in Expressions.");
            return false;
        }
        return true;
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
    private void setVariableAssignmentInHashMap(VariableAssignment variableAssignment) {
        String propertyName = variableAssignment.name.name;

        // hier moet je dan weer de expressiontype gaan calculaten als het een geneste expressie is

//        ExpressionType expressionType =
//        HashMap<String, ExpressionType> hashMap = new HashMap<>();
//        hashMap.put(propertyName, expressionType);
//        variableTypes.addLast(hashMap);
    }

}
