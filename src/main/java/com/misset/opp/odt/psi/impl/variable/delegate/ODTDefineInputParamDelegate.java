package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.extapi.psi.ASTDelegatePsiElement;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.*;
import com.misset.opp.odt.documentation.ODTDocumentationUtil;
import com.misset.opp.odt.psi.ODTDefineParam;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.shared.refactoring.RefactoringUtil;
import com.misset.opp.ttl.model.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * VAR $variable;
 */
public class ODTDefineInputParamDelegate extends ODTDeclaredVariableDelegate {

    private static final Key<CachedValue<Set<OntResource>>> ONT_TYPE = new Key<>("ONT_TYPE");

    public ODTDefineInputParamDelegate(ODTVariable element) {
        super(element);
    }

    @Override
    public Set<OntResource> resolve() {
        return CachedValuesManager.getCachedValue(element, ONT_TYPE, () ->
                new CachedValueProvider.Result<>(calculateType(),
                        element.getODTFile(),
                        OppModel.ONTOLOGY_MODEL_MODIFICATION_TRACKER));
    }

    private PsiElement getScope() {
        return Optional.ofNullable(PsiTreeUtil.getParentOfType(element, ODTScript.class))
                .map(PsiElement::getParent)
                .orElse(null);
    }

    private Set<OntResource> calculateType() {
        return ODTDocumentationUtil.getTypeFromDocTag(getDocTag(), 1);
    }

    private PsiDocTag getDocTag() {
        PsiElement scope = getScope();
        if (scope == null) {
            return null;
        }
        return ReadAction.compute(() -> ReferencesSearch.search(element,
                        new LocalSearchScope(scope))
                .mapping(PsiReference::getElement)
                .filtering(PsiDocTag.class::isInstance)
                .mapping(PsiDocTag.class::cast)
                .findFirst());
    }

    @Override
    public void delete() {
        // remove the define parameter from the calls and recreate the block
        PsiCallable callable = PsiTreeUtil.getParentOfType(element, PsiCallable.class);
        int parameterIndex = getParameterIndex();
        if (callable == null || parameterIndex == -1) {
            return;
        }

        // remove parameter from calls
        ReferencesSearch.search(callable, callable.getUseScope())
                .findAll()
                .forEach(psiReference -> RefactoringUtil.removeParameterFromCall(psiReference, parameterIndex));

        // remove tag
        Optional.ofNullable(getDocTag()).ifPresent(ODTDocumentationUtil::removeDocTag);

        // remove the parameter
        // remove comma's in the DEFINE param block
        // either remove the comma to the left, or to the right or none at all
        ODTDefineParam defineParam = PsiTreeUtil.getParentOfType(element, ODTDefineParam.class);
        if (!removeComma(PsiTreeUtil.prevVisibleLeaf(element))) {
            removeComma(PsiTreeUtil.nextVisibleLeaf(element));
        }
        ASTDelegatePsiElement.deleteElementFromParent(element);
        if (defineParam != null && defineParam.getVariableList().isEmpty()) {
            // remove unnecessary empty parameters block
            defineParam.delete();
        }
    }

    private boolean removeComma(PsiElement comma) {
        if (PsiUtilCore.getElementType(comma) == ODTTypes.COMMA) {
            comma.delete();
            return true;
        }
        return false;
    }

    private int getParameterIndex() {
        ODTDefineStatement defineStatement = PsiTreeUtil.getParentOfType(element, ODTDefineStatement.class);
        List<ODTVariable> parameters = Optional.ofNullable(defineStatement)
                .map(ODTDefineStatement::getDefineParam)
                .map(ODTDefineParam::getVariableList)
                .orElse(Collections.emptyList());
        return parameters.indexOf(element);
    }

    @Override
    public String getSource() {
        return "ODT parameter";
    }
}
