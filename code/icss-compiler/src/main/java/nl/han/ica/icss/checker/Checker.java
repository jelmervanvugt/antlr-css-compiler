package nl.han.ica.icss.checker;

import com.google.errorprone.annotations.Var;
import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;

import static nl.han.ica.icss.ast.types.ExpressionType.*;

import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.ArrayList;
import java.util.HashMap;


public class Checker {

    private HANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        variableTypes = new HANLinkedList<>();

        //Gets all ASTNodes which declare a variable. In this case it'll be Declaration and VariableAssignment
        ArrayList<ASTNode> variablesDeclarations = getAllDeclarations(ast.root);

        //Removes all nodes from ArrayList containing either a Color or Boolean and sets error on concerned node
        variablesDeclarations.removeIf(node ->
        {
            if(node instanceof Declaration) {
                if (((Declaration) node).expression instanceof Operation) {
                    return !checkValidityLiteralsInExpression((Operation) ((Declaration) node).expression);
                }
            } else if (node instanceof VariableAssignment) {
                if (((VariableAssignment) node).expression instanceof VariableReference) {
                    return !checkValidityLiteralsInExpression((Operation) ((VariableAssignment) node).expression);
                }
            }
            return true;
        });

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
        } else {
            ExpressionType exTypeL = getExpressionType(lhs);
            if (exTypeL == BOOL || exTypeL == COLOR) {
                lhs.setError("Colors and/or Boolean DataTypes are not allowed in Expressions.");
                return false;
            }
        }
        if (node.rhs instanceof Operation) {
            checkValidityLiteralsInExpression((Operation) node.rhs);
        } else {
            ExpressionType exTypeR = getExpressionType(rhs);
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

}
