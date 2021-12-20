package com.misset.opp.odt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.misset.opp.odt.psi.ODTVariable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.codeInspection.ProblemHighlightType.LIKE_UNUSED_SYMBOL;
import static com.misset.opp.odt.ODTFindUsagesProvider.CHECK_CAN_FIND_USAGES;

public class ODTUnusedInspection extends LocalInspectionTool {

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Inspect named elements that are not references by any element";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (!CHECK_CAN_FIND_USAGES.test(element)) {
                    return;
                }
                if (!(element instanceof PsiNamedElement)) {
                    return;
                }
                // don't gray-out the entire element but only the name, for example, the DEFINE QUERY query => ''
                // only 'query' should be gray
                PsiElement targetElement = element instanceof PsiNameIdentifierOwner ?
                        ((PsiNameIdentifierOwner) element).getIdentifyingElement() :
                        element;

                final PsiNamedElement namedElement = (PsiNamedElement) element;
                if (!isException(element) && ReferencesSearch.search(element, element.getUseScope())
                        .allowParallelProcessing().findFirst() == null) {
                    holder.registerProblem(
                            targetElement,
                            namedElement.getName() + " is never used",
                            LIKE_UNUSED_SYMBOL);
                }
            }
        };
    }

    private boolean isException(PsiElement element) {
        if(element instanceof ODTVariable) {
            return "$_".equals(((ODTVariable) element).getName());
        }

        return false;
    }
}
