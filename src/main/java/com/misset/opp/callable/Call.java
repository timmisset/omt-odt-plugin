package com.misset.opp.callable;

import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface Call {

    /**
     * Returns the resolved OntResource set at the given index
     * Does not throw out-of-bounds exception but always returns an empty set when index is not present
     */
    @NotNull
    Set<OntResource> resolveSignatureArgument(int index);

    List<Set<OntResource>> resolveSignatureArguments();

    @Nullable
    String getSignatureValue(int index);

    List<String> getSignatureValues();

    @Nullable
    String getFlag();

    int numberOfArguments();

}
