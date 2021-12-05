package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.Call;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.Set;

/**
 * This method returns the subjects that contain a specific predicate or predicate+object info
 * It can also be called using a third GRAPHNAME argument but since this is unknown at compilation
 * time, that option gives the same response as when it's called without the GRAPHNAME.
 */
public class FindSubjectsOperator extends BuiltInOperator {
    private FindSubjectsOperator() {
    }

    public static final FindSubjectsOperator INSTANCE = new FindSubjectsOperator();

    @Override
    public String getName() {
        return "FIND_SUBJECTS";
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           Call call) {
        final int numberOfArguments = call.numberOfArguments();
        if (numberOfArguments == 1) {
            // only a predicate:
            return OppModel.INSTANCE.filterSubjects(resources, call.resolveSignatureArgument(0));
        } else if (numberOfArguments >= 2) {
            // predicate + object OR predicate + object + graphname (ignored)
            return OppModel.INSTANCE.filterSubjects(resources,
                    call.resolveSignatureArgument(0),
                    call.resolveSignatureArgument(1));
        }
        return Collections.emptySet();
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        validateNamedGraphArgument(2, call, holder);
    }
}
