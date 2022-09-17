package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

public class GraphOperator extends AbstractBuiltInOperator {
    private GraphOperator() {
    }

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
        return OntologyModelConstants.getNamedGraph();
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        OntologyValidationUtil.validateInstances(call.resolvePreviousStep(), holder, call);
    }
}
