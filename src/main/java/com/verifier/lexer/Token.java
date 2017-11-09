package com.verifier.lexer;

public enum Token {

	ID(98), RPAREN(20), AND(79), OR(80), LPAREN(5), INTLIT(93), PLUS(60), WHILE(48), ELSE(43), EQUALS(74), EOF(
			0), SEMICOLON(7), MINUS(61), IF(42), BAD(1), LESSEQUALS(
					13), NOT(15), SKIP(17), ENDIF(19), ASSERT(21), ENDWHILE(22), EQSIGN(12), SENTINEL(100);

	private int sym;

	Token(int sym) {
		this.sym = sym;
	}

	public int getSym() {
		return sym;
	}
}
