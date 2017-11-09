package com.verifier.parser;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.verifier.ast.And;
import com.verifier.ast.Assert;
import com.verifier.ast.Assign;
import com.verifier.ast.BExp;
import com.verifier.ast.Block;
import com.verifier.ast.Equals;
import com.verifier.ast.IExp;
import com.verifier.ast.IdExpr;
import com.verifier.ast.Identifier;
import com.verifier.ast.IfThenElse;
import com.verifier.ast.IntLiteral;
import com.verifier.ast.LessThanEquals;
import com.verifier.ast.Minus;
import com.verifier.ast.Not;
import com.verifier.ast.Or;
import com.verifier.ast.Plus;
import com.verifier.ast.Program;
import com.verifier.ast.Skip;
import com.verifier.ast.Statement;
import com.verifier.ast.Tree;
import com.verifier.ast.While;
import com.verifier.lexer.JSymbol;
import com.verifier.lexer.Lexer;
import com.verifier.lexer.Token;

public class Parser {

	private Lexer lexer;
	private JSymbol symbol;
	private static final JSymbol SENTINEL = new JSymbol(Token.SENTINEL, 0, 0);
	private Deque<JSymbol> iOperator = new ArrayDeque<JSymbol>();
	private Deque<IExp> iOperand = new ArrayDeque<>();
	private Deque<JSymbol> bOperator = new ArrayDeque<>();
	private Deque<BExp> bOperand = new ArrayDeque<>();

	public Parser(Lexer lex) {
		lexer = lex;
		iOperator.push(SENTINEL);
		bOperator.push(SENTINEL);
	}

