package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.Call;
import com.misset.opp.callable.builtin.Builtin;
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
    protected Set<OntResource> resolveFrom(Call call) {
        return resolve();
    }

    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           Call call) {
        return resolveFrom(call);
    }

    protected Set<OntResource> combineArgumentResources(Call call) {
        return call.resolveSignatureArguments()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }
}
