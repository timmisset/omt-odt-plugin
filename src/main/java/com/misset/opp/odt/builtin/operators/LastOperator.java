package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class LastOperator extends AbstractBuiltInOperator {
    private LastOperator() {
    }

    public static final LastOperator INSTANCE = new LastOperator();
    private static final List<String> PARAMETER_NAMES = List.of("include");

    @Override
    public String getName() {
        return "LAST";
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
    protected Set<OntResource> resolveFrom(Set<OntResource> resources) {
        return resources;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
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
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
