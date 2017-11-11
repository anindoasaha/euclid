package com.verifier.clause;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anindo Saha on 11/10/17.
 */
public class SExp
{
    String root = "";

    List<SExp> children = null;

    SExp(String root) {
        this.root = root;
        this.children = new ArrayList<>();
    }

    SExp sexp(String root) {
        return new SExp(root);
    }

    SExp add(SExp child) {
        this.children.add(child);
        return this;
    }
}
