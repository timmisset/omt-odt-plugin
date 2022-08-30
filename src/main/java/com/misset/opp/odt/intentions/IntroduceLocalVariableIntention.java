package com.misset.opp.odt.intentions;

import com.intellij.codeInsight.PsiEquivalenceUtil;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pass;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.refactoring.RefactoringActionHandler;
import com.intellij.refactoring.introduce.inplace.OccurrencesChooser;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.ODTFileType;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.util.PsiRelationshipUtil;
import com.misset.opp.odt.refactoring.ODTInplaceVariableIntroducer;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IntroduceLocalVariableIntention implements IntentionAction, RefactoringActionHandler {
    protected static final String TEXT = "Introduce local variable";
    private static final TokenSet LOOKBACK = TokenSet.orSet(
            TokenSet.WHITE_SPACE, TokenSet.create(ODTTypes.SEMICOLON)
    );
    private static final Class<? extends PsiElement>[] NON_STRICT_PARENTS = new Class[]{
            ODTScript.class, ODTVariableAssignment.class, ODTDefineQueryStatement.class
    };

    @Override
    public @IntentionName @NotNull String getText() {
        return getFamilyName();
    }

    @Override
    public @NotNull @IntentionFamilyName String getFamilyName() {
        return TEXT;
    }

    @Override
    public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
        return Optional.ofNullable(getStatement(editor, file))
                .map(statement -> PsiTreeUtil.getNonStrictParentOfType(statement, NON_STRICT_PARENTS))
                .map(ODTScript.class::isInstance)
                .orElse(false);
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
        ODTStatement statement = getStatement(editor, file);
        if (statement instanceof ODTResolvable) {
            findOccurrences(project, editor, statement);
        }
    }

    private ODTInplaceVariableIntroducer getIntroducer(Project project,
                                                       Editor editor,
                                                       ODTStatement statement,
                                                       PsiElement[] occurrences,
                                                       OccurrencesChooser.ReplaceChoice choice) {
        return new ODTInplaceVariableIntroducer(project,
                editor,
                (ODTResolvable) statement,
                null,
                occurrences,
                "Introduce Local Variable",
                ODTFileType.INSTANCE,
                choice);
    }

    private boolean areElementsEquivalent(ODTStatement statement, PsiElement element) {
        if (element instanceof ODTQuery) {
            if (statement instanceof ODTQueryStatement) {
                return PsiEquivalenceUtil.areElementsEquivalent(((ODTQueryStatement) statement).getQuery(), element);
            }
            return false;
        }
        return PsiEquivalenceUtil.areElementsEquivalent(statement, element);
    }

    public <T extends PsiElement> Predicate<T> distinctByStatement() {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(toDistinctLevel(t), Boolean.TRUE) == null;
    }

    public PsiElement toDistinctLevel(PsiElement element) {
        if (element instanceof ODTQuery && element.getParent() instanceof ODTStatement) {
            return element.getParent();
        }
        return element;
    }

    private void findOccurrences(Project project, Editor editor, ODTStatement statement) {
        ODTScript parentScript = PsiTreeUtil.getParentOfType(statement, ODTScript.class);
        Collection<PsiElement> possibleTargets = PsiTreeUtil.findChildrenOfAnyType(parentScript, ODTStatement.class, ODTQuery.class);

        List<PsiElement> elements = possibleTargets.stream().filter(
                        element -> !element.equals(statement) &&
                                PsiRelationshipUtil.canBeRelatedElement(statement, element) &&
                                areElementsEquivalent(statement, element))
                .filter(distinctByStatement())
                .collect(Collectors.toList());

        if (elements.isEmpty()) {
            getIntroducer(project, editor, statement, PsiElement.EMPTY_ARRAY, OccurrencesChooser.ReplaceChoice.ALL).startInplaceIntroduceTemplate();
        } else {
            List<PsiElement> occurrences = new ArrayList<>(elements);
            elements.add(statement);
            new OccurrencesChooser<PsiElement>(editor) {
                @Override
                protected TextRange getOccurrenceRange(PsiElement occurrence) {
                    return occurrence.getTextRange();
                }
            }.showChooser(statement, elements, new Pass<>() {
                @Override
                public void pass(final OccurrencesChooser.ReplaceChoice choice) {
                    getIntroducer(project, editor, statement, occurrences.toArray(PsiElement[]::new), choice).startInplaceIntroduceTemplate();
                }
            });
        }

    }

    private ODTStatement getStatement(Editor editor, PsiFile file) {
        return getElementAtCaret(editor, file, ODTStatement.class);
    }

    private <T extends PsiElement> T getElementAtCaret(Editor editor, PsiFile file, Class<T> elementClass) {
        int offset = editor.getCaretModel().getCurrentCaret().getOffset();
        return Optional.ofNullable(getElementAtCaret(file, offset))
                .map(element -> PsiTreeUtil.getParentOfType(element, elementClass))
                .orElse(null);
    }

    private PsiElement getElementAtCaret(PsiFile file, int offset) {
        PsiElement elementAt = file.findElementAt(offset);
        while (offset > 0 && elementAt != null && LOOKBACK.contains(PsiUtilCore.getElementType(elementAt))) {
            offset -= 1;
            elementAt = file.findElementAt(offset);
        }
        return elementAt;
    }

    @Override
    public boolean startInWriteAction() {
        return false;
    }

    @Override
    public void invoke(@NotNull Project project, Editor editor, PsiFile file, DataContext dataContext) {
        invoke(project, editor, file);
    }

    @Override
    public void invoke(@NotNull Project project, PsiElement @NotNull [] elements, DataContext dataContext) {
        // not supported
    }
}
