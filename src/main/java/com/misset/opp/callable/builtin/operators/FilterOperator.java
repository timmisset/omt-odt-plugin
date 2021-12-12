package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class FilterOperator extends BuiltInCollectionOperator {
    // todo:
    // implement a filter mechanism just like for the filter that is part of the queryStep

    private FilterOperator() {
    }

    public static final FilterOperator INSTANCE = new FilterOperator();

    @Override
    public String getName() {
        return "FILTER";
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        validateBooleanArgument(0, call, holder);
    }

    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        return Set.of(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE);
    }
}
