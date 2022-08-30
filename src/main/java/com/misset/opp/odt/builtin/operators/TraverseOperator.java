package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class TraverseOperator extends AbstractBuiltInOperator {
    private TraverseOperator() {
    }

    public static final TraverseOperator INSTANCE = new TraverseOperator();
    private static final List<String> PARAMETER_NAMES = List.of("predicate", "backwards");

    @Override
    public String getName() {
        return "TRAVERSE";
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
    public OntResource resolveSingle() {
        return OppModelConstants.OWL_THING_INSTANCE;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        ArgumentValidator.validateStringArgument(0, call, holder);
        ArgumentValidator.validateBooleanArgument(1, call, holder);
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModelConstants.XSD_STRING_INSTANCE);
        } else if (index == 1) {
            return Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE);
        }
        return null;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
