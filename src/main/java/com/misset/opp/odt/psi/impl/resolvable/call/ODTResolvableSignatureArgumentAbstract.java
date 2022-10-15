package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvableAbstract;
import com.misset.opp.odt.psi.resolvable.call.ODTCall;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Resolvable;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Literal;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

public abstract class ODTResolvableSignatureArgumentAbstract extends ODTResolvableAbstract
        implements ODTSignatureArgument, ODTTypeFilterProvider {

    private static final Predicate<Set<OntResource>> ACCEPT_NON_VOID =
            resources -> !resources.contains(OntologyModelConstants.getVoidResponse());

    protected ODTResolvableSignatureArgumentAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Optional.ofNullable(getResolvableValue())
                .map(Resolvable::resolve)
                .or(this::resolveCommandBlock)
                .orElse(Collections.emptySet());
    }

    @Override
    public @NotNull List<Literal> resolveLiteral() {
        return Optional.ofNullable(getResolvableValue())
                .map(Resolvable::resolveLiteral)
                .orElse(Collections.emptyList());
    }

    private Optional<Set<OntResource>> resolveCommandBlock() {
        return Optional.ofNullable(getCommandBlock())
                .map(ODTCommandBlock::getScript)
                .map(Resolvable::resolve);
    }

    @Override
    public boolean isPrimitiveArgument() {
        final Collection<ODTQueryStep> childrenOfType = PsiTreeUtil.findChildrenOfType(this, ODTQueryStep.class);
        return childrenOfType.size() == 1 && childrenOfType.stream().allMatch(ODTConstantValue.class::isInstance);
    }

    @Override
    public void inspect(ProblemsHolder holder) {
        /* Inspection is performed on the content of the Signature argument */
    }

    @Override
    public Predicate<Set<OntResource>> getTypeFilter(PsiElement element) {
        ODTCall call = PsiTreeUtil.getParentOfType(element, ODTCall.class, true);
        if (call == null) {
            return ACCEPT_NON_VOID;
        }

        Callable callable = call.getCallable();
        if (callable == null) {
            return ACCEPT_NON_VOID;
        }

        int argumentIndexOf = call.getArgumentIndexOf(element);
        Set<OntResource> acceptableArgumentType = callable.getAcceptableArgumentType(argumentIndexOf, call);

        return ACCEPT_NON_VOID.and(
                resources -> resources.isEmpty() || OntologyModel.getInstance(getProject()).areCompatible(acceptableArgumentType, resources)
        );
    }
}
