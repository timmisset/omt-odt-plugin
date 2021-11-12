package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.builtin.Builtin;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import org.apache.jena.ontology.OntResource;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BuiltInCommand extends Builtin {
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
     */
    protected Set<OntResource> resolveFrom(ODTCall call) {
        return resolve();
    }

    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           ODTCall call) {
        return resolveFrom(call);
    }

    protected Set<OntResource> combineArgumentResources(ODTCall call) {
        return call.getSignatureArguments().stream()
                .map(ODTSignatureArgument::resolve)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
