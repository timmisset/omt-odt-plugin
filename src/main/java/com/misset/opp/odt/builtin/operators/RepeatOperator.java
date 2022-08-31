package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RepeatOperator extends AbstractBuiltInOperator {
    private RepeatOperator() {
    }

    public static final RepeatOperator INSTANCE = new RepeatOperator();
    private static final List<String> PARAMETER_NAMES = List.of("query", "skip", "take");

    @Override
    public String getName() {
        return "REPEAT";
    }

    @Override
    public int maxNumberOfArguments() {
        return 3;
    }

    /**
     * REPEAT operator returns the type of both the input and the resolved outcome of the first argument
     */
    @Override
    public Set<OntResource> resolveFrom(Set<OntResource> resources,
                                        PsiCall call) {
        final HashSet<OntResource> ontResources = new HashSet<>(resources);
        ontResources.addAll(call.resolveSignatureArgument(0));
        return ontResources;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        ArgumentValidator.validateNumberArgument(1, call, holder);
        ArgumentValidator.validateNumberArgument(2, call, holder);
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 1 || index == 2) {
            return Set.of(OppModelConstants.getXsdNumberInstance());
        }
        return null;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
