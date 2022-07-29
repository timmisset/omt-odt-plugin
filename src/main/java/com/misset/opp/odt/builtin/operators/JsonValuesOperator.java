package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.Set;

public class JsonValuesOperator extends BuiltInOperator {
    private JsonValuesOperator() {
    }

    public static final JsonValuesOperator INSTANCE = new JsonValuesOperator();

    @Override
    public String getName() {
        return "JSONVALUES";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Collections.singleton(OppModelConstants.JSON_OBJECT);
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateJSON(call.resolvePreviousStep(), holder, call);
    }

    @Override
    public OntResource resolveSingle() {
        return OppModelConstants.OWL_THING_INSTANCE;
    }
}
