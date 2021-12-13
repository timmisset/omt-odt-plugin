package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class FloorOperator extends BuiltInOperator {
    private FloorOperator() {
    }

    public static final FloorOperator INSTANCE = new FloorOperator();

    @Override
    public String getName() {
        return "FLOOR";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.XSD_INTEGER_INSTANCE;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateDecimal(call.resolveCallInput(), holder, call);
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModel.INSTANCE.XSD_DECIMAL_INSTANCE);
    }
}
