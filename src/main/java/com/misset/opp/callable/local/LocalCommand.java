package com.misset.opp.callable.local;

import com.misset.opp.callable.Callable;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public abstract class LocalCommand implements Callable {
    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public int maxNumberOfArguments() {
        return 0;
    }

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
    public String getType() {
        return "Local Command";
    }

    @Override
    public boolean canBeAppliedTo(Set<OntResource> resources) {
        return false;
    }
}
