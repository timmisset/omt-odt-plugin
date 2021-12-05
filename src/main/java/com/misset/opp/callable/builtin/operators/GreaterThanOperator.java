package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class GreaterThanOperator extends BuiltInBooleanOperator {
    private GreaterThanOperator() { }
    public static final GreaterThanOperator INSTANCE = new GreaterThanOperator();

    @Override
    public String getName() {
        return "GREATER_THAN";
    }

    @Override
    protected String getShorthandSyntax() {
        return ">";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        Set<OntResource> resources = validateLeftRightCompatible(call, holder);
        Set<OntClass> acceptableTypes = Set.of(OppModel.INSTANCE.XSD_STRING, OppModel.INSTANCE.XSD_NUMBER);
        TTLValidationUtil.validateHasOntClass(resources, holder, call, acceptableTypes);
    }

}
