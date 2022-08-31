package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class GreaterThanOperator extends BuiltInBooleanOperator {
    private GreaterThanOperator() { }

    public static final GreaterThanOperator INSTANCE = new GreaterThanOperator();
    private static final List<String> PARAMETER_NAMES = List.of("left", "right");

    @Override
    public String getName() {
        return "GREATER_THAN";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        Set<OntResource> resources = validateLeftRightCompatible(call, holder);
        Set<OntClass> acceptableTypes = Set.of(OppModelConstants.getXsdString(), OppModelConstants.getXsdNumber());
        TTLValidationUtil.validateHasOntClass(resources, holder, call, acceptableTypes);
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        return Set.of(OppModelConstants.getXsdStringInstance(), OppModelConstants.getXsdNumberInstance());
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModelConstants.getXsdStringInstance(), OppModelConstants.getXsdNumberInstance());
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
