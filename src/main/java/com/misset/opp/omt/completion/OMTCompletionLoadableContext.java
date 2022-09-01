package com.misset.opp.omt.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.psi.ODTOperatorCall;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.omt.meta.model.modelitems.OMTLoadableMetaType;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.CallableType;
import com.misset.opp.resolvable.psi.PsiCall;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class OMTCompletionLoadableContext extends CompletionContributor {

    public OMTCompletionLoadableContext() {
        extend(CompletionType.BASIC, getInsideLoadableOperator(), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {
                // get the CommandCall:
                for (String contextSelector : OMTLoadableMetaType.getContextSelectors().keySet()) {
                    String value = "'" + contextSelector + "'";
                    String description = OMTLoadableMetaType.getContextSelectors().get(contextSelector);
                    LookupElementBuilder elementBuilder = LookupElementBuilder.create(value)
                            .withTypeText(description, true)
                            .withLookupString(value)
                            .withPresentableText(contextSelector)
                            .withLookupString(contextSelector);
                    result.addElement(elementBuilder);
                }

                result.stopHere();
            }
        });
    }

    private ElementPattern<PsiElement> getInsideLoadableOperator() {
        return PlatformPatterns.psiElement().inside(ODTSignatureArgument.class).and(PlatformPatterns.psiElement().with(
                new PatternCondition<>("Loadable context") {
                    @Override
                    public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
                        ODTSignatureArgument signatureArgument = PsiTreeUtil.getParentOfType(element, ODTSignatureArgument.class);
                        return Optional.ofNullable(signatureArgument)
                                .map(argument -> PsiTreeUtil.getParentOfType(argument, ODTOperatorCall.class))
                                .map(PsiCall::getCallable)
                                .map(Callable::getType)
                                .map(CallableType.LOADABLE::equals)
                                .orElse(false);
                    }
                }
        ));
    }
}
