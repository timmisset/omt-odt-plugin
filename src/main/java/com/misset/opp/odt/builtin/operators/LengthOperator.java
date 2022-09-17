package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class LengthOperator extends BuiltInIntegerOperator {
    private LengthOperator() { }
    public static final LengthOperator INSTANCE = new LengthOperator();

    @Override
    public String getName() {
        return "LENGTH";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        OntologyValidationUtil.validateString(call.resolvePreviousStep(), holder, call);
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OntologyModelConstants.getXsdStringInstance());
    }
}
