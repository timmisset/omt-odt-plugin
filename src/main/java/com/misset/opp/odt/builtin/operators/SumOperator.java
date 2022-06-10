package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class SumOperator extends BuiltInOperator {
    private SumOperator() { }
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
        return resources.stream().allMatch(OppModel.INSTANCE.XSD_INTEGER_INSTANCE::equals) ?
                resources :
                Set.of(OppModel.INSTANCE.XSD_DECIMAL_INSTANCE);
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateNumber(call.resolvePreviousStep(), holder, call);
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModel.INSTANCE.XSD_NUMBER_INSTANCE);
    }
}
