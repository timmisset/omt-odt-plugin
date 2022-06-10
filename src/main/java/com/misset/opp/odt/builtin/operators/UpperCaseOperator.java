package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class UpperCaseOperator extends BuiltInStringOperator {
    private UpperCaseOperator() { }
    public static final UpperCaseOperator INSTANCE = new UpperCaseOperator();

    @Override
    public String getName() {
        return "UPPERCASE";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateString(call.resolvePreviousStep(), holder, call);
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE);
    }
}
