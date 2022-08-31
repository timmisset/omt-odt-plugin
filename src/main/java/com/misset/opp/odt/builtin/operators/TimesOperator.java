package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class TimesOperator extends BuiltinMathOperator {
    private TimesOperator() {
    }

    public static final TimesOperator INSTANCE = new TimesOperator();
    private static final List<String> PARAMETER_NAMES = List.of("value", "multiplier");

    @Override
    public String getName() {
        return "TIMES";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        if (call.getNumberOfArguments() == 1) {
            TTLValidationUtil.validateNumber(call.resolvePreviousStep(), holder, call);
        }
        ArgumentValidator.validateAllArguments(call, holder, ArgumentValidator::validateNumberArgument);
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        return Set.of(OppModelConstants.getXsdNumberInstance());
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModelConstants.getXsdNumberInstance());
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
