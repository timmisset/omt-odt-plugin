package com.misset.opp.odt.psi.impl.resolvable.callable;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaDocumentedElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.documentation.ODTDocumentationUtil;
import com.misset.opp.odt.documentation.ODTDocumented;
import com.misset.opp.odt.psi.ODTDefineName;
import com.misset.opp.odt.psi.ODTDefineParam;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTVariableDelegate;
import com.misset.opp.odt.psi.resolvable.callable.ODTDefineStatement;
import com.misset.opp.odt.refactoring.ODTRefactoringUtil;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.resolvable.psi.PsiCallableImpl;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class ODTDefineStatementAbstract extends PsiCallableImpl implements
        ODTDocumented,
        PsiJavaDocumentedElement,
        ODTDefineStatement {
    private static final Key<CachedValue<HashMap<Integer, Set<OntResource>>>> PARAMETER_TYPES = new Key<>("PARAMETER_TYPES");

    protected ODTDefineStatementAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return getDefineName();
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        ODTDefineName odtDefineName = ODTElementGenerator.getInstance(getProject())
                .fromFile("DEFINE QUERY " + name + " => '';", ODTDefineName.class);
        getDefineName().replace(odtDefineName);
        return this;
    }

    @Override
    public String getDescription(Project project) {
        String javaDocComment = ODTDocumentationUtil.getJavaDocCommentDescription(this);
        return javaDocComment == null || javaDocComment.replace("<br>", "\n").isBlank() ?
                super.getDescription(project) :
                javaDocComment;
    }

    @Override
    public String getDocumentation(Project project) {
        return getDescription(project);
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
        return getContainingFile().getUseScope();
    }

    @Override
    public HashMap<Integer, Set<OntResource>> getParameterTypes() {
        PsiFile containingFile = getContainingFile();
        if (containingFile == null) {
            return new HashMap<>();
        }
        return CachedValuesManager.getCachedValue(this, PARAMETER_TYPES, () -> {
            List<Set<OntResource>> types = Optional.ofNullable(getDefineParam())
                    .map(ODTDefineParam::getVariableList)
                    .stream()
                    .flatMap(Collection::stream)
                    .map(ODTVariableDelegate::resolve)
                    .collect(Collectors.toList());
            return new CachedValueProvider.Result<>(mapCallableParameters(types), containingFile);
        });
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public String getName() {
        return getDefineName().getText();
    }

    protected void decorateCall(PsiCall call) {
        HashMap<Integer, Set<OntResource>> parameterTypes = getParameterTypes();
        final List<ODTVariable> variables = Optional.ofNullable(getDefineParam())
                .map(ODTDefineParam::getVariableList)
                .orElse(Collections.emptyList());
        for (int i = 0; i < variables.size(); i++) {
            if (parameterTypes.containsKey(i) && !parameterTypes.get(i).isEmpty()) {
                call.setParamType(variables.get(i).getName(), parameterTypes.get(i));
            } else {
                call.setParamType(variables.get(i).getName(), call.resolveSignatureArgument(i));
            }
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

    @Override
    public boolean isUnused() {
        return ReferencesSearch.search(this, getUseScope()).findFirst() == null;
    }

    @Override
    public void delete() throws IncorrectOperationException {
        ODTRefactoringUtil.removeScriptline(this);
    }

    @Override
    public Set<OntResource> getReturnType() {
        return Optional.ofNullable(ODTDocumentationUtil.getJavaDocComment(this))
                .map(psiDocComment -> psiDocComment.findTagByName("return"))
                .map(docTag -> ODTDocumentationUtil.getTypeFromDocTag(docTag, 0))
                .orElse(null);
    }

    @Override
    public void inspect(ProblemsHolder holder) {
    }

    @Override
    public Set<OntResource> resolvePreviousStep() {
        return Collections.emptySet();
    }

}
