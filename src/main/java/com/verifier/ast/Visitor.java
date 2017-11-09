package com.verifier.ast;

public interface Visitor<R> {

	// Program
	public R visit(Program program);

	public R visit(Identifier identifier);

	public R visit(Assert assert1);
	// Statement

	public R visit(Assign n);

	public R visit(Skip n);

	public R visit(Block n);

	public R visit(IfThenElse n);

	public R visit(While n);

	// Identifier Expression

	public R visit(IntLiteral n);

	public R visit(Plus n);

	public R visit(Minus n);

	public R visit(IdExpr idExpr);

	public R visit(IExp iExp);

	// Boolean Expression

	public R visit(Equals n);

	public R visit(LessThanEquals n);

	public R visit(And n);

	public R visit(Or n);

	public R visit(Not n);

	public R visit(BExp bExp);

}
