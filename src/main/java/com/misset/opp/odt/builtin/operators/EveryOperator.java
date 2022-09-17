package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class EveryOperator extends BuiltInBooleanOperator {
    private EveryOperator() {
    }

    public static final EveryOperator INSTANCE = new EveryOperator();
    private static final List<String> PARAMETER_NAMES = List.of("predicate");

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
            OntologyValidationUtil.validateBoolean(call.resolvePreviousStep(), holder, call);
        } else {
            ArgumentValidator.validateBooleanArgument(0, call, holder);
        }
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        return Set.of(OntologyModelConstants.getXsdBooleanInstance());
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OntologyModelConstants.getXsdBooleanInstance());
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
