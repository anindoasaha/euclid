package com.verifier.clause;

import com.verifier.ast.Assign;
import com.verifier.ast.IExp;
import com.verifier.ast.IdExpr;
import com.verifier.ast.Identifier;

/**
 * Created by Anindo Saha on 11/9/17.
 */
public class Assignment implements Term
{
    public Assign assign = null;

    public Assignment() {

    }

    public Assignment(Assign assign) {
        this.assign = assign;
    }

    @Override
    public String toString() {
        return assign.toString();
    }
}
