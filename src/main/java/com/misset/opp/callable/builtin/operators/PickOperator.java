package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class PickOperator extends BuiltInCollectionOperator {
    private PickOperator() { }
    public static final PickOperator INSTANCE = new PickOperator();

    @Override
    protected String getShorthandSyntax() {
        return "[]";
    }

    @Override
    public String getName() {
        return "PICK";
    }

    @Override
    public int maxNumberOfArguments() {
        return -1;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        validateIntegerArgument(0, call, holder);
    }


    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModel.INSTANCE.XSD_INTEGER_INSTANCE);
        }
        return null;
    }
}
