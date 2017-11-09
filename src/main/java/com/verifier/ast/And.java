package com.verifier.ast;

import com.verifier.lexer.JSymbol;

public class And extends BExp
{
    public BExp lhs;
    public BExp rhs;

    public And(JSymbol jSymbol, BExp lhs, BExp rhs) {
        super(jSymbol);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public BExp getLhs() {
        return lhs;
    }

    public void setLhs(BExp lhs) {
        this.lhs = lhs;
    }

    public BExp getRhs() {
        return rhs;
    }

    public void setRhs(BExp rhs) {
        this.rhs = rhs;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }

    @Override
    public String toString() {
        return lhs + " and " + rhs;
    }
}
