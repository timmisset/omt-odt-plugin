package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTConstantValue;
import com.misset.opp.odt.psi.ODTQueryStep;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.odt.psi.impl.resolvable.ODTBaseResolvable;
import com.misset.opp.odt.psi.impl.resolvable.ODTTypeFilterProvider;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Resolvable;
import com.misset.opp.ttl.model.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public abstract class ODTResolvableSignatureArgument extends ODTBaseResolvable
        implements ODTSignatureArgument, ODTTypeFilterProvider {
    protected ODTResolvableSignatureArgument(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Optional.ofNullable(getResolvableValue())
                .map(Resolvable::resolve)
                .or(this::resolveCommandBlock)
                .orElse(Collections.emptySet());
    }

    private Optional<Set<OntResource>> resolveCommandBlock() {
        // todo: try to resolve the command block
        return Optional.of(Collections.emptySet());
    }

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
            return ODTTypeFilterProvider.ACCEPT_ALL;
        }

        Callable callable = call.getCallable();
        if (callable == null) {
            return ODTTypeFilterProvider.ACCEPT_ALL;
        }

        int argumentIndexOf = call.getArgumentIndexOf(element);
        Set<OntResource> acceptableArgumentType = callable.getAcceptableArgumentType(argumentIndexOf, call);

        return resources -> resources.isEmpty() || OppModel.getInstance().areCompatible(acceptableArgumentType, resources);
    }
}
