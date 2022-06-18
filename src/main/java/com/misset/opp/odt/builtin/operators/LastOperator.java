package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;

import java.util.List;
import java.util.Set;

public class LastOperator extends BuiltInOperator {
    private LastOperator() { }

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
        validateBooleanArgument(0, call, holder);
    }

    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE);
        }
        return null;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
