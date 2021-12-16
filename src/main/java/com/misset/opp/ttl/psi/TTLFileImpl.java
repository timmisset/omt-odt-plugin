package com.misset.opp.ttl.psi;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.TTLFileType;
import com.misset.opp.ttl.TTLLanguage;
import com.misset.opp.ttl.psi.iri.TTLIriHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

public class TTLFileImpl extends PsiFileBase implements TTLFile {
    public TTLFileImpl(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, TTLLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return TTLFileType.INSTANCE;
    }

    /**
     * Use to find a subject
     */
    @Override
    public TTLSubject getSubject(String iri) {
        if (iri == null) {
            return null;
        }
        return PsiTreeUtil.findChildrenOfType(this, TTLSubject.class)
                .stream()
                .filter(subject -> isSubject(subject, iri))
                .findFirst()
                .orElse(null);
    }

    private boolean isSubject(TTLSubject subject, String iri) {
        return Optional.ofNullable(subject)
                .map(TTLSubject::getIri)
                .map(TTLIriHolder::getQualifiedUri)
                .map(iri::equals)
                .orElse(false);
    }

    @Override
    public TTLObject getPredicate(String subjectIri, String predicateIri) {
        return Optional.ofNullable(getSubject(subjectIri))
                .map(subject -> PsiTreeUtil.findChildrenOfType(subject.getParent(), TTLPredicateObject.class))
                .stream()
                .flatMap(Collection::stream)
                .filter(ttlPredicateObject ->
                        hasPredicate(ttlPredicateObject, OppModel.INSTANCE.SHACL_PATH.getURI()) &&
                                hasObject(ttlPredicateObject, predicateIri)
                )
                .map(predicateObject -> predicateObject.getObjectList().getObjectList().get(0))
                .findFirst()
                .orElse(null);
    }

    /**
     * Use to find an object by subject-predicate, expecting a SHACL structure with a
     * predicateObjectList that contains a sh:path to the predicate and a sh:class.
     * The returned PsiElement is the object of the predicate-object sh:class-[Object]
     */
    @Override
    public TTLObject getObject(String subjectIri, String predicateIri) {
        return Optional.ofNullable(getPredicate(subjectIri, predicateIri))
                .map(predicateObject -> PsiTreeUtil.getParentOfType(predicateObject, TTLPredicateObjectList.class))
                .map(TTLPredicateObjectList::getPredicateObjectList)
                .stream()
                .flatMap(Collection::stream)
                .filter(predicateObject -> hasPredicate(predicateObject, OppModel.INSTANCE.SHACL_CLASS.getURI()))
                .map(predicateObject -> PsiTreeUtil.findChildOfType(predicateObject, TTLObject.class))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private boolean hasPredicate(TTLPredicateObject predicateObject, String iri) {
        return Optional.of(predicateObject.getVerb())
                .map(TTLVerb::getPredicate)
                .map(TTLPredicate::getIri)
                .map(TTLIriHolder::getQualifiedUri)
                .map(iri::equals)
                .orElse(false);
    }

    private boolean hasObject(TTLPredicateObject predicateObject, String iri) {
        return Optional.of(predicateObject)
                .map(TTLPredicateObject::getObjectList)
                .map(TTLObjectList::getObjectList)
                .stream()
                .flatMap(Collection::stream)
                .anyMatch(ttlObject -> hasObject(ttlObject, iri));
    }

    private boolean hasObject(TTLObject ttlObject, String iri) {
        return Optional.ofNullable(ttlObject)
                .map(TTLObject::getIri)
                .map(TTLIriHolder::getQualifiedUri)
                .map(iri::equals)
                .orElse(false);
    }

}
