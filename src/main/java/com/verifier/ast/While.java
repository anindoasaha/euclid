package com.verifier.ast;

import com.verifier.lexer.JSymbol;

public class While extends Statement
{
    public BExp expr;
    public Statement body;

    public While(JSymbol jSymbol, BExp expr, Statement body) {
        super(jSymbol);
        this.expr = expr;
        this.body = body;
    }

    public BExp getExpr() {
        return expr;
    }

    public void setExpr(BExp expr) {
        this.expr = expr;
    }

    public Statement getBody() {
        return body;
    }

    public void setBody(Statement body) {
        this.body = body;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }
}
