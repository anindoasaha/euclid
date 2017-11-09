package com.verifier.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

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
import com.verifier.ast.Visitor;
import com.verifier.ast.While;
import com.verifier.lexer.Lexer;

public class ASTPrinter implements Visitor<String> {

	int level = 0;

	void incLevel() {
		level = level + 1;
	}

	void decLevel() {
		level = level - 1;
	}

	public String printInc() {
		char[] chars = new char[level];
		java.util.Arrays.fill(chars, '\t');
		return new String(chars);
	}

	@Override
	public String visit(Assign n) {
		return printInc() + "(= " + n.id.accept(this) + " " + n.expr.accept(this) + ")";
	}

	@Override
	public String visit(Skip n) {
		return "";
	}

	@Override
	public String visit(Block n) {
		StringBuilder strBuilder = new StringBuilder();
		for (Statement stat : n.body) {
			strBuilder.append(stat.accept(this) + "\n");
		}
		return strBuilder.toString();
	}

	@Override
	public String visit(IfThenElse n) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(printInc() + "(if " + n.expr.accept(this) + "\n");

		incLevel();
		strBuilder.append(n.then.accept(this) + "\n");
		String elze = n.elze.accept(this);
		if (elze != null && elze.trim().length() > 0) {
			strBuilder.append(elze + "\n");
		}
		decLevel();

		strBuilder.append(printInc() + "endif)\n");
		return strBuilder.toString();
	}

	@Override
	public String visit(While n) {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(printInc() + "(while " + n.expr.accept(this) + "\n");

		incLevel();
		strBuilder.append(n.body.accept(this));
		decLevel();

		strBuilder.append(printInc() + "endwhile)\n");
		return strBuilder.toString();
	}

	@Override
	public String visit(IntLiteral n) {
		return "(INTLIT " + n.value + ")";
	}

	@Override
	public String visit(Plus n) {
		return "(+ " + n.lhs.accept(this) + " " + n.rhs.accept(this) + ")";
	}

	@Override
	public String visit(Minus n) {
		return "(- " + n.lhs.accept(this) + " " + n.rhs.accept(this) + ")";
	}

	@Override
	public String visit(Equals n) {
		return "(== " + n.lhs.accept(this) + " " + n.rhs.accept(this) + ")";
	}

	@Override
	public String visit(LessThanEquals n) {
		return "(<= " + n.lhs.accept(this) + " " + n.rhs.accept(this) + ")";
	}

	@Override
	public String visit(And n) {
		return "(and " + n.lhs.accept(this) + " " + n.rhs.accept(this) + ")";
	}

	@Override
	public String visit(Or n) {
		return "(or " + n.lhs.accept(this) + " " + n.rhs.accept(this) + ")";
	}

	@Override
	public String visit(Not n) {
		return "(not " + n.expr.accept(this) + ")";
	}

	@Override
	public String visit(IdExpr identifier) {
		return "(ID " + identifier.varID + ")";
	}

	@Override
	public String visit(Program program) {
		StringBuilder sb = new StringBuilder();

		incLevel();
		sb.append(program.getStmt().accept(this) + "\n");
		decLevel();

		return sb.toString();
	}

	@Override
	public String visit(Identifier identifier) {
		return "(ID " + identifier.varID + ")";
	}

	public static String printFileAst(File file) throws FileNotFoundException, ParseException {
		ASTPrinter printer = new ASTPrinter();
		Lexer lexer = new Lexer(new FileReader(file));
		Parser p = new Parser(lexer);
		Tree tree = p.parse();
		return printer.visit((Program) tree);

	}

	@Override
	public String visit(Assert assert1) {
		return printInc() + "(assert " + assert1.getExpr().accept(this) + ")";
	}

	@Override
	public String visit(IExp iExp) {
		return printInc() + "( " + iExp.accept(this) + ")";
	}

	@Override
	public String visit(BExp bExp) {
		return printInc() + "( " + bExp.accept(this) + ")";
	}

}
