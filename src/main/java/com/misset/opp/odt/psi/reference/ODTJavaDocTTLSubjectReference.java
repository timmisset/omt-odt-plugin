package com.misset.opp.odt.psi.reference;

import com.intellij.codeInsight.daemon.EmptyResolveMessageProvider;
import com.intellij.codeInspection.util.InspectionMessage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.stubs.StubIndex;
import com.misset.opp.odt.documentation.ODTDocumentationUtil;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.psi.TTLSubject;
import com.misset.opp.ttl.stubs.index.TTLSubjectStubIndex;
import com.misset.opp.ttl.util.TTLScopeUtil;
import com.misset.opp.util.LoggerUtil;
import org.apache.jena.ontology.OntClass;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ODTJavaDocTTLSubjectReference extends PsiReferenceBase.Poly<PsiDocTag> implements
        PsiPolyVariantReference,
        EmptyResolveMessageProvider {
    private final int position;
    private static final Logger LOGGER = Logger.getInstance(ODTJavaDocTTLSubjectReference.class);

    public ODTJavaDocTTLSubjectReference(PsiDocTag element, TextRange textRange, int position) {
        super(element, textRange, false);
        this.position = position;
    }

    public OntClass getClassFromModel() {
        return ODTDocumentationUtil.getTypeFromDocTag(getElement(), position)
                .stream()
                .map(OppModel.INSTANCE::toClass)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return LoggerUtil.computeWithLogger(LOGGER, "Resolving ODTJavaDocTTLSubjectReference", () -> {
            PsiDocTag element = getElement();
            OntClass classFromModel = getClassFromModel();
            if (classFromModel == null) {
                return ResolveResult.EMPTY_ARRAY;
            } else {
                return StubIndex.getElements(
                                TTLSubjectStubIndex.KEY,
                                classFromModel.getURI(),
                                element.getProject(),
                                TTLScopeUtil.getModelSearchScope(myElement.getProject()),
                                TTLSubject.class
                        ).stream()
                        .map(PsiElementResolveResult::new)
                        .toArray(ResolveResult[]::new);
            }
        });
    }


    @Override
    public @InspectionMessage @NotNull String getUnresolvedMessagePattern() {
        return "Cannot resolve type to ontology class / type";
    }
}
