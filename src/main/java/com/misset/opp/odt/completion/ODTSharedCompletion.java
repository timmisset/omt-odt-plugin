package com.misset.opp.odt.completion;

import com.intellij.openapi.util.Key;
import com.intellij.util.SharedProcessingContext;
import com.misset.opp.resolvable.Callable;
import org.apache.jena.ontology.OntResource;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

/**
 * Manages shared context to be exchanged between completion providers
 */
public class ODTSharedCompletion {
    /**
     * Type-filter to filter to completions
     */
    public static final Key<Predicate<Set<OntResource>>> TYPE_FILTER = new Key<>("TYPE_FILTER");
    /**
     * Type-filter to filter callables by type (Operators/Commands);
     */
    public static final Key<Predicate<Callable>> CALLABLE_FILTER = new Key<>("CALLABLE_FILTER");
    public static final AtomicReference<SharedProcessingContext> sharedContext = new AtomicReference<>();

    private ODTSharedCompletion() {
        // empty constructor
    }
}
