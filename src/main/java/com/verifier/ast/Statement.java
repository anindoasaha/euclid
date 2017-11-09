package com.verifier.ast;

import com.verifier.lexer.JSymbol;

public abstract class Statement extends Tree {

	public Statement(JSymbol jsymbol) {
		super(jsymbol);
	}

}
