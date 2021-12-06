package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

public class TraverseOperator extends BuiltInOperator {
    private TraverseOperator() { }
    public static final TraverseOperator INSTANCE = new TraverseOperator();

    @Override
    public String getName() {
        return "TRAVERSE";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.OWL_THING_INSTANCE;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        validateStringArgument(0, call, holder);
        validateBooleanArgument(1, call, holder);
    }
}
