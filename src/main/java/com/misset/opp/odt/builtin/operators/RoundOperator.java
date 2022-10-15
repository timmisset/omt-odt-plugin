package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class RoundOperator extends AbstractBuiltInOperator {

    protected static final String UNNECESSARY_DECIMAL_PLACES_VALUE = "Unnecessary decimalPlaces value";
    protected static final String INPUT_IS_ALREADY_AN_INTEGER = "Input is already an integer";
    private static final List<String> PARAMETER_NAMES = List.of("decimalPlaces");

    private RoundOperator() {
    }

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
            return Set.of(OntologyModelConstants.getXsdIntegerInstance());
        } else {
            if ("0".equals(call.getSignatureValue(0))) {
                return Set.of(OntologyModelConstants.getXsdIntegerInstance());
            }
            return Set.of(OntologyModelConstants.getXsdDecimalInstance());
        }
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        Set<OntResource> callInputType = call.resolvePreviousStep();
        OntologyValidationUtil.getInstance(call.getProject()).validateNumber(callInputType, holder, call);
        ArgumentValidator.validateNumberArgument(0, call, holder);

        if ("0".equals(call.getSignatureValue(0))) {
            holder.registerProblem(call.getCallSignatureArgumentElement(0),
                    UNNECESSARY_DECIMAL_PLACES_VALUE, ProblemHighlightType.WEAK_WARNING);
        }
        if (!callInputType.isEmpty() && callInputType.stream().allMatch(OntologyModelConstants.getXsdIntegerInstance()::equals)) {
            holder.registerProblem(call,
                    INPUT_IS_ALREADY_AN_INTEGER, ProblemHighlightType.WEAK_WARNING);
        }
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OntologyModelConstants.getXsdIntegerInstance());
        }
        return null;
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OntologyModelConstants.getXsdDecimalInstance());
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
