package nl.han.ica.icss.transforms;

//BEGIN UITWERKING
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;

import java.util.ArrayList;
//EIND UITWERKING

public class RemoveIf implements Transform {

    @Override
    public void apply(AST ast) {
        //checkStylesheet((Stylesheet) ast.root);
    }

//    private void checkStylesheet(Stylesheet stylesheet) {
//        stylesheet.getChildren().forEach(astNode -> {
//            if(astNode instanceof IfClause) {
//                if(checkIfClause((IfClause) astNode)) stylesheet.removeChild(astNode);
//            }
//        });
//    }
//
//    private boolean checkIfClause(IfClause ifClause) {
//
//
//
//        return true;
//    }

}
