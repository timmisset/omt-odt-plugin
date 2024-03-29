package com.misset.opp.omt.meta;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.completion.OMTCompletions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.CompletionContext;
import org.jetbrains.yaml.meta.model.YamlStringType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLScalar;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

public class OMTImportMemberMetaType extends YamlStringType {
    protected static final UnaryOperator<String> NOT_AN_EXPORTED_MEMBER = s -> String.format("%s is not an exported member", s);
    private static final OMTImportMemberMetaType INSTANCE = new OMTImportMemberMetaType();

    public static OMTImportMemberMetaType getInstance() {
        return INSTANCE;
    }

    private OMTImportMemberMetaType() {
    }

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {

        if (scalarValue instanceof YAMLPlainTextImpl) {
            Optional.ofNullable(scalarValue.getReference())
                    .filter(psiReference -> psiReference.resolve() == null)
                    .ifPresent(psiReference -> holder.registerProblem(
                            psiReference.getElement(),
                            NOT_AN_EXPORTED_MEMBER.apply(psiReference.getElement().getText()),
                            ProblemHighlightType.ERROR)
                    );
        }
    }

    private Set<String> getExportingMemberNames(PsiElement element) {
        final YAMLKeyValue keyValue = PsiTreeUtil.getParentOfType(element, YAMLKeyValue.class);
        if (keyValue == null) {
            return Collections.emptySet();
        }
        return OMTImportMetaType.getInstance().getExportedMembersFromOMTFile(keyValue).keySet();
    }

    @Override
    public @NotNull List<? extends LookupElement> getValueLookups(@NotNull YAMLScalar insertedScalar,
                                                                  @Nullable CompletionContext completionContext) {
        /*
            Return a list with exporting members of the OMT file that is imported
            Resolve the insertedScalar to the parent Key:Value which is the Import structure
         */
        final PsiElement key = insertedScalar.getFirstChild();
        if(key == null) { return Collections.emptyList(); }

        final CompletionParameters completionParameters = OMTCompletions.getCompletionParameters(key);
        if(completionParameters == null) { return Collections.emptyList(); }

        return getExportingMemberNames(completionParameters.getOriginalPosition()).stream()
                .map(s -> s.replace("@", ""))
                .sorted()
                .map(s -> new LookupElement() {
                    @Override
                    public @NotNull String getLookupString() {
                        return s;
                    }
                })
                .collect(Collectors.toList());
    }
}
