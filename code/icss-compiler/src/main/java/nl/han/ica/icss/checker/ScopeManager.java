package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;

import java.util.HashMap;

public class ScopeManager<T> {

    private HANLinkedList<HashMap<String, T>> variables;

    public ScopeManager() {
        variables = new HANLinkedList<>();
    }

    public void enterScope() {
        HashMap<String, T> hashMap = new HashMap<>();
        variables.addFirst(hashMap);
    }

    public void exitScope() {
        if(!variables.isEmpty()) {
            variables.removeFirst();
        }
    }

    public void addVariable(String varName, T anytype) {
        HashMap<String, T> scopeVariables = variables.getFirst();
        scopeVariables.put(varName, anytype);
        variables.removeFirst();
        variables.addFirst(scopeVariables);
    }

    public T getVariable(String varName) {
        for(HashMap<String, T> hashMap : variables) {
            T anytype = hashMap.get(varName);
            if(anytype != null) return anytype;
        }
        return null;
    }

}
