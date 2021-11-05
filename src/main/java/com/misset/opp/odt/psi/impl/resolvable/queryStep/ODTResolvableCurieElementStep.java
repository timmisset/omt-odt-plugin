package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTCurieElement;
import com.misset.opp.odt.psi.impl.ODTNamespacePrefixImpl;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;

public abstract class ODTResolvableCurieElementStep extends ODTResolvableQueryStep implements ODTCurieElement {
    public ODTResolvableCurieElementStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public Set<OntResource> calculate() {
        final OppModel model = OppModel.INSTANCE;
        if (isRootStep()) {
            // when the path start with a root curie, resolve the curie and return it:
            return Set.of(model.getClass(getFullyQualified()));
        } else {
            // resolve the previous step and use the current curie to traverse the model
            final Property property = model.createProperty(getFullyQualified());
            return model.listObjects(resolvePreviousStep(), property);
        }
    }

    public String getFullyQualified() {
        final ODTNamespacePrefixImpl namespacePrefix = (ODTNamespacePrefixImpl) getNamespacePrefix();
        final String fullyQualified;
        if (namespacePrefix != null) {
            fullyQualified = namespacePrefix.getFullyQualified(
                    Optional.ofNullable(PsiTreeUtil.nextVisibleLeaf(namespacePrefix))
                            .map(PsiElement::getText)
                            .orElse(""));
        } else {
            fullyQualified = getText().substring(1, getTextLength() - 1);
        }
        return fullyQualified;
    }
}
