package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class LowerCaseOperator extends BuiltInStringOperator {
    private LowerCaseOperator() {
    }

    public static final LowerCaseOperator INSTANCE = new LowerCaseOperator();

    @Override
    public String getName() {
        return "LOWERCASE";
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
        return Set.of(OppModelConstants.XSD_STRING_INSTANCE);
    }
}
