package com.verifier.clause;

import java.util.*;

/**
 * Created by Anindo Saha on 11/9/17.
 */
public class Antecedent implements Comparable<Antecedent>
{
    public Set<String> quantifiers = null;
    public List<Term> terms = null;

    public Antecedent() {
        this.quantifiers = new TreeSet<>();
        this.terms = new ArrayList<>();
    }

    public Term get(int index) {
        return terms.get(index);
    }

    public Term set(int index, Term element) {
        return terms.set(index, element);
    }

    public void term(Term element) {
        terms.add(element);
    }

    public void quantifier(String quantifier) {
        quantifiers.add(quantifier);
    }

    public void quantifiers(Set<String> quantifiers) {
        this.quantifiers = quantifiers;
    }

    public Term remove(int index) {
        return terms.remove(index);
    }

    public int indexOf(Object o) {
        return terms.indexOf(o);
    }

    @Override
    public int compareTo(Antecedent o) {
        return 0;
    }
}
