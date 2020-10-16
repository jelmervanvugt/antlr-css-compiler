package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.selectors.TagSelector;

import java.util.ArrayList;

public class RemoveIf implements Transform {

    @Override
    public void apply(AST ast) {
        transformStylesheet(ast.root);
    }

    private void transformStylesheet(Stylesheet stylesheet) {
        stylesheet.getChildren().forEach(astNode -> {
            if (astNode instanceof Stylerule) {
                ((Stylerule) astNode).body = transformBlock(astNode);
            }
        });
    }

    private ArrayList<ASTNode> transformBlock(ASTNode block) {
        ArrayList<ASTNode> newBody = new ArrayList<>();
        block.getChildren().forEach(astNode -> {
            if(astNode instanceof IfClause) {
                newBody.addAll(transformIfClause((IfClause) astNode));
            } else if(!(astNode instanceof VariableReference) && !(astNode instanceof TagSelector) && !(astNode instanceof Literal)) {
                newBody.add(astNode);
            }
        });
        return newBody;
    }

    private ArrayList<ASTNode> transformIfClause(IfClause node) {
        if(((BoolLiteral) node.conditionalExpression).value) {
            node.removeChild(node.conditionalExpression);
            return transformBlock(node);
        } else {
            if(node.elseClause != null) {
                node.removeChild(node.conditionalExpression);
                return transformBlock(node.elseClause);
            }
        }
            return new ArrayList<ASTNode>();
    }

}
