package com.verifier.ast;

import com.verifier.lexer.JSymbol;

public class Assign extends Statement
{

    public Identifier id;
    public IExp expr;

    public Assign(JSymbol jSymbol, Identifier id, IExp exp) {
        super(jSymbol);
        this.id = id;
        this.expr = exp;
    }

    public Identifier getId() {
        return id;
    }

    public void setId(Identifier id) {
        this.id = id;
    }

    public IExp getExpr() {
        return expr;
    }

    public void setExpr(IExp expr) {
        this.expr = expr;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }

    @Override
    public String toString() {
        return id.toString() + " = " + expr.toString();
    }
}