package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

public class ErrorOperator extends BuiltInOperator {
    private ErrorOperator() { }
    public static final ErrorOperator INSTANCE = new ErrorOperator();

    @Override
    public String getName() {
        return "ERROR";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.ERROR;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        validateStringArgument(0, call, holder);
    }
}
