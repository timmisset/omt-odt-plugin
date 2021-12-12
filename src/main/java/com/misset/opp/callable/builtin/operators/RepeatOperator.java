package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.Call;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.HashSet;
import java.util.Set;

public class RepeatOperator extends BuiltInOperator {
    private RepeatOperator() { }
    public static final RepeatOperator INSTANCE = new RepeatOperator();

    @Override
    public String getName() {
        return "REPEAT";
    }

    @Override
    public int maxNumberOfArguments() {
        return 3;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    /**
     * REPEAT operator returns the type of both the input and the resolved outcome of the first argument
     */
    @Override
    public Set<OntResource> resolveFrom(Set<OntResource> resources,
                                        Call call) {
        final HashSet<OntResource> ontResources = new HashSet<>(resources);
        ontResources.addAll(call.resolveSignatureArgument(0));
        return ontResources;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        validateNumberArgument(1, call, holder);
        validateNumberArgument(2, call, holder);
    }


    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 1 || index == 2) {
            return Set.of(OppModel.INSTANCE.XSD_NUMBER_INSTANCE);
        }
        return null;
    }
}
