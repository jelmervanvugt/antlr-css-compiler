// Generated from nl\han\ica\icss\parser\ICSS.g4 by ANTLR 4.8
package nl.han.ica.icss.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ICSSParser}.
 */
public interface ICSSListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ICSSParser#stylesheet}.
	 * @param ctx the parse tree
	 */
	void enterStylesheet(ICSSParser.StylesheetContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#stylesheet}.
	 * @param ctx the parse tree
	 */
	void exitStylesheet(ICSSParser.StylesheetContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(ICSSParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(ICSSParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#expressionType}.
	 * @param ctx the parse tree
	 */
	void enterExpressionType(ICSSParser.ExpressionTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#expressionType}.
	 * @param ctx the parse tree
	 */
	void exitExpressionType(ICSSParser.ExpressionTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#operationType}.
	 * @param ctx the parse tree
	 */
	void enterOperationType(ICSSParser.OperationTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#operationType}.
	 * @param ctx the parse tree
	 */
	void exitOperationType(ICSSParser.OperationTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#styleruleIdentifier}.
	 * @param ctx the parse tree
	 */
	void enterStyleruleIdentifier(ICSSParser.StyleruleIdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#styleruleIdentifier}.
	 * @param ctx the parse tree
	 */
	void exitStyleruleIdentifier(ICSSParser.StyleruleIdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#styleruleContent}.
	 * @param ctx the parse tree
	 */
	void enterStyleruleContent(ICSSParser.StyleruleContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#styleruleContent}.
	 * @param ctx the parse tree
	 */
	void exitStyleruleContent(ICSSParser.StyleruleContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#stylerule}.
	 * @param ctx the parse tree
	 */
	void enterStylerule(ICSSParser.StyleruleContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#stylerule}.
	 * @param ctx the parse tree
	 */
	void exitStylerule(ICSSParser.StyleruleContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(ICSSParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(ICSSParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ICSSParser#variableReference}.
	 * @param ctx the parse tree
	 */
	void enterVariableReference(ICSSParser.VariableReferenceContext ctx);
	/**
	 * Exit a parse tree produced by {@link ICSSParser#variableReference}.
	 * @param ctx the parse tree
	 */
	void exitVariableReference(ICSSParser.VariableReferenceContext ctx);
}