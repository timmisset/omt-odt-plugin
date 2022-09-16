package com.misset.opp.ttl.psi.spo;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.psi.*;
import com.misset.opp.ttl.psi.iri.TTLIriHolder;
import com.misset.opp.ttl.stubs.object.TTLObjectStub;
import com.misset.opp.util.Icons;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The SHACL model has:
 * - sh:path OBJECT <-- this is the predicate in the simplified model
 * - sh:class / sh:datatype OBJECT <-- this is the object in the simplified model
 */
public abstract class TTLBaseObject extends StubBasedPsiElementBase<TTLObjectStub> implements TTLObject {
    protected TTLBaseObject(@NotNull TTLObjectStub stub, @NotNull IStubElementType<?, ?> nodeType) {
        super(stub, nodeType);
    }

    protected TTLBaseObject(@NotNull ASTNode node) {
        super(node);
    }

    protected TTLBaseObject(TTLObjectStub stub, IElementType nodeType, ASTNode node) {
        super(stub, nodeType, node);
    }

    public String getQualifiedUri() {
        return Optional.ofNullable(getIri()).map(TTLIriHolder::getQualifiedUri).orElse(null);
    }

    @Override
    public ItemPresentation getPresentation() {
        return new ItemPresentation() {
            @Override
            public @NlsSafe @Nullable String getPresentableText() {
                String subjectIri = getSubjectIri();
                if (subjectIri == null) {
                    return null;
                }
                Resource resource = ResourceFactory.createResource(subjectIri);
                return getText() + "(" + resource.getLocalName() + ")";
            }

            @Override
            public @Nullable Icon getIcon(boolean unused) {
                return Icons.TTLFile;
            }
        };
    }

    @Override
    public boolean isPredicate() {
        return getFromStubOrPsi(TTLObjectStub::isPredicate,
                () -> hasShaclPredicate(OppModelConstants.getShaclPath().getURI()));
    }

    @Override
    public String getSubjectIri() {
        return getFromStubOrPsi(TTLObjectStub::getSubjectIri,
                () -> Optional.ofNullable(PsiTreeUtil.getTopmostParentOfType(this, TTLStatement.class))
                        .map(ttlStatement -> PsiTreeUtil.findChildOfType(ttlStatement, TTLSubject.class))
                        .map(TTLSPO::getQualifiedUri)
                        .orElse(null));
    }

    private <T> T getFromStubOrPsi(Function<TTLObjectStub, T> fromStub, Supplier<T> fromPsi) {
        if (getStub() != null) {
            return fromStub.apply(getStub());
        } else {
            return fromPsi.get();
        }
    }

    @Override
    public boolean isObjectClass() {
        return getFromStubOrPsi(TTLObjectStub::isPredicate,
                () -> hasShaclPredicate(OppModelConstants.getShaclClass().getURI(),
                        OppModelConstants.getRdfsSubclassOf().getURI(),
                        OppModelConstants.getRdfType().getURI()));
    }

    private boolean hasShaclPredicate(String... iri) {
        return Optional.ofNullable(PsiTreeUtil.getParentOfType(this, TTLPredicateObject.class))
                .map(TTLPredicateObject::getVerb)
                .map(this::getUri)
                .map(s -> Arrays.asList(iri).contains(s))
                .orElse(false);
    }

    private String getUri(TTLVerb verb) {
        if (verb.getPredicate() != null) {
            return verb.getPredicate().getIri().getQualifiedUri();
        } else {
            if (PsiUtilCore.getElementType(verb.getFirstChild()) == TTLTypes.A) {
                return OppModelConstants.getRdfType().getURI();
            }
        }
        return null;
    }
}
