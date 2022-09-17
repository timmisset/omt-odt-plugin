package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class AndOperator extends BuiltInBooleanOperator {
    private AndOperator() { }

    public static final AndOperator INSTANCE = new AndOperator();
    private static final List<String> PARAMETER_NAMES = List.of("boolean", "boolean");

    @Override
    public String getName() {
        return "AND";
    }

    @Override
    public int maxNumberOfArguments() {
        return -1;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        validateSingleArgumentInputBoolean(call, holder);
        ArgumentValidator.validateAllArguments(call, holder, ArgumentValidator::validateBooleanArgument);
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
