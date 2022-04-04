package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.Builtin;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BuiltInCommand extends Builtin {

    protected static final String TYPE = "Builtin Command";

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

    /**
     * For CommandCalls, many of the commands do not take input parameters
     * Instead, resolve only the call arguments
     *
     * @param call
     */
    protected Set<OntResource> resolveFrom(PsiCall call) {
        return resolve();
    }

    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           PsiCall call) {
        return resolveFrom(call);
    }

    protected Set<OntResource> combineArgumentResources(PsiCall call) {
        return call.resolveSignatureArguments()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean canBeAppliedTo(Set<OntResource> resources) {
        return false;
    }

    @Override
    public boolean isStatic() {
        return true;
    }
}
