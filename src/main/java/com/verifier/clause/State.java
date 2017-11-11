package com.verifier.clause;

import com.verifier.ast.Identifier;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by Anindo Saha on 11/9/17.
 */
public class State implements Term
{
    public static Set<String> variables = null;
    // P0(x,y)
    public String id = null;
    public Set<String> on = null;

    public State() {
        this.id = String.valueOf(IdGen.next());
        this.on = new TreeSet<>(variables);
    }

    public State(Set<String> on) {
        this.id = String.valueOf(IdGen.next());
        this.on = new TreeSet<>(on);
    }

    public State(String id, Set<String> on) {
        this.id = id;
        this.on = new TreeSet<>(on);
    }

    public State reset() {
        State state = new State(this.id, variables);
        return state;
    }

    @Override
    public String toString() {
        return "(P" + id + "" + on.stream().reduce("", (s1, s2) -> s1 + " " + s2) + ")";
    }

    public static class IdGen
    {
        private static int init = 0;

        public static int next() {
            return init++;
        }
    }
}
