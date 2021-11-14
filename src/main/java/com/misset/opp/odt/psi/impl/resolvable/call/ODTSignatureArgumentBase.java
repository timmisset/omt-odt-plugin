package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.misset.opp.callable.Resolvable;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.odt.psi.impl.ODTASTWrapperPsiElement;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public abstract class ODTSignatureArgumentBase extends ODTASTWrapperPsiElement implements ODTSignatureArgument {
    public ODTSignatureArgumentBase(@NotNull ASTNode node) {
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

    @Override
    public void inspect(ProblemsHolder holder) {
        /* Inspection is performed on the content of the Signature argument */
    }

    @Override
    public void annotate(AnnotationHolder holder) {
        /* Annotation is performed on the content of the Signature argument */
    }
}
