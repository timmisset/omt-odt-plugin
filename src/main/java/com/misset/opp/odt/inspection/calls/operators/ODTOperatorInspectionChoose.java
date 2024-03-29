package com.misset.opp.odt.inspection.calls.operators;

import com.intellij.codeInspection.*;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.ODTChooseBlock;
import com.misset.opp.odt.psi.ODTOtherwisePath;
import com.misset.opp.odt.psi.ODTWhenPath;
import com.misset.opp.odt.psi.resolvable.call.ODTCall;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Code inspection for all unused declarations
 */
public class ODTOperatorInspectionChoose extends LocalInspectionTool {
    protected static final String USE_IIF = "Only one WHEN condition, use IIF instead";
    protected static final String OMIT_OTHERWISE = "OTHERWISE can be omitted if null";
    protected static final String REPLACE_QUICKFIX_TITLE = "Replace with IIF";
    protected static final String INCOMPLETE_CHOOSE_EXPECTED_WHEN_CONDITIONS = "No WHEN conditions set";
    protected static final String INCOMPLETE_CHOOSE_EXPECTED_END = "No END set";

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Validates specific ODT operator: Choose";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTChooseBlock) {
                    inspectChooseOperator(holder, (ODTChooseBlock) element);
                }
            }
        };
    }

    private void inspectChooseOperator(@NotNull ProblemsHolder holder,
                                       @NotNull ODTChooseBlock chooseBlock) {
        if (chooseBlock.getQueryStepList().isEmpty()) {
            holder.registerProblem(chooseBlock, INCOMPLETE_CHOOSE_EXPECTED_WHEN_CONDITIONS, ProblemHighlightType.ERROR);
        } else if (chooseBlock.getWhenPathList().size() == 1) {
            holder.registerProblem(chooseBlock,
                    USE_IIF,
                    ProblemHighlightType.WARNING,
                    getReplaceWithIIFQuickFix(chooseBlock));
        }
        if (chooseBlock.getEndPath() == null) {
            holder.registerProblem(chooseBlock, INCOMPLETE_CHOOSE_EXPECTED_END, ProblemHighlightType.ERROR);
        }
        if (Optional.ofNullable(chooseBlock.getOtherwisePath())
                .map(ODTOtherwisePath::getQuery)
                .map(PsiElement::getText)
                .filter("null"::equals)
                .isPresent()) {
            holder.registerProblem(chooseBlock.getOtherwisePath(), OMIT_OTHERWISE, ProblemHighlightType.WEAK_WARNING);
        }
    }

    private @Nullable PsiElement getIIFReplacement(@NotNull ODTChooseBlock chooseBlock) {
        return Optional.ofNullable(chooseBlock.getWhenPathList().get(0))
                .filter(whenPath -> whenPath.getQueryList().size() == 2)
                .map(whenPath -> getReplacementFromChooseAndWhen(chooseBlock, whenPath))
                .orElse(null);
    }

    private ODTCall getReplacementFromChooseAndWhen(@NotNull ODTChooseBlock chooseBlock,
                                                    ODTWhenPath whenPath) {
        return ODTElementGenerator.getInstance(chooseBlock.getProject())
                .createCall("IIF", null,
                        Optional.ofNullable(whenPath.getCondition()).map(PsiElement::getText).orElse(null),
                        Optional.ofNullable(whenPath.getThen()).map(PsiElement::getText).orElse(null),
                        Optional.ofNullable(chooseBlock.getOtherwisePath())
                                .map(ODTOtherwisePath::getQuery)
                                .map(PsiElement::getText)
                                .orElse(null));
    }

    private LocalQuickFix getReplaceWithIIFQuickFix(@NotNull ODTChooseBlock chooseBlock) {
        final PsiElement replacement = getIIFReplacement(chooseBlock);
        return new LocalQuickFix() {
            @Override
            public @IntentionFamilyName @NotNull String getFamilyName() {
                return "Replace";
            }

            @Override
            public @IntentionName @NotNull String getName() {
                return REPLACE_QUICKFIX_TITLE;
            }

            @Override
            public void applyFix(@NotNull Project project,
                                 @NotNull ProblemDescriptor descriptor) {
                if (replacement != null) {
                    descriptor.getPsiElement().replace(replacement);
                }
            }
        };
    }
}
