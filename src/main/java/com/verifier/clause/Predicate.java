package com.verifier.clause;

import com.verifier.ast.Assert;
import com.verifier.ast.BExp;

/**
 * Created by Anindo Saha on 11/9/17.
 */
public class Predicate implements Term
{
    public static final Predicate TRUE = new Predicate();
    public static final Predicate FALSE = new Predicate();

    public Assert anAssert = null;

    public Predicate() {

    }

    public Predicate(Assert a) {
        this.anAssert = a;
    }

    @Override
    public String toString() {
        if (this == TRUE) {
            return "true";
        } else if (this == FALSE) {
            return "false";
        } else {
            return "(" + anAssert.toString() + ")";
        }
    }
}
