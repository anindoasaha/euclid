package com.verifier.ast;

import com.verifier.lexer.JSymbol;

public class LessThanEquals extends BExp
{
    public IExp lhs;
    public IExp rhs;

    public LessThanEquals(JSymbol jSymbol, IExp lhs, IExp rhs) {
        super(jSymbol);
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public IExp getLhs() {
        return lhs;
    }

    public void setLhs(IExp lhs) {
        this.lhs = lhs;
    }

    public IExp getRhs() {
        return rhs;
    }

    public void setRhs(IExp rhs) {
        this.rhs = rhs;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }

    @Override
    public String toString() {
        return lhs + " <= " + rhs;
    }
}
