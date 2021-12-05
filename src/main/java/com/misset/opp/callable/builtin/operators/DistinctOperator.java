package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.List;
import java.util.Set;

public class DistinctOperator extends BuiltInCollectionOperator {
    private DistinctOperator() { }
    public static final DistinctOperator INSTANCE = new DistinctOperator();

    @Override
    public String getName() {
        return "DISTINCT";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public List<String> getFlags() {
        return IGNORE_CASE_FLAG;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        validateIgnoreCaseFlagUsage(1, call, holder);
    }
}
