package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.List;
import java.util.Set;

public class SubstringOperator extends BuiltInStringOperator {
    private SubstringOperator() {
    }

    public static final SubstringOperator INSTANCE = new SubstringOperator();
    private static final List<String> PARAMETER_NAMES = List.of("begin", "end");

    @Override
    public String getName() {
        return "SUBSTRING";
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
        TTLValidationUtil.validateString(call.resolvePreviousStep(), holder, call);
        validateAllArguments(call, holder, this::validateIntegerArgument);
    }

    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        return Set.of(OppModelConstants.XSD_INTEGER_INSTANCE);
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModelConstants.XSD_STRING_INSTANCE);
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
