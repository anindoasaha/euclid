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

public class VarDupChecker implements Visitor<Boolean> {

	private Identifier lhsId;

	@Override
	public Boolean visit(Program program) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(Identifier identifier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(Assert assert1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(Assign n) {
		lhsId = n.getId();
		return n.getExpr().accept(this);
	}

	@Override
	public Boolean visit(Skip n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(Block n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IfThenElse n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(While n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(IntLiteral n) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Boolean visit(Plus n) {
		return n.getLhs().accept(this) || n.getRhs().accept(this);
	}

	@Override
	public Boolean visit(Minus n) {
		return n.getLhs().accept(this) || n.getRhs().accept(this);
	}

	@Override
	public Boolean visit(IdExpr idExpr) {
		return lhsId != null && lhsId.getVarID().equals(idExpr.getVarID());
	}

	@Override
	public Boolean visit(IExp iExp) {
		return iExp.accept(this);
	}

	@Override
	public Boolean visit(Equals n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(LessThanEquals n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(And n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(Or n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(Not n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean visit(BExp bExp) {
		// TODO Auto-generated method stub
		return null;
	}

}