	public void advance() {
		try {
			symbol = lexer.yylex();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void eat(Token tok) throws ParseException {
		if (tok == symbol.token) {
			advance();
		} else {
			error(symbol.token, tok);
		}
	}

	public Tree parse() throws ParseException {
		Program prog = null;
		try {

			List<Statement> body = new ArrayList<>();

			advance();
			while (symbol.token != Token.EOF) {
				Statement st = parseStmt();
				eat(Token.SEMICOLON);
				body.add(st);
			}

			Block b = new Block(symbol, body);
			prog = new Program(symbol, b);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prog;
	}

	public Statement parseStmt() throws ParseException {
		switch (symbol.token) {

		case SKIP: {
			return new Skip(symbol);
		}

		case ID: {
			// Collect ID
			Identifier id = new Identifier(symbol, (String) symbol.getValue());
			eat(Token.ID);
			eat(Token.EQSIGN);
			IExp exp = parseIExp();
			return new Assign(symbol, id, exp);
		}

		case IF: {
			eat(Token.IF);
			eat(Token.LPAREN);
			BExp bexp = parseBExp();
			eat(Token.RPAREN);
			Statement then = parseStmt();
			eat(Token.ELSE);
			Statement elze = parseStmt();
			eat(Token.ENDIF);
			return new IfThenElse(symbol, bexp, then, elze);
		}

		case WHILE: {
			eat(Token.WHILE);
			eat(Token.LPAREN);
			BExp bexp = parseBExp();
			eat(Token.RPAREN);
			Statement body = parseStmt();
			eat(Token.ENDWHILE);
			return new While(symbol, bexp, body);
		}

		case ASSERT: {
			eat(Token.ASSERT);
			eat(Token.LPAREN);
			BExp bexp = parseBExp();
			eat(Token.RPAREN);
			return new Assert(symbol, bexp);
		}

		default:
			throw new ParseException(symbol.getLine(), symbol.getColumn(), "Invalid token :" + symbol.token);

		}

	}

	public IExp parseIExp() throws ParseException {

		try {
			_parseIExp();
			JSymbol top = iOperator.peek();
			while (top.token != Token.SENTINEL) {
				pop(iOperator);
				top = iOperator.peek();
			}
			return iOperand.pop();

		} catch (ParseException e) {
			System.err.println("Parser Error " + symbol.getLine() + ":" + symbol.getColumn());
			throw e;
		}
	}

	public void _parseIExp() throws ParseException {
		switch (symbol.token) {
		case INTLIT: {
			IntLiteral lit = new IntLiteral(symbol, (Integer) symbol.getValue());
			eat(Token.INTLIT);
			iOperand.push(lit);
			_parseIExp1();
		}
			break;

		case ID: {
			IdExpr exp = new IdExpr(symbol, (String) symbol.getValue());
			eat(Token.ID);
			iOperand.push(exp);
			_parseIExp1();
		}
			break;

		default:
			throw new ParseException(symbol.getLine(), symbol.getColumn(), "Invalid token :" + symbol.token);

		}

	}

	public void _parseIExp1() throws ParseException {
		switch (symbol.token) {
		case PLUS: {
			push(iOperator, symbol);
			eat(Token.PLUS);
			_parseIExp();
			_parseIExp1();
		}
			break;
		case MINUS: {
			push(iOperator, symbol);
			eat(Token.MINUS);
			_parseIExp();
			_parseIExp1();
		}
			break;

		case SEMICOLON:
		case EQUALS:
		case LESSEQUALS:
		case RPAREN:
		case ELSE:
		case ENDIF:
		case ENDWHILE: {
			// Epsilon expected

		}
			break;

		default:
			System.err.println("_parseIExp1(): Epsilon expected");
			throw new ParseException(symbol.getLine(), symbol.getColumn(), "Invalid token :" + symbol.token);

		}
	}

	private void push(Deque<JSymbol> operator, JSymbol current) throws ParseException {
		JSymbol top = operator.peek();
		while (getPriority(top.token) >= getPriority(current.token)) {
			pop(operator);
			top = operator.peek();
		}
		operator.push(current);
	}

	private void pop(Deque<JSymbol> operator) throws ParseException {

		JSymbol top = operator.pop();

		switch (top.token) {
		case PLUS: {
			IExp rhs = iOperand.pop();
			IExp lhs = iOperand.pop();
			Plus plus = new Plus(top, lhs, rhs);
			iOperand.push(plus);
		}
			break;

		case MINUS: {
			IExp rhs = iOperand.pop();
			IExp lhs = iOperand.pop();
			Minus minus = new Minus(top, lhs, rhs);
			iOperand.push(minus);
		}
			break;

		case LESSEQUALS: {
			IExp rhs = iOperand.pop();
			IExp lhs = iOperand.pop();
			LessThanEquals lte = new LessThanEquals(top, lhs, rhs);
			bOperand.push(lte);
		}
			break;

		case EQUALS: {
			IExp rhs = iOperand.pop();
			IExp lhs = iOperand.pop();
			Equals eq = new Equals(top, lhs, rhs);
			bOperand.push(eq);
		}
			break;

		case AND: {
			BExp rhs = bOperand.pop();
			BExp lhs = bOperand.pop();
			And and = new And(top, lhs, rhs);
			bOperand.push(and);
		}
			break;

		case OR: {
			BExp rhs = bOperand.pop();
			BExp lhs = bOperand.pop();
			Or or = new Or(top, lhs, rhs);
			bOperand.push(or);
		}
			break;

		case NOT: {
			BExp bexp = bOperand.pop();
			Not not = new Not(top, bexp);
			bOperand.push(not);
		}
			break;

		default:
			throw new ParseException(top.getLine(), top.getColumn(), "Invalid token :" + top.token);

		}

	}

	private int getPriority(Token operator) {
		switch (operator) {

		case SENTINEL: {
			return -1;
		}

		case OR: {
			return 1;
		}

		case AND: {
			return 2;
		}

		case NOT: {
			return 3;
		}

		case EQUALS: {
			return 4;
		}

		case LESSEQUALS: {
			return 4;
		}

		case PLUS: {
			return 5;
		}

		case MINUS: {
			return 5;
		}

		default:
			return 0;

		}

	}

	public BExp parseBExp() throws ParseException {
		try {
			_parseBExp();
			JSymbol top = bOperator.peek();
			while (top.token != Token.SENTINEL) {
				pop(bOperator);
				top = bOperator.peek();
			}
			return bOperand.pop();

		} catch (ParseException e) {
			System.err.println("Parser Error " + symbol.getLine() + ":" + symbol.getColumn());
			throw e;
		}

	}

	public void _parseBExp() throws ParseException {
		switch (symbol.token) {
		case NOT: {
			push(bOperator, symbol);
			eat(Token.NOT);
			_parseBExp();
			_parseBExp1();
		}
			break;

		case INTLIT:
		case ID: {
			IExp iexp = parseIExp();
			iOperand.push(iexp);
			_parseBExp2();
			_parseBExp1();
		}
			break;

		default:
			throw new ParseException(symbol.getLine(), symbol.getColumn(), "Invalid token :" + symbol.token);

		}
	}

	public void _parseBExp1() throws ParseException {
		switch (symbol.token) {

		case AND: {
			push(bOperator, symbol);
			eat(Token.AND);
			_parseBExp(); // TODO: Check
			_parseBExp1();
		}
			break;

		case OR: {
			push(bOperator, symbol);
			eat(Token.OR);
			_parseBExp(); // TODO: Check
			_parseBExp1();
		}
			break;

		case RPAREN: {
			// Epsilon case
		}
			break;

		default:
			// Add cases for epsilon
			System.out.println("epsilon required found " + symbol.toString());
			throw new ParseException(symbol.getLine(), symbol.getColumn(), "Invalid token :" + symbol.token);
		}
	}

	public void _parseBExp2() throws ParseException {
		switch (symbol.token) {
		case LESSEQUALS: {
			push(bOperator, symbol);
			eat(Token.LESSEQUALS);
			IExp iex = parseIExp();
			iOperand.push(iex);
		}
			break;

		case EQUALS: {
			push(bOperator, symbol);
			eat(Token.EQUALS);
			IExp iex = parseIExp();
			iOperand.push(iex);
		}
			break;

		default:
			throw new ParseException(symbol.getLine(), symbol.getColumn(), "Invalid token :" + symbol.token);

		}
	}

	public void error(Token found, Token expected) throws ParseException {
		throw new ParseException(symbol.getLine(), symbol.getColumn(),
				"Invalid token: " + found + " Expected token: " + expected);
	}

}
