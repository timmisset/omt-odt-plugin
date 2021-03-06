package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class CeilOperator extends BuiltInIntegerOperator {
    private CeilOperator() { }
    public static final CeilOperator INSTANCE = new CeilOperator();

    @Override
    public String getName() {
        return "CEIL";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateDecimal(call.resolvePreviousStep(), holder, call);
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModelConstants.XSD_DECIMAL_INSTANCE);
    }
}
