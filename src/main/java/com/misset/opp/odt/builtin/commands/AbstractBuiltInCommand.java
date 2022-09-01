package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.odt.builtin.AbstractBuiltin;
import com.misset.opp.resolvable.CallableType;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractBuiltInCommand extends AbstractBuiltin {

    @Override
    public boolean isCommand() {
        return true;
    }

    /**
     * Returns the callId which is the name prefixed with an '@' to indicate that it must be called as such
     */
    public String getCallId() {
        return "@" + getName();
    }

    @Override
    public boolean isVoid() {
        return true;
    }

    @Override
    protected Set<OntResource> resolveFrom(PsiCall call) {
        return resolve();
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           PsiCall call) {
        return resolveFrom(call);
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources) {
        return resolve();
    }

    @Override
    protected Set<OntResource> resolveError(Set<OntResource> resources, PsiCall call) {
        return resources;
    }

    @Override
    protected OntResource resolveSingle() {
        return null;
    }

    protected Set<OntResource> combineArgumentResources(PsiCall call) {
        return call.resolveSignatureArguments()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public CallableType getType() {
        return CallableType.BUILTIN_COMMAND;
    }

    @Override
    public boolean canBeAppliedTo(Set<OntResource> resources) {
        return false;
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        // do nothing if not overridden in implementation
    }
}
