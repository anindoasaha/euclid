package com.verifier.ast;

import com.verifier.lexer.JSymbol;

import java.util.Set;
import java.util.TreeSet;

public class Identifier extends Tree
{
    private static Set<String> ids = new TreeSet<>();
    public String varID;

    public Identifier(JSymbol jSymbol, String varID) {
        super(jSymbol);
        this.varID = varID;
        ids.add(varID);
    }

    public static Set<String> getIds() {
        return ids;
    }

    public static void setIds(Set<String> ids) {
        Identifier.ids = ids;
    }

    public static int size() {
        return ids.size();
    }

    public static boolean isEmpty() {
        return ids.isEmpty();
    }

    public static boolean contains(Object o) {
        return ids.contains(o);
    }

    public static boolean add(String s) {
        return ids.add(s);
    }

    public static boolean remove(Object o) {
        return ids.remove(o);
    }

    public String getVarID() {
        return varID;
    }

    public void setVarID(String varID) {
        this.varID = varID;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }

    @Override
    public String toString() {
        return varID;
    }
}
