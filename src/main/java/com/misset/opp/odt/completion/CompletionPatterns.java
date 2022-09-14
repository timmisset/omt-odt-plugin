package com.misset.opp.odt.completion;

import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PatternCondition;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.patterns.PsiJavaElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.builtin.commands.AbstractBuiltInCommand;
import com.misset.opp.odt.builtin.operators.AbstractBuiltInOperator;
import com.misset.opp.odt.psi.*;
import com.misset.opp.resolvable.psi.PsiCall;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.intellij.patterns.PsiJavaPatterns.psiElement;

public interface CompletionPatterns {

    enum COMPLETION_PRIORITY {
        CALLABLE(0),
        VARIABLE_LOCAL(1),
        VARIABLE_GLOBAL(0),
        TRAVERSE(2),
        ROOT_ELEMENT(3);

        private final int value;

        COMPLETION_PRIORITY(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    PsiJavaElementPattern.Capture<PsiElement> QUERY_STEP = psiElement().inside(ODTQueryStep.class);
    PsiJavaElementPattern.Capture<PsiElement> INSIDE_DEFINED_QUERY = psiElement().inside(ODTDefineQueryStatement.class);
    PsiJavaElementPattern.Capture<PsiElement> INSIDE_QUERY_FILTER = psiElement().inside(ODTQueryFilter.class);
    PsiJavaElementPattern.Capture<PsiElement> INSIDE_EQUATION_STATEMENT = psiElement().inside(ODTEquationStatement.class);
    PsiJavaElementPattern.Capture<PsiElement> FIRST_QUERY_STEP = QUERY_STEP.atStartOf(
            psiElement(ODTQueryPath.class)
    );
    PsiJavaElementPattern.Capture<PsiElement> FIRST_QUERY_STEP_ABSOLUTE = QUERY_STEP.afterLeaf(
            PlatformPatterns.psiElement().withElementType(ODTTypes.FORWARD_SLASH).atStartOf(psiElement(ODTQueryPath.class))
    );
    PsiJavaElementPattern.Capture<PsiElement> AFTER_FIRST_QUERY_STEP = QUERY_STEP.andNot(FIRST_QUERY_STEP);
    PsiJavaElementPattern.Capture<PsiElement> VARIABLE_ASSIGNMENT_VALUE = psiElement().inside(ODTVariableAssignment.class);
    PsiJavaElementPattern.Capture<PsiElement> SIGNATURE_ARGUMENT = psiElement().inside(ODTSignatureArgument.class);

    static ElementPattern<PsiElement> getAfterCommandPrefixPattern() {
        return PlatformPatterns.psiElement().with(
                new PatternCondition<>("Command completion") {
                    @Override
                    public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
                        return PsiTreeUtil.prevLeaf(element).getText().equals("@");
                    }
                }
        );
    }

    static ElementPattern<PsiElement> getInsideBuiltinCommandSignaturePattern(AbstractBuiltInCommand builtin) {
        return PlatformPatterns.psiElement().inside(ODTSignatureArgument.class).and(PlatformPatterns.psiElement().with(
                new PatternCondition<>("Builtin command") {
                    @Override
                    public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
                        ODTCommandCall commandCall = PsiTreeUtil.getParentOfType(element, ODTCommandCall.class);
                        return commandCall != null && commandCall.getCallable() == builtin;
                    }
                }
        ));
    }

    static ElementPattern<PsiElement> getInsideBuiltinOperatorSignaturePattern(AbstractBuiltInOperator operator) {
        return PlatformPatterns.psiElement().inside(ODTSignatureArgument.class).and(PlatformPatterns.psiElement().with(
                new PatternCondition<>("Builtin operator") {
                    @Override
                    public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
                        // since the dummy placeholder (IntelliJRulezzzz) is also parsed as an operator
                        // we must first travel outside that scope
                        ODTSignatureArgument signatureArgument = PsiTreeUtil.getParentOfType(element, ODTSignatureArgument.class);
                        return Optional.ofNullable(signatureArgument)
                                .map(argument -> PsiTreeUtil.getParentOfType(argument, ODTOperatorCall.class))
                                .map(PsiCall::getCallable)
                                .map(operator::equals)
                                .orElse(false);
                    }
                }
        ));
    }
}
