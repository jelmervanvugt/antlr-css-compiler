package nl.han.ica.icss.parser;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import nl.han.ica.datastructures.HANStack;
import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */

public class ASTListener extends ICSSBaseListener {

    //Accumulator attributes:

    private AST ast;

    //Use this to keep track of the parent nodes when recursively traversing the ast
    private IHANStack<ASTNode> currentContainer;

    public ASTListener() {
        ast = new AST();
        currentContainer = new HANStack<>();
    }

    public AST getAST() {
        return ast;
    }

    //Created root node AST
    @Override
    public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
        ASTNode stylesheet = new Stylesheet();
        currentContainer.push(stylesheet);
    }

    //Routes root node to AST after parsing the entire stylesheet
    @Override
    public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
        ast.setRoot((Stylesheet) currentContainer.pop());
    }

    //Stylerule
    @Override
    public void enterStyleRule(ICSSParser.StyleRuleContext ctx) {
        ASTNode styleRule = new Stylerule();
        currentContainer.push(styleRule);
    }

    @Override
    public void exitStyleRule(ICSSParser.StyleRuleContext ctx) {
        ASTNode styleRule = currentContainer.pop();
        currentContainer.peek().addChild(styleRule);
    }

    //Selector of stylerule
    @Override
    public void enterTagSelector(ICSSParser.TagSelectorContext ctx) {
        ASTNode tagSelector = new TagSelector(ctx.getText());
        currentContainer.push(tagSelector);
    }

    @Override
    public void exitTagSelector(ICSSParser.TagSelectorContext ctx) {
        ASTNode tagSelector = currentContainer.pop();
        currentContainer.peek().addChild(tagSelector);
    }

    @Override
    public void enterClassSelector(ICSSParser.ClassSelectorContext ctx) {
        ASTNode classSelector = new ClassSelector(ctx.getText());
        currentContainer.push(classSelector);
    }

    @Override
    public void exitClassSelector(ICSSParser.ClassSelectorContext ctx) {
        ASTNode classSelector = currentContainer.pop();
        currentContainer.peek().addChild(classSelector);
    }

    @Override
    public void enterIdSelector(ICSSParser.IdSelectorContext ctx) {
        ASTNode idSelector = new IdSelector(ctx.getText());
        currentContainer.push(idSelector);
    }

    @Override
    public void exitIdSelector(ICSSParser.IdSelectorContext ctx) {
        ASTNode idSelector = currentContainer.pop();
        currentContainer.peek().addChild(idSelector);
    }


    //Declarations
    @Override
    public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
        ASTNode declaration = new Declaration();
        currentContainer.push(declaration);
    }

    @Override
    public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
        ASTNode declaration = currentContainer.pop();
        currentContainer.peek().addChild(declaration);
    }

    @Override
    public void enterPropertyName(ICSSParser.PropertyNameContext ctx) {
        currentContainer.peek().addChild(new PropertyName(ctx.getText()));
    }

    @Override
    public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        ASTNode variableAssignment = new VariableAssignment();
        currentContainer.push(variableAssignment);
    }

    @Override
    public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        ASTNode variableAssignment = currentContainer.pop();
        currentContainer.peek().addChild(variableAssignment);
    }


    //Variable reference
    @Override
    public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
        currentContainer.peek().addChild(new VariableReference(ctx.getText()));
    }


    //Expression
    public void exitExpression(ICSSParser.ExpressionContext ctx) {
        Expression expression = null;
        if (ctx.getChildCount() == 1) {
            if (Pattern.matches("[0-9]+px", ctx.getText())) {
                expression = new PixelLiteral(ctx.getChild(0).getText());
            }
            if (ctx.getText().matches("[0-9]+ '%'")) {
                expression = new PercentageLiteral(ctx.getChild(0).getText());
            }
            if (ctx.getText().matches("[A-Z] [A-Za-z0-9_]*")) {
                expression = new VariableReference(ctx.getChild(0).getText());
            }
            if (ctx.getText().matches("[0-9]+")) {
                expression = new ScalarLiteral(ctx.getChild(0).getText());
            }
            if (ctx.getText().matches("'FALSE'") || ctx.getText().matches("'TRUE'")) {
                expression = new BoolLiteral(ctx.getChild(0).getText());
            }
            if (ctx.getText().matches("'#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f]")) {
                expression = new ColorLiteral(ctx.getChild(0).getText());
            }
            currentContainer.push(expression);
        }

        if (ctx.getChildCount() == 3) {
            if (ctx.getChild(1).getText().equals("*")) {
                expression = new MultiplyOperation();
            }
            if (ctx.getChild(1).getText().equals("+")) {
                expression = new AddOperation();
            }
            if (ctx.getChild(1).getText().equals("-")) {
                expression = new SubtractOperation();
            }

            ASTNode astNode1 = currentContainer.pop();
            expression.addChild(astNode1);
            ASTNode astNode2 = currentContainer.pop();
            expression.addChild(astNode2);
            currentContainer.peek().addChild(expression);
        }
    }

}