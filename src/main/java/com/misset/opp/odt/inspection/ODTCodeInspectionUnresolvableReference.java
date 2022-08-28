package com.misset.opp.odt.inspection;

import com.intellij.codeInspection.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.javadoc.PsiDocTag;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTNamespacePrefix;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTResolvableCall;
import com.misset.opp.odt.psi.reference.ODTCallReference;
import com.misset.opp.odt.psi.reference.ODTJavaDocTTLSubjectReference;
import com.misset.opp.odt.psi.reference.ODTTypePrefixAnnotationReference;
import com.misset.opp.omt.indexing.OMTPrefixIndex;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Code annotator for all unused declarations
 */
public class ODTCodeInspectionUnresolvableReference extends LocalInspectionTool {

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Inspects references set on variables and calls. If not resolvable it will show UNKNOWN errors:<br>" +
                "Cannot resolve [type] '[name]'";
    }

    private static final Logger LOGGER = Logger.getInstance(ODTCodeInspectionUnresolvableReference.class);

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly, @NotNull LocalInspectionToolSession session) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                PsiFile containingFile = element.getContainingFile();
                if (!(containingFile instanceof ODTFile)) {
                    return;
                }
                if (element instanceof ODTVariable) {
                    LoggerUtil.runWithLogger(LOGGER, "Inspect variable " + ((ODTVariable) element).getName(), () -> inspectVariable(holder, (ODTVariable) element));
                } else if (element instanceof ODTResolvableCall) {
                    LoggerUtil.runWithLogger(LOGGER, "Inspect Call " + ((ODTResolvableCall) element).getName(), () -> inspectCall(holder, (ODTResolvableCall) element));
                } else if (element instanceof PsiDocTag) {
                    LoggerUtil.runWithLogger(LOGGER, "Inspect DocTag", () -> inspectDocTag(holder, (PsiDocTag) element));
                } else if (element instanceof ODTNamespacePrefix) {
                    LoggerUtil.runWithLogger(LOGGER, "Inspect NamespacePrefix", () -> inspectNamespacePrefix(holder, (ODTNamespacePrefix) element));
                }
            }
        };
    }

    private void inspectVariable(@NotNull ProblemsHolder holder,
                                 @NotNull ODTVariable variable) {
        PsiReference reference = variable.getReference();
        if (reference != null && !variable.isDeclaredVariable() && variable.getDeclared() == null) {
            holder.registerProblem(reference);
        }
    }

    private void inspectCall(@NotNull ProblemsHolder holder,
                             @NotNull ODTResolvableCall call) {
        final Callable callable = call.getCallable();
        ODTCallReference reference = call.getReference();
        if (reference != null && callable == null) {
            holder.registerProblem(reference);
        }
    }

    private void inspectDocTag(@NotNull ProblemsHolder holder,
                               @NotNull PsiDocTag docTag) {
        // Inspect the DocTag elements which contain references to multiple items:
        // $variables
        // prefix
        // ontology subjects
        for (PsiReference reference : docTag.getReferences()) {
            if (reference.resolve() == null) {
                if (reference instanceof ODTTypePrefixAnnotationReference) {
                    inspectPrefixReference(holder, reference, docTag);
                } else if (reference instanceof ODTJavaDocTTLSubjectReference) {
                    ODTJavaDocTTLSubjectReference subjectReference = (ODTJavaDocTTLSubjectReference) reference;
                    if (subjectReference.getClassFromModel() != null) {
                        // although not part of the TTL ontology files, the type or class might still be known
                        // in the model itself.
                        return;
                    }
                    holder.registerProblem(reference);
                } else {
                    holder.registerProblem(reference);
                }
            }
        }
    }

    private void inspectNamespacePrefix(@NotNull ProblemsHolder holder,
                                        @NotNull ODTNamespacePrefix namespacePrefix) {
        PsiReference reference = namespacePrefix.getReference();
        if (reference == null || reference.resolve() != null) {
            return;
        }
        inspectPrefixReference(holder, reference, namespacePrefix);
    }

    private void inspectPrefixReference(@NotNull ProblemsHolder holder,
                                        @NotNull PsiReference reference,
                                        @NotNull PsiElement referenceHolder) {
        String prefix = reference.getRangeInElement().substring(referenceHolder.getText());

        PsiFile file = holder.getFile();
        if (!(file instanceof ODTFile)) {
            return;
        }

        LocalQuickFix[] localQuickFixes = OMTPrefixIndex.getNamespaces(prefix)
                .stream()
                .map(namespace -> ((ODTFile) file).getRegisterPrefixQuickfix(prefix, namespace))
                .toArray(LocalQuickFix[]::new);

        holder.registerProblem(referenceHolder,
                "Cannot resolve prefix '" + prefix + "'",
                ProblemHighlightType.LIKE_UNKNOWN_SYMBOL,
                reference.getRangeInElement(),
                localQuickFixes);
    }
}
