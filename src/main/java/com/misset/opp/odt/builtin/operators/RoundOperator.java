package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class RoundOperator extends BuiltInOperator {
    private RoundOperator() { }
    public static final RoundOperator INSTANCE = new RoundOperator();

    @Override
    public String getName() {
        return "ROUND";
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           PsiCall call) {
        final int numberOfArguments = call.getNumberOfArguments();
        if (numberOfArguments == 0) {
            return Set.of(OppModel.INSTANCE.XSD_INTEGER_INSTANCE);
        } else {
            if ("0".equals(call.getSignatureValue(0))) {
                return Set.of(OppModel.INSTANCE.XSD_INTEGER_INSTANCE);
            }
            return Set.of(OppModel.INSTANCE.XSD_DECIMAL_INSTANCE);
        }
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        Set<OntResource> callInputType = call.resolveCallInput();
        TTLValidationUtil.validateNumber(callInputType, holder, call);
        validateNumberArgument(0, call, holder);

        if ("0".equals(call.getSignatureValue(0))) {
            holder.registerProblem(call.getCallSignatureArgumentElement(0),
                    "Unnecessary decimalPlaces value", ProblemHighlightType.WEAK_WARNING);
        }
        if (!callInputType.isEmpty() && callInputType.stream().allMatch(OppModel.INSTANCE.XSD_INTEGER_INSTANCE::equals)) {
            holder.registerProblem(call.getCallSignatureArgumentElement(0),
                    "Input is already an integer", ProblemHighlightType.WEAK_WARNING);
        }
    }


    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModel.INSTANCE.XSD_INTEGER_INSTANCE);
        }
        return null;
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModel.INSTANCE.XSD_DECIMAL_INSTANCE);
    }
}
