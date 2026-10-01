package interpreter;

import lexer.Token;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Environment {
    private final Map<String, Object> values = new HashMap<>();
    private final Set<String> constants = new HashSet<>();

    public void define(String name, Object value, boolean isConstant) {
        if(this.values.containsKey(name)) throw new RuntimeException("La variable '" + name + "' est déjà définie dans ce contexte.");

        this.values.put(name, value);

        if(isConstant) constants.add(name);

    }

    public Object get(Token name) {
        String varName = name.getLexeme();

        if (this.values.containsKey(varName)) return this.values.get(varName);

        throw new RuntimeException("Ligne " + name.getLine() + " : Variable non définie '" + varName + "'.");

    }

    public void assign(Token name, Object value) {
        String varName = name.getLexeme();

        if (!this.values.containsKey(varName)) throw new RuntimeException("Ligne " + name.getLine() + " : Variable non définie '" + varName + "'.");

        if (this.constants.contains(varName)) throw new RuntimeException("Ligne " + name.getLine() + " : Impossible de modifier la constante '" + varName + "'.");

        this.values.put(varName, value);
    }
}
