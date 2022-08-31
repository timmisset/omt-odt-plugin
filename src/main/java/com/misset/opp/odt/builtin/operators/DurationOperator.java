package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class DurationOperator extends AbstractBuiltInOperator {
    private DurationOperator() {
    }

    public static final DurationOperator INSTANCE = new DurationOperator();
    private static final List<String> PARAMETER_NAMES = List.of("value", "unit");

    @Override
    public String getName() {
        return "DURATION";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModelConstants.getXsdDurationInstance();
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        ArgumentValidator.validateNumberArgument(0, call, holder);
        ArgumentValidator.validateStringArgument(1, call, holder);
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModelConstants.getXsdNumberInstance());
        } else if (index == 1) {
            return Set.of(OppModelConstants.getXsdStringInstance());
        }
        return null;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
