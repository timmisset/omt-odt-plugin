package com.misset.opp.odt.psi.impl.resolvable.querystep.traverse;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.misset.opp.odt.psi.ODTSchemalessIriStep;
import com.misset.opp.ttl.model.OppModel;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The Schemaless or default: schema step uses the subject's namespace/schema to traverse the graph
 * This means that when:
 * /ont:ClassA / ^rdf:type / <somePredicate>
 * The subject is resolved to instances of http://ontology/classA and the predicate to http://ontology/somePredicate
 */
public abstract class ODTResolvableSchemalessIriStep extends ODTResolvableQueryForwardStep implements ODTSchemalessIriStep {
    protected ODTResolvableSchemalessIriStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String calculateFullyQualifiedUri() {
        // we require the preceding step to resolve the current IRI
        String localName = getText();
        return resolvePreviousStep().stream()
                .map(OppModel.getInstance()::toClass)
                .filter(Objects::nonNull)
                .map(Resource::getNameSpace)
                .map(nameSpace -> nameSpace + localName.substring(1, localName.length() - 1))
                // if more than 1 namespace is discovered, an Inspection should indicate this.
                // for traversing the graph, just use the first one discovered
                .findFirst()
                .orElse(null);
    }

    @Override
    protected TextRange getModelReferenceTextRange() {
        return TextRange.create(1, getTextLength() - 1);
    }
}
