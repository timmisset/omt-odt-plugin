package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;
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
    private static final List<String> PARAMETER_NAMES = List.of("predicates", "objects", "graphName");

    @Override
    public String getName() {
        return "FIND_SUBJECTS";
    }

    @Override
    public int maxNumberOfArguments() {
        return 3;
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           PsiCall call) {
        final int numberOfArguments = call.getNumberOfArguments();
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

    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 2) {
            return Set.of(OppModelConstants.NAMED_GRAPH);
        }
        return null;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
