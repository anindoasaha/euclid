package com.verifier.ast;

import com.verifier.lexer.JSymbol;

public class IntLiteral extends IExp
{
    public int value;

    public IntLiteral(JSymbol jSymbol, int value) {
        super(jSymbol);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
