package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

public class DurationOperator extends BuiltInOperator {
    private DurationOperator() { }
    public static final DurationOperator INSTANCE = new DurationOperator();

    @Override
    public String getName() {
        return "DURATION";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.XSD_DURATION_INSTANCE;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        validateNumberArgument(0, call, holder);
        validateStringArgument(1, call, holder);
    }
}
