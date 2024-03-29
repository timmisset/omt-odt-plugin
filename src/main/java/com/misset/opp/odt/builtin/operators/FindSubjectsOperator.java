package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * This method returns the subjects that contain a specific predicate or predicate+object info
 * It can also be called using a third GRAPHNAME argument but since this is unknown at compilation
 * time, that option gives the same response as when it's called without the GRAPHNAME.
 */
public class FindSubjectsOperator extends AbstractBuiltInOperator {
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
            return OntologyModel.getInstance(call.getProject()).filterSubjects(resources, call.resolveSignatureArgument(0));
        } else if (numberOfArguments >= 2) {
            // predicate + object OR predicate + object + graphname (ignored)
            return OntologyModel.getInstance(call.getProject()).filterSubjects(resources,
                    call.resolveSignatureArgument(0),
                    call.resolveSignatureArgument(1));
        }
        return Collections.emptySet();
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        ArgumentValidator.validateNamedGraphArgument(2, call, holder);
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 2) {
            return Set.of(OntologyModelConstants.getNamedGraph());
        }
        return null;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
