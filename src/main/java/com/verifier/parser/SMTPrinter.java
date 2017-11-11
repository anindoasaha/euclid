package com.verifier.parser;

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
import com.verifier.ast.Visitor;
import com.verifier.ast.While;

public class SMTPrinter implements Visitor<String> {

	@Override
	public String visit(Program program) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(Identifier identifier) {
		return identifier.getVarID();
	}

	@Override
	public String visit(Assert assert1) {
		return assert1.getExpr().accept(this);
	}

	@Override
	public String visit(Assign n) {
		return "(= " + n.getId().accept(this) + " " + n.getExpr().accept(this) + " )";
	}

	@Override
	public String visit(Skip n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(Block n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(IfThenElse n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(While n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String visit(IntLiteral n) {
		return Integer.toString(n.getValue());
	}

	@Override
	public String visit(Plus n) {
		return "(+ " + n.getLhs().accept(this) + " " + n.getRhs().accept(this) + " )";
	}

	@Override
	public String visit(Minus n) {
		return "(- " + n.getLhs().accept(this) + " " + n.getRhs().accept(this) + " )";
	}

	@Override
	public String visit(IdExpr idExpr) {
		return idExpr.getVarID();
	}

	@Override
	public String visit(IExp iExp) {
		return iExp.accept(this);
	}

	@Override
	public String visit(Equals n) {
		return "(= " + n.getLhs().accept(this) + " " + n.getRhs().accept(this) + " )";
	}

	@Override
	public String visit(LessThanEquals n) {
		return "(<= " + n.getLhs().accept(this) + " " + n.getRhs().accept(this) + " )";
	}

	@Override
	public String visit(And n) {
		return "(and " + n.getLhs().accept(this) + " " + n.getRhs().accept(this) + " )";
	}

	@Override
	public String visit(Or n) {
		return "(or " + n.getLhs().accept(this) + " " + n.getRhs().accept(this) + " )";
	}

	@Override
	public String visit(Not n) {
		return "(not " + n.getExpr().accept(this) + " )";
	}

	@Override
	public String visit(BExp bExp) {
		return bExp.accept(this);
	}
}
