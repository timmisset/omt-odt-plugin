package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class DurationOperator extends BuiltInOperator {
    private DurationOperator() {
    }

    public static final DurationOperator INSTANCE = new DurationOperator();

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
        return OppModelConstants.XSD_DURATION_INSTANCE;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        validateNumberArgument(0, call, holder);
        validateStringArgument(1, call, holder);
    }

    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModelConstants.XSD_NUMBER_INSTANCE);
        } else if (index == 1) {
            return Set.of(OppModelConstants.XSD_STRING_INSTANCE);
        }
        return null;
    }
}
