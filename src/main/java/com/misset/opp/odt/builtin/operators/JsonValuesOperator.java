package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.Set;

public class JsonValuesOperator extends AbstractBuiltInOperator {
    private JsonValuesOperator() {
    }

    public static final JsonValuesOperator INSTANCE = new JsonValuesOperator();

    @Override
    public String getName() {
        return "JSON_VALUES";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Collections.singleton(OntologyModelConstants.getJsonObject());
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        OntologyValidationUtil.getInstance(call.getProject()).validateJSON(call.resolvePreviousStep(), holder, call);
    }

    @Override
    public OntResource resolveSingle() {
        return OntologyModelConstants.getOwlThingInstance();
    }
}
