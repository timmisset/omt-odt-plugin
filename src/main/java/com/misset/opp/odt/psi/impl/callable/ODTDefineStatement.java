package com.misset.opp.odt.psi.impl.callable;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiJavaDocumentedElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.callable.Call;
import com.misset.opp.callable.psi.PsiCallable;
import com.misset.opp.odt.documentation.ODTDocumentationUtil;
import com.misset.opp.odt.psi.ODTDefineName;
import com.misset.opp.odt.psi.ODTDefineParam;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.ODTASTWrapperPsiElement;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTVariableDelegate;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class ODTDefineStatement extends ODTASTWrapperPsiElement implements
        PsiCallable,
        PsiNameIdentifierOwner,
        PsiJavaDocumentedElement {
    private static final Key<CachedValue<HashMap<Integer, Set<OntResource>>>> PARAMETER_TYPES = new Key<>("PARAMETER_TYPES");

    public ODTDefineStatement(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return getDefineName();
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return this;
    }

    @NotNull
    abstract public ODTDefineName getDefineName();

    @Override
    public String getDescription(String context) {
        String javaDocComment = ODTDocumentationUtil.getJavaDocComment(this);
        return javaDocComment == null || javaDocComment.replace("<br>", "\n").isBlank() ? PsiCallable.super.getDescription(context) : javaDocComment;
    }

    @Override
    public int minNumberOfArguments() {
        return Optional.ofNullable(getDefineParam())
                .map(ODTDefineParam::getVariableList)
                .map(List::size)
                .orElse(0);
    }

    @Override
    public int maxNumberOfArguments() {
        return minNumberOfArguments();
    }

    @Override
    public Set<OntResource> getParamType(int index) {
        return getParameterTypes().getOrDefault(index, Collections.emptySet());
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        final ODTFile containingFile = getContainingFile();
        return containingFile.getExportingMemberUseScope(getName());
    }

    @Override
    public HashMap<Integer, Set<OntResource>> getParameterTypes() {
        return CachedValuesManager.getCachedValue(this, PARAMETER_TYPES, () -> {
            List<Set<OntResource>> types = Optional.ofNullable(getDefineParam())
                    .map(ODTDefineParam::getVariableList)
                    .stream()
                    .flatMap(Collection::stream)
                    .map(ODTVariableDelegate::getType)
                    .collect(Collectors.toList());
            return new CachedValueProvider.Result<>(mapCallableParameters(types), getContainingFile());
        });
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    public String getName() {
        return getDefineName().getText();
    }

    public abstract ODTDefineParam getDefineParam();

    protected void decorateCall(Call call) {
        final List<ODTVariable> variables = Optional.ofNullable(getDefineParam())
                .map(ODTDefineParam::getVariableList)
                .orElse(Collections.emptyList());
        for (int i = 0; i < variables.size(); i++) {
            call.setParamType(variables.get(i).getName(), call.resolveSignatureArgument(i));
        }
    }

    @Override
    public int getTextOffset() {
        return getDefineName().getTextOffset();
    }

    @Override
    public @Nullable PsiDocComment getDocComment() {
        return Optional.ofNullable(PsiTreeUtil.getParentOfType(this, PsiJavaDocumentedElement.class))
                .map(PsiJavaDocumentedElement::getDocComment)
                .orElse(null);
    }

    @Override
    public Map<Integer, String> getParameterNames() {
        List<ODTVariable> variables = Optional.ofNullable(getDefineParam())
                .map(ODTDefineParam::getVariableList)
                .orElse(Collections.emptyList());
        return IntStream.range(0, variables.size())
                .boxed()
                .collect(Collectors.toMap(i -> i, i -> getParamName(variables, i)));
    }

    private String getParamName(List<ODTVariable> variables, int index) {
        return Optional.ofNullable(variables.get(index))
                .map(PsiNamedElement::getName)
                .orElse("$param" + index);
    }
}
