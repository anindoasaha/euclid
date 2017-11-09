package com.verifier.ast;

import com.verifier.lexer.JSymbol;

public class IfThenElse extends Statement
{
    public BExp expr;
    public Statement then;
    public Statement elze; // we cannot use "else" here since it is a reserved
    // word

    public IfThenElse(JSymbol jSymbol, BExp expr, Statement then, Statement elze) {
        super(jSymbol);
        this.expr = expr;
        this.then = then;
        this.elze = elze;
    }

    public BExp getExpr() {
        return expr;
    }

    public void setExpr(BExp expr) {
        this.expr = expr;
    }

    public Statement getThen() {
        return then;
    }

    public void setThen(Statement then) {
        this.then = then;
    }

    public Statement getElze() {
        return elze;
    }

    public void setElze(Statement elze) {
        this.elze = elze;
    }

    @Override
    public <R> R accept(Visitor<R> v) {
        return v.visit(this);
    }

    @Override
    public String toString() {
        return " if( " + expr + " ) " + then + " else " + elze + " endif";
    }
}
