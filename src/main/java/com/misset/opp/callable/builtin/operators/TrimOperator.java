package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class TrimOperator extends BuiltInStringOperator {
    private TrimOperator() { }
    public static final TrimOperator INSTANCE = new TrimOperator();

    @Override
    public String getName() {
        return "TRIM";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateString(call.resolveCallInput(), holder, call);
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE);
    }
}
