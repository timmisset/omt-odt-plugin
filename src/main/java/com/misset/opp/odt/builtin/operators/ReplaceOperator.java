package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.List;
import java.util.Set;

public class ReplaceOperator extends BuiltInStringOperator {
    private ReplaceOperator() { }

    public static final ReplaceOperator INSTANCE = new ReplaceOperator();
    private static final List<String> PARAMETER_NAMES = List.of("find", "replaceWith");

    @Override
    public String getName() {
        return "REPLACE";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateString(call.resolvePreviousStep(), holder, call);
        validateAllArguments(call, holder, this::validateStringArgument);
    }

    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        return Set.of(OppModelConstants.XSD_STRING_INSTANCE);
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
