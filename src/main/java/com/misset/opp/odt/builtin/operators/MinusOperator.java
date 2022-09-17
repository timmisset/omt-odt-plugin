package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class MinusOperator extends BuiltinMathOperator {
    private MinusOperator() { }

    public static final MinusOperator INSTANCE = new MinusOperator();
    private static final List<String> PARAMETER_NAMES = List.of("value", "subtraction");

    @Override
    public String getName() {
        return "MINUS";
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
            OntologyValidationUtil.validateRequiredTypes(validInputs, call.resolvePreviousStep(), holder, call);
        }
        ArgumentValidator.validateAllArguments(call, holder, validator);
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        return validInputs;
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return validInputs;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
