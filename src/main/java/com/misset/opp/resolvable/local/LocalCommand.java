package com.misset.opp.resolvable.local;

import com.misset.opp.resolvable.Callable;
import org.apache.jena.ontology.OntResource;

import java.util.HashMap;
import java.util.Map;
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

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public HashMap<Integer, Set<OntResource>> getParameterTypes() {
        return new HashMap<>();
    }

    @Override
    public Map<Integer, String> getParameterNames() {
        return new HashMap<>();
    }
}
