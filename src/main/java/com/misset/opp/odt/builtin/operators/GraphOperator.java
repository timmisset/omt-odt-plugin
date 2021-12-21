package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

public class GraphOperator extends BuiltInOperator {
    private GraphOperator() { }
    public static final GraphOperator INSTANCE = new GraphOperator();

    @Override
    public String getName() {
        return "GRAPH";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.NAMED_GRAPH;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateInstances(call.resolveCallInput(), holder, call);
    }
}
