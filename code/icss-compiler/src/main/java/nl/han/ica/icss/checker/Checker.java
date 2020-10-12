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

        for (ASTNode node : ast.root.getChildren()) {
            setVariableAssignment(node);
        }
    }

    //Function which finds instances of VariableAssignment
    private void setVariableAssignment(ASTNode node) {
        if (node instanceof VariableAssignment) {
            String propertyName = ((VariableAssignment) node).name.name;
            ExpressionType expressionType = getExpressionType(((VariableAssignment) node).expression);
            HashMap<String, ExpressionType> hashMap = new HashMap<>();
            hashMap.put(propertyName, expressionType);
            variableTypes.addLast(hashMap);
        }
    }

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










//    private void checkStylesheet(Stylesheet sheet) {
//        for (ASTNode child : sheet.getChildren()) {
//            if (child instanceof VariableAssignment) {
//                checkVariableAssignment((VariableAssignment) child);
//            } else {
//                checkStyleRule((Stylerule) child);
//            }
//        }
//    }
//
//
//    //Check children of stylesheet
//    private void checkStyleRule(Stylerule styleRule) {
//        for (ASTNode child : styleRule.getChildren()) {
//            if (child instanceof Declaration) {
//                checkDeclaration((Declaration) child);
//            } else if (child instanceof IfClause) {
//                checkIfClause((IfClause) child);
//            }
//        }
//    }
//
//    private void checkVariableAssignment(VariableAssignment variableAssignment) {
//            //Checking if third child is an expression
//            for(ASTNode node : variableAssignment.getChildren()) {
//                if(node.)
//            }
//    }
//
//    //Check children of stylerule
//    private void checkDeclaration(Declaration declaration) {
//
//    }
//
//    private void checkIfClause(IfClause ifClause) {
//
//    }
//
//
//
//
//
//
//    //Iterates through variableTypes LinkedList and returns ExpressionType of variable
//    private ExpressionType getVariableValue(VariableReference variableReference) {
//        for (HashMap<String, ExpressionType> map : variableTypes) {
//            if (map.containsKey(variableReference.name)) {
//                return map.get(variableReference.name);
//            }
//        }
//        return null;
//    }
}
