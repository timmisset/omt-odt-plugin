package com.misset.opp.omt.refactor;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.refactoring.listeners.RefactoringElementListener;
import com.intellij.refactoring.rename.RenameDialog;
import com.intellij.refactoring.rename.RenamePsiElementProcessor;
import com.intellij.refactoring.ui.NameSuggestionsField;
import com.intellij.usageView.UsageInfo;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.variables.OMTNamedVariableMetaType;
import com.misset.opp.omt.psi.impl.variable.OMTVariableImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * Processor for generation the RenameDialog and handling the response
 */
public class OMTVariableRenameProcessor extends RenamePsiElementProcessor {
    @Override
    public boolean canProcessElement(@NotNull PsiElement element) {
        return getMetaType(element) != null;
    }

    private OMTNamedVariableMetaType getMetaType(PsiElement element) {
        return Optional.ofNullable(OMTMetaTypeProvider.getInstance(element.getProject())
                        .getMetaTypeProxy(element))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .filter(OMTNamedVariableMetaType.class::isInstance)
                .map(OMTNamedVariableMetaType.class::cast)
                .orElse(null);
    }

    public YAMLValue getYamlValueElement(PsiElement element) {
        return PsiTreeUtil.getParentOfType(element, YAMLValue.class, false);
    }

    @Override
    public void renameElement(@NotNull PsiElement element,
                              @NotNull String newName,
                              UsageInfo @NotNull [] usages,
                              @Nullable RefactoringElementListener listener) throws IncorrectOperationException {
        final YAMLValue yamlValueElement = getYamlValueElement(element);
        OMTVariableImpl.wrap(yamlValueElement).setName(newName);
        Arrays.stream(usages)
                .map(UsageInfo::getReference)
                .filter(Objects::nonNull)
                .forEach(reference -> reference.handleElementRename(newName));
    }

    @Override
    public @NotNull RenameDialog createRenameDialog(@NotNull Project project,
                                                    @NotNull PsiElement element,
                                                    @Nullable PsiElement nameSuggestionContext,
                                                    @Nullable Editor editor) {
        return new VariableRenameProcessorWindow(project, getYamlValueElement(element), nameSuggestionContext, editor);
    }

    @Override
    public @NotNull Collection<PsiReference> findReferences(@NotNull PsiElement element,
                                                            @NotNull SearchScope searchScope,
                                                            boolean searchInCommentsAndStrings) {
        return super.findReferences(element,
                new LocalSearchScope(element.getContainingFile()),
                searchInCommentsAndStrings);
    }

    private class VariableRenameProcessorWindow extends RenameDialog {

        public VariableRenameProcessorWindow(@NotNull Project project,
                                             @NotNull PsiElement psiElement,
                                             @Nullable PsiElement nameSuggestionContext,
                                             Editor editor) {
            super(project, psiElement, nameSuggestionContext, editor);
        }

        @Override
        protected String getFullName() {
            final PsiElement psiElement = getPsiElement();
            final OMTNamedVariableMetaType metaType = getMetaType(psiElement);
            return metaType.getName((YAMLValue) psiElement);
        }

        @Override
        protected NameSuggestionsField getNameSuggestionsField() {
            return super.getNameSuggestionsField();
        }

        @Override
        public String[] getSuggestedNames() {
            return new String[]{getFullName()};
        }
    }
}
