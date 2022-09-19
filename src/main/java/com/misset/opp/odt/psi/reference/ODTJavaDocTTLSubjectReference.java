package com.misset.opp.odt.psi.reference;

import com.intellij.codeInsight.daemon.EmptyResolveMessageProvider;
import com.intellij.codeInspection.util.InspectionMessage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.impl.PsiJavaParserFacadeImpl;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.util.OntologyScopeUtil;
import com.misset.opp.odt.documentation.ODTDocumentationUtil;
import com.misset.opp.ttl.psi.TTLLocalname;
import com.misset.opp.ttl.psi.TTLSubject;
import com.misset.opp.ttl.stubs.index.TTLSubjectStubIndex;
import com.misset.opp.util.LoggerUtil;
import com.misset.opp.util.UriPatternUtil;
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
                .map(OntologyModel.getInstance()::toClass)
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
                                OntologyScopeUtil.getModelSearchScope(myElement.getProject()),
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

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        if (element instanceof TTLLocalname) {
            String fullyQualifiedUri = getClassFromModel().getURI();
            return fullyQualifiedUri != null &&
                    fullyQualifiedUri.equals(((TTLLocalname) element).getQualifiedIri());
        }
        return super.isReferenceTo(element);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        String currentValue = getRangeInElement().substring(myElement.getText());
        if (UriPatternUtil.isUri(currentValue)) {
            String newUri = UriPatternUtil.getNamespace(currentValue) + newElementName;
            Project project = getElement().getProject();
            PsiDocTag docTag = new PsiJavaParserFacadeImpl(project)
                    .createDocTagFromText(myElement.getText().replace(currentValue, newUri));
            return myElement.replace(docTag);
        } else {
            return super.handleElementRename(newElementName);
        }
    }
}
