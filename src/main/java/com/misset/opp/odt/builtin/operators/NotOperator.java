package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTResolvableValue;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.odt.psi.resolvable.call.ODTCall;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class NotOperator extends BuiltInBooleanOperator {
    private NotOperator() { }

    public static final NotOperator INSTANCE = new NotOperator();
    private static final List<String> PARAMETER_NAMES = List.of("expression");

    @Override
    public String getName() {
        return "NOT";
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
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        OntologyValidationUtil.validateBoolean(call.resolvePreviousStep(), holder, call);
        ArgumentValidator.validateBooleanArgument(0, call, holder);
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OntologyModelConstants.getXsdBooleanInstance());
        }
        return null;
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OntologyModelConstants.getXsdBooleanInstance());
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }

    public boolean requiresInput(ODTCall call) {
        return Optional.ofNullable(call.getSignatureArgument(0))
                .map(ODTSignatureArgument::getResolvableValue)
                .map(ODTResolvableValue::getQuery)
                .map(ODTQuery::requiresInput)
                .orElse(requiresInput());
    }
}
