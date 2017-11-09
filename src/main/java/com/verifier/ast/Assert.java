package com.verifier.ast;

import com.verifier.lexer.JSymbol;

public class Assert extends Statement
{

    private BExp expr;

    public Assert(JSymbol jsymbol, BExp expr) {
        super(jsymbol);
        this.expr = expr;
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
        return "[ " + expr + " ]";
    }
}
