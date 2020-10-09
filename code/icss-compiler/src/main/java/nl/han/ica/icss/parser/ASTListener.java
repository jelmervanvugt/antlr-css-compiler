package nl.han.ica.icss.parser;

import java.util.Stack;


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
    @Override public void enterTagSelector(ICSSParser.TagSelectorContext ctx) {
        ASTNode tagSelector = new TagSelector(ctx.getText());
        currentContainer.push(tagSelector);
    }

    @Override public void exitTagSelector(ICSSParser.TagSelectorContext ctx) {
        ASTNode tagSelector = currentContainer.pop();
        currentContainer.peek().addChild(tagSelector);
    }

    @Override public void enterClassSelector(ICSSParser.ClassSelectorContext ctx) {
        ASTNode classSelector = new ClassSelector(ctx.getText());
        currentContainer.push(classSelector);
    }

    @Override public void exitClassSelector(ICSSParser.ClassSelectorContext ctx) {
        ASTNode classSelector = currentContainer.pop();
        currentContainer.peek().addChild(classSelector);
    }

    @Override public void enterIdSelector(ICSSParser.IdSelectorContext ctx) {
        ASTNode idSelector = new IdSelector(ctx.getText());
        currentContainer.push(idSelector);
    }

    @Override public void exitIdSelector(ICSSParser.IdSelectorContext ctx) {
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

    @Override public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        ASTNode variableAssignment = new VariableAssignment();
        currentContainer.push(variableAssignment);
    }

    @Override public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
        ASTNode variableAssignment = currentContainer.pop();
        currentContainer.peek().addChild(variableAssignment);
    }


    //Variablereference
    @Override public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
        currentContainer.peek().addChild(new VariableReference(ctx.getText()));
    }

    //Literals
    @Override public void enterBoolLiteral(ICSSParser.BoolLiteralContext ctx) {
        currentContainer.peek().addChild(new BoolLiteral(ctx.getText()));
    }

    @Override public void enterColorLiteral(ICSSParser.ColorLiteralContext ctx) {
        currentContainer.peek().addChild(new ColorLiteral(ctx.getText()));
    }

    @Override public void enterPercentageLiteral(ICSSParser.PercentageLiteralContext ctx) {
        currentContainer.peek().addChild(new PercentageLiteral(ctx.getText()));
    }

    @Override public void enterPixelLiteral(ICSSParser.PixelLiteralContext ctx) {
        currentContainer.peek().addChild(new PixelLiteral(ctx.getText()));
    }

    @Override public void enterScalarLiteral(ICSSParser.ScalarLiteralContext ctx) {
        currentContainer.peek().addChild(new ScalarLiteral(ctx.getText()));
    }



}