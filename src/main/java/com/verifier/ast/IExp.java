package com.verifier.ast;

import com.verifier.lexer.JSymbol;

public class IExp extends Tree
{

    public IExp(JSymbol jsymbol) {
        super(jsymbol);
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
