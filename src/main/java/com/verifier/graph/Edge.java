package com.verifier.graph;

import com.verifier.ast.Statement;

public class Edge
{

    private Node src;
    private Node dst;
    private Statement st;

    public Edge(Node src, Node dst) {
        this.src = src;
        this.dst = dst;
    }

    public Edge(Node src, Node dst, Statement st) {
        this.src = src;
        this.dst = dst;
        this.st = st;
    }

    public Node getSrc() {
        return src;
    }

    public void setSrc(Node src) {
        this.src = src;
    }

    public Node getDst() {
        return dst;
    }

    public void setDst(Node dst) {
        this.dst = dst;
    }

    public Statement getSt() {
        return st;
    }

    public void setSt(Statement st) {
        this.st = st;
    }

    @Override
    public String toString() {
        if (src == null || dst == null) {
            return super.toString();
        }
        return src.toString() + " -> " + dst.toString() + "[label= \"" + st.toString() + "\"]";
    }

}
