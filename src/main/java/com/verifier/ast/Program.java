package com.verifier.ast;

import com.verifier.lexer.JSymbol;

public class Program extends Tree {

	Statement stmt;

	public Program(JSymbol jsymbol, Statement st) {
		super(jsymbol);
		stmt = st;
	}

	public Statement getStmt() {
		return stmt;
	}

	public void setStmt(Statement stmt) {
		this.stmt = stmt;
	}

	@Override
	public <R> R accept(Visitor<R> v) {
		return v.visit(this);
	}

}
