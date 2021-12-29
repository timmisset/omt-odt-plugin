package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.stubs.StubIndex;
import com.misset.opp.odt.documentation.ODTDocumentationUtil;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.psi.TTLSubject;
import com.misset.opp.ttl.stubs.index.TTLSubjectStubIndex;
import com.misset.opp.ttl.util.TTLScopeUtil;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ODTJavaDocTTLSubjectReference extends PsiReferenceBase.Poly<PsiDocTag> implements PsiPolyVariantReference {
    private final int position;

    public ODTJavaDocTTLSubjectReference(PsiDocTag element, TextRange textRange, int position) {
        super(element, textRange, false);
        this.position = position;
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        PsiDocTag element = getElement();
        Set<OntResource> typeFromDocTag =
                ODTDocumentationUtil.getTypeFromDocTag(element, position);
        if (typeFromDocTag.isEmpty()) {
            return ResolveResult.EMPTY_ARRAY;
        } else {
            OntResource next = typeFromDocTag.iterator().next();
            OntClass ontClass = OppModel.INSTANCE.toClass(next);
            return StubIndex.getElements(
                            TTLSubjectStubIndex.KEY,
                            ontClass.getURI(),
                            element.getProject(),
                            TTLScopeUtil.getModelSearchScope(myElement.getProject()),
                            TTLSubject.class
                    ).stream()
                    .map(PsiElementResolveResult::new)
                    .toArray(ResolveResult[]::new);
        }
    }


}
