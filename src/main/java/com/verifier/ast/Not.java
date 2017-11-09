package com.verifier.ast;

import com.verifier.lexer.JSymbol;

public class Not extends BExp
{
    public BExp expr;
    public boolean notational = false;

    public Not(JSymbol jSymbol, BExp expr) {
        super(jSymbol);
        this.expr = expr;
    }

    public Not(JSymbol jSymbol, BExp expr, boolean notational) {
        super(jSymbol);
        this.expr = expr;
        this.notational = notational;
    }

    public BExp getExpr() {
        return expr;
    }

    public void setExpr(BExp expr) {
        this.expr = expr;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }

    @Override
    public String toString() {
        return (notational ? "~ " : "not ") + expr;
    }
}
