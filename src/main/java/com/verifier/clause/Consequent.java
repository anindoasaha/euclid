package com.verifier.clause;

import java.util.List;

/**
 * Created by Anindo Saha on 11/9/17.
 */
public class Consequent
{
    public Term term = null;

    public Consequent() {
        this.term = new State();
    }

    public Consequent(Term term) {
        this.term = term;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    @Override
    public String toString() {
        return term.toString();
    }
}
