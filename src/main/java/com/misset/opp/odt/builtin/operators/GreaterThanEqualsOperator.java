package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;

import java.util.List;
import java.util.Set;

public class GreaterThanEqualsOperator extends BuiltInBooleanOperator {
    private GreaterThanEqualsOperator() { }

    public static final GreaterThanEqualsOperator INSTANCE = new GreaterThanEqualsOperator();
    private static final List<String> PARAMETER_NAMES = List.of("left", "right");

    @Override
    public String getName() {
        return "GREATER_THAN_EQUALS";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        Set<OntResource> resources = validateLeftRightCompatible(call, holder);
        Set<OntClass> acceptableTypes = Set.of(OppModelConstants.XSD_STRING, OppModelConstants.XSD_NUMBER);
        TTLValidationUtil.validateHasOntClass(resources, holder, call, acceptableTypes);
    }

    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        return Set.of(OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE);
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModelConstants.XSD_STRING_INSTANCE, OppModelConstants.XSD_NUMBER_INSTANCE);
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
