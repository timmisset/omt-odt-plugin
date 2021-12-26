package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import com.misset.opp.shared.InjectableContentType;
import com.misset.opp.shared.InjectionHost;
import org.jetbrains.annotations.NotNull;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * Certain injection segments have specific completions (templates).
 */
public class ODTInjectableSectionCompletion extends CompletionContributor {

    protected static final String QUERY_SIMPLE_TEMPLATE = "DEFINE QUERY simpleQuery => 'Hello world';";
    protected static final String QUERY_PARAMETER_TEMPLATE = "" +
            "/**\n" +
            " * @param $param (string)\n" +
            " */\n" +
            "DEFINE QUERY queryWithParameter($param) => LOG($param);";
    protected static final String QUERY_BASE_TEMPLATE = "" +
            "/**\n" +
            " * @base (string)\n" +
            " */\n" +
            "DEFINE QUERY queryWithBase => LOG;";
    protected static final String COMMAND_SIMPLE_TEMPLATE = "DEFINE COMMAND simpleCommand => { RETURN true; }";
    protected static final String COMMAND_PARAMETER_TEMPLATE = "/**\n" +
            " * @param $param (string)\n" +
            " */\n" +
            "DEFINE COMMAND commandWithParameter($param) => { RETURN $param; }";

    private static final String CODE_SNIPPET = "Code snippet";
    private static final PsiElementPattern.Capture<PsiElement> OUTSIDE_STATEMENT = psiElement().andNot(psiElement().inside(ODTDefineStatement.class));

    public ODTInjectableSectionCompletion() {
        extend(CompletionType.BASIC, psiElement(), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                ODTFile originalFile = (ODTFile) parameters.getOriginalFile();
                InjectionHost host = originalFile.getHost();
                if (host != null) {
                    InjectableContentType injectableContentType = host.getInjectableContentType();
                    if (injectableContentType != InjectableContentType.None) {
                        insertInjectableContentTypeCompletions(injectableContentType, parameters.getPosition(), result);
                    }
                }
            }

            private void insertInjectableContentTypeCompletions(InjectableContentType injectableContentType,
                                                                PsiElement element,
                                                                CompletionResultSet resultSet) {
                switch (injectableContentType) {
                    case Query:
                        insertQueryTemplate(element, resultSet);
                        break;
                    case Command:
                        insideCommandTemplate(element, resultSet);
                        break;
                }

            }

            private void insertQueryTemplate(PsiElement element, CompletionResultSet resultSet) {
                if (OUTSIDE_STATEMENT.accepts(element)) {
                    resultSet.addElement(LookupElementBuilder.create(QUERY_SIMPLE_TEMPLATE)
                            .withPresentableText("DEFINE QUERY simpleQuery")
                            .withTypeText(CODE_SNIPPET));
                    resultSet.addElement(LookupElementBuilder.create(QUERY_PARAMETER_TEMPLATE)
                            .withPresentableText("DEFINE QUERY queryWithParameter($param)")
                            .withTypeText(CODE_SNIPPET));
                    resultSet.addElement(LookupElementBuilder.create(QUERY_BASE_TEMPLATE)
                            .withPresentableText("DEFINE QUERY queryWithBase")
                            .withTypeText(CODE_SNIPPET));
                    resultSet.stopHere();
                }
            }

            private void insideCommandTemplate(PsiElement element, CompletionResultSet resultSet) {
                if (OUTSIDE_STATEMENT.accepts(element)) {
                    resultSet.addElement(LookupElementBuilder.create(COMMAND_SIMPLE_TEMPLATE)
                            .withPresentableText("DEFINE COMMAND simpleCommand")
                            .withTypeText(CODE_SNIPPET));
                    resultSet.addElement(LookupElementBuilder.create(COMMAND_PARAMETER_TEMPLATE)
                            .withPresentableText("DEFINE COMMAND commandWithParameter($param)")
                            .withTypeText(CODE_SNIPPET));
                    resultSet.stopHere();
                }
            }
        });
    }

}
