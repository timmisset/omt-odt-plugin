package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class EveryOperator extends BuiltInBooleanOperator {
    private EveryOperator() {
    }

    public static final EveryOperator INSTANCE = new EveryOperator();

    @Override
    public String getName() {
        return "EVERY";
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
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        if (call.getNumberOfArguments() == 0) {
            TTLValidationUtil.validateBoolean(call.resolvePreviousStep(), holder, call);
        } else {
            validateBooleanArgument(0, call, holder);
        }
    }

    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        return Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE);
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE);
    }
}
