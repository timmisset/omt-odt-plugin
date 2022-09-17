package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class LessThanOperator extends BuiltInBooleanOperator {
    private LessThanOperator() { }

    public static final LessThanOperator INSTANCE = new LessThanOperator();
    private static final List<String> PARAMETER_NAMES = List.of("left", "right");

    @Override
    public String getName() {
        return "LESS_THAN";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        Set<OntResource> resources = validateLeftRightCompatible(call, holder);
        Set<OntClass> acceptableTypes = Set.of(OntologyModelConstants.getXsdString(), OntologyModelConstants.getXsdNumber());
        OntologyValidationUtil.validateHasOntClass(resources, holder, call, acceptableTypes);
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        return Set.of(OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdNumberInstance());
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdNumberInstance());
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
