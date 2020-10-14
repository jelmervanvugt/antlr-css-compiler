package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.icss.ast.types.ExpressionType;

import java.util.HashMap;

public class ScopeManager {

    private HANLinkedList<HashMap<String, ExpressionType>> variables;

    public ScopeManager() {
        variables = new HANLinkedList<>();
    }

    public void enterScope() {
        HashMap<String, ExpressionType> hashMap = new HashMap<>();
        variables.addFirst(hashMap);
    }

    public void exitScope() {
        if(!variables.isEmpty()) {
            variables.removeFirst();
        }
    }

    public void addVariable(String varName, ExpressionType expressionType) {
        HashMap<String, ExpressionType> scopeVariables = variables.getFirst();
        scopeVariables.put(varName, expressionType);
        variables.removeFirst();
        variables.addFirst(scopeVariables);
    }

    public ExpressionType getVariable(String varName) {
        for(HashMap<String, ExpressionType> hashMap : variables) {
            ExpressionType expressionType = hashMap.get(varName);
            if(expressionType != null) return expressionType;
        }
        return null;
    }

}
