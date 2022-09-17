package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.odt.builtin.operators.NotOperator;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.resolvable.querystep.ODTResolvableQualifiedUriStep;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Context;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class ODTResolvableQueryPathAbstract extends ODTResolvableQueryAbstract implements ODTQueryPath {
    protected ODTResolvableQueryPathAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Optional.ofNullable(getLastOperation())
                .map(ODTResolvable::resolve)
                .orElse(Collections.emptySet());
    }

    @Override
    public @NotNull Set<OntResource> resolve(Context context) {
        return Optional.ofNullable(getLastOperation())
                .map(lastStep -> lastStep.resolve(context))
                .orElse(Collections.emptySet());
    }

    private transient Set<OntResource> fromSet;

    /**
     * Resolves the query with a given input, the provided set is evaluated once,
     * after which the fromSet reset
     */
    @Override
    public Set<OntResource> resolveFromSet(Set<OntResource> fromSet) {
        this.fromSet = fromSet;
        final Set<OntResource> resolve = resolve();
        this.fromSet = null;
        return resolve;
    }

    @Override
    public Set<OntResource> getFromSet() {
        return fromSet;
    }

    private ODTQueryOperationStep getLastOperation() {
        if (getQueryOperationStepList().isEmpty()) {
            return null;
        }
        final ArrayList<ODTQueryOperationStep> operations = new ArrayList<>(getQueryOperationStepList());
        Collections.reverse(operations);
        return operations.get(0);
    }

    @Override
    public ODTQueryStep getStepMovingBackward(int index) {
        final ArrayList<ODTQueryOperationStep> operationSteps = new ArrayList<>(
                getQueryOperationStepList());
        Collections.reverse(operationSteps);
        return Optional.ofNullable(operationSteps.get(index)).map(ODTQueryOperationStep::getQueryStep).orElse(null);
    }

    @Override
    public Pair<Set<OntResource>, Property> resolveToSubjectPredicate() {
        if (getQueryOperationStepList().size() < 2) {
            return null;
        }

        ODTResolvable subject = getStepMovingBackward(1);
        final Set<OntResource> subjectResolved = subject != null ? subject.resolve() : Collections.emptySet();
        if (subjectResolved.isEmpty()) {
            return null;
        }

        ODTResolvable predicate = getStepMovingBackward(0);
        if (predicate instanceof ODTResolvableQualifiedUriStep) {
            final ODTResolvableQualifiedUriStep qualifiedUriStep = (ODTResolvableQualifiedUriStep) predicate;
            final String fullyQualifiedUri = qualifiedUriStep.getFullyQualifiedUri();
            final Property property = OntologyModel.getInstance().getProperty(fullyQualifiedUri);
            if (property != null) {
                return new Pair<>(subjectResolved, property);
            }
        }
        return null;
    }

    @Override
    public boolean isAbsolutePath() {
        return Optional.ofNullable(getFirstChild())
                .map(PsiElement::getNode)
                .map(ASTNode::getElementType)
                .map(ODTTypes.ROOT_INDICATOR::equals)
                .orElse(false);
    }

    @Override
    public Set<OntResource> filter(Set<OntResource> resources) {
        return Optional.ofNullable(getLastOperation())
                .map(ODTQueryOperationStep::getQueryStep)
                .map(odtQueryStep -> odtQueryStep.filter(resources))
                .orElse(resources); // by default, return the input
    }

    @Override
    public boolean isReference() {
        return getQueryOperationStepList().size() == 1 &&
                Optional.ofNullable(getLastOperation())
                        .map(ODTQueryOperationStep::getQueryStep)
                        .map(PsiElement::getReference)
                        .map(PsiReference::resolve)
                        .isPresent();
    }

    @Override
    public boolean requiresInput() {
        List<ODTQueryOperationStep> steps = getQueryOperationStepList();
        if (steps.isEmpty()) {
            return false;
        }
        ODTQueryOperationStep step = steps.get(0);
        ODTQueryStep queryStep = step.getQueryStep();

        boolean requiresInput;

        if (queryStep instanceof ODTNegatedStep) {
            requiresInput = ((ODTNegatedStep) queryStep).getQuery() == null;
        } else if (queryStep instanceof ODTOperatorCall) {
            Callable callable = ((ODTOperatorCall) queryStep).getCallable();
            if (callable instanceof NotOperator) {
                requiresInput = ((NotOperator) callable).requiresInput((ODTOperatorCall) queryStep);
            } else {
                requiresInput = callable.requiresInput();
            }
        } else if (queryStep instanceof ODTQueryReverseStep) {
            requiresInput = true;
        } else {
            requiresInput =
                    PsiUtilCore.getElementType(PsiTreeUtil.prevVisibleLeaf(step)) != ODTTypes.FORWARD_SLASH &&
                            queryStep instanceof ODTResolvableQualifiedUriStep ||
                            queryStep instanceof ODTIdentifierStep;
        }
        return requiresInput;
    }
}
