package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;

import static nl.han.ica.icss.ast.types.ExpressionType.*;

import nl.han.ica.icss.ast.types.ExpressionType;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;


public class Checker {

    private HANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        variableTypes = new HANLinkedList<>();

        //Finds instances of VariableAssignment and populates HashMap
        setVariableAssignment(ast.root);

        //Checks validity operations
        checkValidityDeclarations(ast.root);

    }



    private void checkValidityDeclarations(ASTNode root) {
        //Gets all ASTNode with instance of Declaration
        ArrayList<Declaration> declarations = getAllDeclarations(root);

        //Removes all declarations from ArrayList containing either a Color or Boolean and sets error on concerned node
        declarations.removeIf(node -> checkValidityLiteralsInExpression((Operation) node.expression));


    }

    //Returns ArrayList containing all Declaration nodes within AST
    private ArrayList<Declaration> getAllDeclarations(ASTNode node) {
        ArrayList<Declaration> temp = new ArrayList<>();
        if(node instanceof Declaration) {
            temp.add((Declaration) node);
        } else {
            for(ASTNode child : node.getChildren()) {
                var temp2 = getAllDeclarations(child);
                if(!temp2.isEmpty()) {
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
        } else {
            ExpressionType exTypeL = getExpressionType(lhs);
            ExpressionType exTypeR = getExpressionType(rhs);
            if (exTypeL == BOOL || exTypeL == COLOR) {
                lhs.setError("Colors and/or Boolean DataTypes are not allowed in Expressions.");
                return false;
            }
            if (exTypeR == BOOL || exTypeR == COLOR) {
                rhs.setError("Colors and/or Boolean DataTypes are not allowed in Expressions.");
                return false;
            }
        }
        return true;
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




    //Supporting functions
    /*--------------------------------------------------------*/

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

}
