package com.verifier.graph;

import com.verifier.ast.*;
import com.verifier.clause.*;
import com.verifier.parser.SMTPrinter;
import com.verifier.parser.VarDupChecker;

import java.util.*;

public class Graph
{

    private List<Node> adj = new ArrayList<>();
    private List<Edge> edgeList = new ArrayList<>();
    private List<Integer> indegree = new ArrayList<>();

    public Graph() {
        // adj.term(new Node(0));
        // adj.term(new Node(1));
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
	 * .term(adj.get(w)); indegree.set(w, indegree.get(w) + 1); }
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
            //System.out.println(e.getSt().getClass());
            sb.append(e.toString());
            sb.append("\n");
        }
        sb.append("}");
        return sb.toString();
    }

    public void getHornClauses() {
        State.variables = new TreeSet<>(Identifier.getIds());
        Set<String> initializedVars = new TreeSet<>();

        Map<Antecedent, Consequent> assertions = new LinkedHashMap<>();
        // (set-logic HORN)
        // (declare-fun P1 (Int) Bool)
        // (declare-fun P2 (Int) Bool)
        // (assert (forall ((n Int)) (=> (= n 0) (P1 n) )))
        // Assert -> Forall -> => -> LHS RHS

        // (declare-fun P1 (Int) Bool)
        // Name, Int as per number of state variables, Bool always in our case
        Set<Consequent> declareFunctions = new LinkedHashSet<>();

        Map<Node, Consequent> nodeConsequentMap = new HashMap<Node, Consequent>();
        Antecedent lhsClause0 = new Antecedent();
        lhsClause0.term(Predicate.TRUE);
        Set<String> q0 = new TreeSet<>(State.variables);
        lhsClause0.quantifiers(q0);

        Consequent rhsClause0 = new Consequent();
        assertions.put(lhsClause0, rhsClause0);
        declareFunctions.add(rhsClause0);
        nodeConsequentMap.put(edgeList.get(0).getSrc(), rhsClause0);
        for (Edge e : edgeList) {
            Antecedent lhsClause = new Antecedent();
            Consequent prevConsequent = nodeConsequentMap.getOrDefault(e.getSrc(), rhsClause0);
            State state = (State) prevConsequent.getTerm();
            lhsClause.term(state.reset());
            Statement statement = e.getSt();
            // x -> x'
            Map<String, String> quantifiers = on(State.variables, statement);
            lhsClause.term(term(statement));
            Set<String> q = new TreeSet<>();
            q.addAll(quantifiers.keySet());
            q.addAll(quantifiers.values());
            lhsClause.quantifiers(q);

            Set<String> on = new TreeSet<>();
            on.addAll(quantifiers.values());
            Consequent rhsClause =
                    nodeConsequentMap.getOrDefault(e.getDst(), new Consequent(e.getDst().isError ? Predicate.FALSE : new State(on)));
            if (nodeConsequentMap.get(e.getDst()) != null) {
                Consequent consequent = nodeConsequentMap.get(e.getDst());
                if (consequent.term instanceof State) {
                    rhsClause = new Consequent(new State(((State) consequent.term).id, on));
                }
            } else {
                declareFunctions.add(rhsClause);
            }
            // Create new rhsClause with on variables from rhsClause
            assertions.put(lhsClause, rhsClause);
            nodeConsequentMap.put(e.getDst(), rhsClause);
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(set-logic HORN)\n");
        for (Consequent consequent : declareFunctions) {
            // (declare-fun P1 (Int) Bool)
            if (consequent.term instanceof State) {
                stringBuilder.append("(declare-fun P" + ((State) consequent.term).id + " (" + ((State) consequent.term).on.stream().reduce("", (s1, s2) -> s1 + " " + "Int") + ")" + " Bool)");
                stringBuilder.append("\n");
            }
        }
        for (Map.Entry<Antecedent, Consequent> assertion : assertions.entrySet()) {
            // (assert (forall ((n Int)) (=> (= n 0) (P1 n) )))
            Antecedent key = assertion.getKey();
            Consequent value = assertion.getValue();
            stringBuilder.append("(");
            stringBuilder.append("assert ");
            stringBuilder.append("(");
            stringBuilder.append("forall ");
            stringBuilder.append("(");
            // quantifiers
            int j = 0;
            for (String q : key.quantifiers) {
                stringBuilder.append("(");
                stringBuilder.append(q + " Int");
                stringBuilder.append(")");
                if (j != key.quantifiers.size() - 1)
                    stringBuilder.append(" ");
            }
            stringBuilder.append(")");
            stringBuilder.append("(");
            // implication
            stringBuilder.append("=> ");
            stringBuilder.append("(and ");
            int i = 0;
            SMTPrinter smtPrinter = new SMTPrinter();
            for (Term term : key.terms) {
                stringBuilder.append(sexp(term));
                if (i != key.terms.size() - 1)
                    stringBuilder.append(" ");
                i++;
            }
            // forall in
            stringBuilder.append(") ");
            stringBuilder.append((value.term).toString());
            stringBuilder.append(")");
            stringBuilder.append(")");
            stringBuilder.append(")");
            stringBuilder.append("\n");
        }
        stringBuilder.append("(check-sat)\n");
        stringBuilder.append("(exit)\n");
        System.out.println(stringBuilder.toString());
    }

    /**
     * Create as per statement.
     *
     * @param statement
     * @return
     */
    public Term term(Statement statement) {
        if (statement instanceof Assert) {
            return new Predicate((Assert) statement);
        } else if (statement instanceof Assign) {
            return new Assignment((Assign) statement);
        } else {
            throw new RuntimeException("Non Assert/Assign type found.");
        }
    }

    /**
     * Change consequence on variable as per quantifiers
     *
     * @param quantifiers
     * @param statement
     * @return
     */
    public Map<String, String> on(Set<String> quantifiers, Statement statement) {
        if (statement instanceof Assign) {
            Map<String, String> mod = new TreeMap<>();
            Assign assign = (Assign) statement;
            for (String quantifier : quantifiers) {
                mod.put(quantifier, quantifier);
            }
            try {
                VarDupChecker varDupChecker = new VarDupChecker();
                if (varDupChecker.visit((Assign) statement)) {
                    mod.put(assign.id.varID, assign.id.varID + "_p");
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            return mod;
        } else {
            Map<String, String> mod = new TreeMap<>();
            for (String quantifier : quantifiers) {
                mod.put(quantifier, quantifier);
            }
            return mod;
        }
    }

    /**
     * Create as per statement.
     *
     * @return
     */
    public String sexp(Term term) {
        SMTPrinter smtPrinter = new SMTPrinter();
        if (term instanceof Predicate) {
            Predicate predicate = (Predicate) term;
            return predicate.anAssert != null ?
                    smtPrinter.visit(predicate.anAssert) : predicate.toString();
        } else if (term instanceof Assignment) {
            Assignment assignment = (Assignment) term;
            Assign assign = assignment.assign;
            VarDupChecker varDupChecker = new VarDupChecker();
            if (varDupChecker.visit(assign)) {
                Map<String, String> quantifiers = on(State.variables, assign);
                assign.id.varID = quantifiers.get(assign.id.varID);
            }
            return smtPrinter.visit(assign);
        } else if (term instanceof State) {
            State state = (State) term;
            return state.toString();
        } else {
            throw new RuntimeException("Non Assert/Assign type found.");
        }
    }
}
