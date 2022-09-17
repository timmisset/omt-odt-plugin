package com.misset.opp.ttl.psi.impl.spo;

import com.intellij.extapi.psi.StubBasedPsiElementBase;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.stubs.IStubElementType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.ttl.psi.*;
import com.misset.opp.ttl.psi.extend.TTLQualifiedIriResolver;
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
public abstract class TTLObjectAbstract extends StubBasedPsiElementBase<TTLObjectStub> implements TTLStubBasedObject, TTLObject {
    protected TTLObjectAbstract(@NotNull TTLObjectStub stub, @NotNull IStubElementType<?, ?> nodeType) {
        super(stub, nodeType);
    }

    protected TTLObjectAbstract(@NotNull ASTNode node) {
        super(node);
    }

    protected TTLObjectAbstract(TTLObjectStub stub, IElementType nodeType, ASTNode node) {
        super(stub, nodeType, node);
    }

    @Override
    public String getQualifiedIri() {
        return Optional.ofNullable(getIri()).map(TTLQualifiedIriResolver::getQualifiedIri).orElse(null);
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
                () -> hasShaclPredicate(OntologyModelConstants.getShaclPath().getURI()));
    }

    @Override
    public String getSubjectIri() {
        return getFromStubOrPsi(TTLObjectStub::getSubjectIri,
                () -> Optional.ofNullable(PsiTreeUtil.getTopmostParentOfType(this, TTLStatement.class))
                        .map(ttlStatement -> PsiTreeUtil.findChildOfType(ttlStatement, TTLSubject.class))
                        .map(TTLSubject::getQualifiedIri)
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
    public boolean isClass() {
        return getFromStubOrPsi(TTLObjectStub::isPredicate,
                () -> hasShaclPredicate(OntologyModelConstants.getShaclClass().getURI(),
                        OntologyModelConstants.getRdfsSubclassOf().getURI(),
                        OntologyModelConstants.getRdfType().getURI()));
    }

    private boolean hasShaclPredicate(String... iri) {
        return Optional.ofNullable(PsiTreeUtil.getParentOfType(this, TTLPredicateObject.class))
                .map(TTLPredicateObject::getVerb)
                .map(this::getUri)
                .map(s -> Arrays.asList(iri).contains(s))
                .orElse(false);
    }

    private String getUri(TTLVerb verb) {
        if (verb instanceof TTLPredicate) {
            return ((TTLPredicate) verb).getIri().getQualifiedIri();
        } else {
            if (verb != null && PsiUtilCore.getElementType(verb.getFirstChild()) == TTLTypes.A) {
                return OntologyModelConstants.getRdfType().getURI();
            }
        }
        return null;
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return null;
    }
}
