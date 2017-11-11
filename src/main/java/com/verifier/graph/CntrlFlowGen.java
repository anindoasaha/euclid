package com.verifier.graph;

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
import com.verifier.ast.Visitor;
import com.verifier.ast.While;

public class CntrlFlowGen implements Visitor<Node>
{

    private Graph g = new Graph();
    private Node src;
    private Node end;

    public CntrlFlowGen() {
        src = g.createNode();
        end = g.createNode();
    }

    public Graph getG() {
        return g;
    }

    @Override
    public Node visit(Program program) {
        program.getStmt().accept(this);
        return src;
    }

    @Override
    public Node visit(Identifier identifier) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node visit(Assert assert1) {
        Edge e = new Edge(src, end, assert1);
        g.addEdge(e);

        Node errorNode = g.createNode();
        errorNode.isError = true;
        Assert error = new Assert(assert1.getSymbol(), new Not(assert1.getSymbol(), assert1.getExpr(), true));
        Edge errorEdge = new Edge(src, errorNode, error);
        g.addEdge(errorEdge);
        return end;
    }

    @Override
    public Node visit(Assign n) {
        Edge e = new Edge(src, end, n);
        g.addEdge(e);
        return end;
    }

    @Override
    public Node visit(Skip n) {
        Edge e = new Edge(src, end, n);
        g.addEdge(e);
        return end;
    }

    @Override
    public Node visit(Block n) {
        Node dst = end;

        int i = 0;

        while (i < n.getStatListSize() - 1) {
            Statement stat = n.getStatAt(i);
            end = g.createNode();
            src = stat.accept(this);
            i++;
        }
        end = dst;
        Statement stat = n.getStatAt(i);
        dst = stat.accept(this);
        return dst;
    }

    @Override
    public Node visit(IfThenElse n) {

        Node n1 = g.createNode();
        Edge e1 = new Edge(src, n1, new Assert(n.getSymbol(), n.getExpr()));
        g.addEdge(e1);

        Node n2 = g.createNode();
        Edge e2 = new Edge(src, n2, new Assert(n.getSymbol(), new Not(n.getSymbol(), n.getExpr(), true)));
        g.addEdge(e2);

        src = n1;
        n.getThen().accept(this);

        src = n2;
        n.getElze().accept(this);

        return end;
    }

    @Override
    public Node visit(While n) {

        Edge e = new Edge(src, end, new Assert(n.getSymbol(), new Not(n.getSymbol(), n.getExpr(), true)));
        g.addEdge(e);

        Node n1 = g.createNode();
        Edge e1 = new Edge(src, n1, new Assert(n.getSymbol(), n.getExpr()));
        g.addEdge(e1);

        Node tend = end;
        end = src;
        src = n1;
        n.getBody().accept(this);

        return tend;
    }

    @Override
    public Node visit(IntLiteral n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node visit(Plus n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node visit(Minus n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node visit(IdExpr idExpr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node visit(IExp iExp) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node visit(Equals n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node visit(LessThanEquals n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node visit(And n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node visit(Or n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node visit(Not n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node visit(BExp bExp) {
        // TODO Auto-generated method stub
        return null;
    }

}
