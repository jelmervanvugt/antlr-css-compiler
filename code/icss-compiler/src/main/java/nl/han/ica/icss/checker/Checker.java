package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;

import static nl.han.ica.icss.ast.types.ExpressionType.*;

import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;


public class Checker {

    private HANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        variableTypes = new HANLinkedList<>();

        //Finds instances of VariableAssignment and populates HashMap
        setVariableAssignment(ast.root);

        //Checks validity operations
        checkValidityOperations(ast.root);

    }

    private void setVariableAssignment(ASTNode root) {
        for (ASTNode node : root.getChildren()) {
            if (node instanceof VariableAssignment) {
                String propertyName = ((VariableAssignment) node).name.name;
                ExpressionType expressionType = getExpressionType(((VariableAssignment) node).expression);
                HashMap<String, ExpressionType> hashMap = new HashMap<>();
                hashMap.put(propertyName, expressionType);
                variableTypes.addLast(hashMap);
            }
        }
    }



    //Recursive functions which finds all ASTNodes which are Operations
    private void checkValidityOperations(ASTNode node) {
        if(node instanceof Operation) {
            checkIfExpressionTypesAreEqual((Operation) node);
        }
    }

    private void checkIfExpressionTypesAreEqual(Operation operation) {

        ExpressionType exTypeLeft = null;
        ExpressionType exTypeRight = null;

        if(operation.lhs instanceof VariableReference) {
            exTypeLeft = variableTypes.getFirst().get(((VariableReference) operation.lhs).name);
        } else {
            exTypeLeft = getExpressionType(operation.lhs);
        }

        if(operation.rhs instanceof VariableReference) {
            exTypeRight = variableTypes.getFirst().get(((VariableReference) operation.rhs).name);
        } else {
            exTypeRight = getExpressionType(operation.rhs);
        }


        //logica
        

    }


    //Supporting functions
    /*--------------------------------------------------------*/

    //Returns ExpressionType based on Literal
    private ExpressionType getExpressionType(Expression expression) {
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
}
