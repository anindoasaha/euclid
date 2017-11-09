package com.verifier.graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {

	private List<Node> adj = new ArrayList<>();
	private List<Edge> edgeList = new ArrayList<>();
	private List<Integer> indegree = new ArrayList<>();

	public Graph() {
		// adj.add(new Node(0));
		// adj.add(new Node(1));
	}

	public Node createNode() {
		Node n = new Node(adj.size());
		adj.add(n);
		return n;
	}

	public void addEdge(Edge e) {
		Node src = e.getSrc();
		Node dst = e.getDst();
		addEdge(src.getId(), dst.getId());
		edgeList.add(e);
	}

	private void addEdge(int v, int w) {
		validate(v);
		validate(w);
		adj.get(v).add(adj.get(w));
		// indegree.set(w, indegree.get(w) + 1);
	}

	/*
	 * public void removeEdge(u, v) { validate(v); validate(w);
	 *
	 * for (Node n : adj.get(v))
	 *
	 * .add(adj.get(w)); indegree.set(w, indegree.get(w) + 1); }
	 */

	public void addEdgePair(int u, int v, int w) {
		validate(u);
		validate(v);
		validate(w);

		addEdge(u, v);
		addEdge(v, w);

		adj.get(v).add(adj.get(w));
		indegree.set(w, indegree.get(w) + 1);
	}

	private void validate(int v) {
		if (v < 0 || v >= adj.size()) {
			throw new IllegalArgumentException("vertex " + v + " is not valid");
		}
	}

	public String getDotSrc() {
		StringBuilder sb = new StringBuilder();
		sb.append(" digraph G {\n");
		for (Edge e : edgeList) {
			sb.append(e.toString());
			sb.append("\n");
		}
		sb.append("}");
		return sb.toString();
	}

}
