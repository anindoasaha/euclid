package com.verifier.graph;

import java.util.ArrayList;
import java.util.List;

public class Node {

	private int id;
	private List<Node> nxtNodes = new ArrayList<Node>();

	public Node(int id) {
		this.id = id;
	}

	public void add(Node node) {
		nxtNodes.add(node);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return Integer.toString(id);
	}

}
