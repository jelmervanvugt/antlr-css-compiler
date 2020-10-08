// Generated from C:/Users/jelmer van vugt/Documents/GitHub/antlr-css-compiler/code/icss-compiler/src/main/antlr4/nl/han/ica/icss/parser\ICSS.g4 by ANTLR 4.8
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ICSSParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ICSSVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ICSSParser#stylesheet}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStylesheet(ICSSParser.StylesheetContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitName(ICSSParser.NameContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#expressionType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpressionType(ICSSParser.ExpressionTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#operationType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperationType(ICSSParser.OperationTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#styleruleIdentifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStyleruleIdentifier(ICSSParser.StyleruleIdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#styleruleContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStyleruleContent(ICSSParser.StyleruleContentContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#stylerule}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStylerule(ICSSParser.StyleruleContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#variableDeclaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableDeclaration(ICSSParser.VariableDeclarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#variableReference}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariableReference(ICSSParser.VariableReferenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#ifClause}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfClause(ICSSParser.IfClauseContext ctx);
	/**
	 * Visit a parse tree produced by {@link ICSSParser#ifClauseContent}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIfClauseContent(ICSSParser.IfClauseContentContext ctx);
}