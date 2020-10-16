package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;

public class Generator {

    StringBuilder stringBuilder;

    public Generator() {
        stringBuilder = new StringBuilder();
    }

    public String generate(AST ast) {
        generateStylesheet(ast.root);
        return stringBuilder.toString();
    }

    private void generateStylesheet(Stylesheet stylesheet) {
        stylesheet.getChildren().forEach(astNode -> {
            if (astNode instanceof Stylerule)
                generateStylerule((Stylerule) astNode);
        });
    }

    private void generateStylerule(Stylerule stylerule) {
        stringBuilder.append(stylerule.selectors.get(0)).append(" {\n");
        stylerule.getChildren().forEach(astNode -> {
            if (astNode instanceof Declaration)
                generateDeclaration((Declaration) astNode);
        });
        stringBuilder.append("}\n\n");
    }

    private void generateDeclaration(Declaration node) {
        stringBuilder.append("  ").append(node.property.name).append(": ");
        generateExpression((Literal) node.expression);
        stringBuilder.append("; \n");
    }

    private void generateExpression(Literal literal) {
        if (literal instanceof ColorLiteral)
            stringBuilder.append(((ColorLiteral) literal).value);
        else if (literal instanceof PercentageLiteral)
            stringBuilder.append(((PercentageLiteral) literal).value).append("%");
        else
            stringBuilder.append(((PixelLiteral) literal).value).append("px");
    }


}
