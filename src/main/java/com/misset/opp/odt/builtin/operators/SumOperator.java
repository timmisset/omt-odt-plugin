package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class SumOperator extends AbstractBuiltInOperator {
    private SumOperator() {
    }

    public static final SumOperator INSTANCE = new SumOperator();

    @Override
    public String getName() {
        return "SUM";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources) {
        return resources.stream().allMatch(OntologyModelConstants.getXsdIntegerInstance()::equals) ?
                resources :
                Set.of(OntologyModelConstants.getXsdDecimalInstance());
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        OntologyValidationUtil.validateNumber(call.resolvePreviousStep(), holder, call);
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OntologyModelConstants.getXsdNumberInstance());
    }
}
