package com.misset.opp.odt.psi.impl.callable;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.misset.opp.callable.Call;
import com.misset.opp.odt.psi.ODTDefineQueryStatement;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public abstract class ODTBaseDefineQueryStatement extends ODTDefineStatement implements ODTDefineQueryStatement, ODTResolvable {
    public ODTBaseDefineQueryStatement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public boolean isCommand() {
        return false;
    }

    @Override
    public String getCallId() {
        return getName();
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return getQuery().resolve();
    }

    @Override
    public Set<OntResource> resolve(Set<OntResource> resources,
                                    Call call) {
        decorateCall(call);
        return getQuery().resolve(resources, call);
    }

    @Override
    public void inspect(ProblemsHolder holder) {

    }

    @Override
    public void annotate(AnnotationHolder holder) {
        // inspect SEMICOLON endings
        // these are optional in the lexer to be more lenient when writing the query statement
        if (getLastChild().getNode().getElementType() != ODTTypes.SEMICOLON) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Missing semicolon").create();
        }
    }
}
