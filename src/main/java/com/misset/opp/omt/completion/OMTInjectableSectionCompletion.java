package com.misset.opp.omt.completion;

import com.google.common.base.Strings;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.completion.ODTSharedCompletion;
import com.misset.opp.odt.completion.commands.ODTCommandCompletionNewGraph;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.resolvable.callable.ODTDefineStatement;
import com.misset.opp.omt.injection.OMTODTInjectionUtil;
import com.misset.opp.omt.meta.scalars.queries.OMTBooleanQueryType;
import com.misset.opp.omt.meta.scalars.queries.OMTShapeQueryType;
import com.misset.opp.omt.meta.scalars.scripts.OMTCommandsMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTQueriesMetaType;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.Collections;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * Certain injection segments have specific completions (templates).
 * Since this is driven by the OMT structure where the ODT fragment resides, it is considered
 * an OMT/ODT completion and not purely an ODT completion.
 */
public class OMTInjectableSectionCompletion extends CompletionContributor {

    protected static final String QUERY_SIMPLE_TEMPLATE = "DEFINE QUERY simpleQuery => 'Hello world';";
    private static final String JAVADOC_START = "/**\n";
    private static final String JAVADOC_END = " */\n";
    protected static final String QUERY_PARAMETER_TEMPLATE = "" +
            JAVADOC_START +
            " * @param $param (string)\n" +
            JAVADOC_END +
            "DEFINE QUERY queryWithParameter($param) => $param;";
    protected static final String COMMAND_SIMPLE_TEMPLATE = "DEFINE COMMAND simpleCommand => { RETURN true; }";
    protected static final String QUERY_BASE_TEMPLATE = "" +
            JAVADOC_START +
            " * @base (string)\n" +
            JAVADOC_END +
            "DEFINE QUERY queryWithBase => LOG;";

    private static final String CODE_SNIPPET = "Code snippet";
    private static final PsiElementPattern.Capture<PsiElement> OUTSIDE_STATEMENT = psiElement().andNot(psiElement().inside(ODTDefineStatement.class));
    protected static final String COMMAND_PARAMETER_TEMPLATE = JAVADOC_START +
            " * @param $param (string)\n" +
            JAVADOC_END +
            "DEFINE COMMAND commandWithParameter($param) => { RETURN $param; }";

    public OMTInjectableSectionCompletion() {

        extend(CompletionType.BASIC, psiElement(), new CompletionProvider<>() {

            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {

                PsiLanguageInjectionHost injectionHost = OMTODTInjectionUtil.getInjectionHost(parameters.getPosition());
                PsiFile originalFile = parameters.getOriginalFile();

                if (injectionHost != null && originalFile instanceof ODTFile) {
                    ODTSharedCompletion.sharedContext.set(context.getSharedContext());
                    YamlMetaType injectionMetaType = OMTODTInjectionUtil.getInjectionMetaType(parameters.getPosition());
                    addInjectableSectionCompletions(parameters, result, injectionMetaType, (ODTFile) originalFile);
                }
            }
        });
    }

    private void addInjectableSectionCompletions(@NotNull CompletionParameters parameters,
                                                 @NotNull CompletionResultSet result,
                                                 YamlMetaType injectionMetaType,
                                                 ODTFile originalFile) {

        PsiElement element = parameters.getPosition();
        if (injectionMetaType instanceof OMTQueriesMetaType) {
            insertQueryTemplate(result, originalFile, element);
        } else if (injectionMetaType instanceof OMTCommandsMetaType) {
            insertCommandTemplate(element, originalFile, result);
        } else if (injectionMetaType instanceof OMTShapeQueryType) {
            ODTCommandCompletionNewGraph.addShapeCompletions(parameters, result);
            result.stopHere();
        } else if (injectionMetaType instanceof OMTBooleanQueryType) {
            insertBooleanQuery(result);
        }
    }

    private void insertBooleanQuery(@NotNull CompletionResultSet result) {
        ODTSharedCompletion.sharedContext.get().put(
                ODTSharedCompletion.TYPE_FILTER,
                resources -> OppModel.getInstance().areCompatible(Collections.singleton(OppModelConstants.getXsdBooleanInstance()), resources));
        result.addElement(PrioritizedLookupElement.withPriority(
                LookupElementBuilder.create("true"), 100
        ));
        result.addElement(PrioritizedLookupElement.withPriority(
                LookupElementBuilder.create("false"), 100
        ));
    }

    private void insertQueryTemplate(@NotNull CompletionResultSet result, ODTFile originalFile, PsiElement element) {
        ODTSharedCompletion.sharedContext.get().put(ODTSharedCompletion.CALLABLE_FILTER, callable -> !callable.isCommand());

        // check if the caret is located outside of an DEFINE QUERY statement, only then show the template
        // otherwise, the completion provider of ODT will take over to create completions for the current query
        if (OUTSIDE_STATEMENT.accepts(element)) {
            result.addElement(LookupElementBuilder.create(withIndentation(QUERY_SIMPLE_TEMPLATE, originalFile))
                    .withPresentableText("DEFINE QUERY simpleQuery")
                    .withTypeText(CODE_SNIPPET));
            result.addElement(LookupElementBuilder.create(withIndentation(QUERY_PARAMETER_TEMPLATE, originalFile))
                    .withPresentableText("DEFINE QUERY queryWithParameter($param)")
                    .withTypeText(CODE_SNIPPET));
            result.addElement(LookupElementBuilder.create(withIndentation(QUERY_BASE_TEMPLATE, originalFile))
                    .withPresentableText("DEFINE QUERY queryWithBase")
                    .withTypeText(CODE_SNIPPET));

            result.stopHere();
        }
    }

    private void insertCommandTemplate(PsiElement element,
                                       ODTFile containingFile,
                                       CompletionResultSet result) {
        if (OUTSIDE_STATEMENT.accepts(element)) {
            result.addElement(LookupElementBuilder.create(withIndentation(COMMAND_SIMPLE_TEMPLATE, containingFile))
                    .withPresentableText("DEFINE COMMAND simpleCommand")
                    .withTypeText(CODE_SNIPPET));
            result.addElement(LookupElementBuilder.create(withIndentation(COMMAND_PARAMETER_TEMPLATE, containingFile))
                    .withPresentableText("DEFINE COMMAND commandWithParameter($param)")
                    .withTypeText(CODE_SNIPPET));
            result.stopHere();
        }
    }

    private String withIndentation(String template, ODTFile containingFile) {
        String indent = Strings.repeat(" ", containingFile.getLineOffsetInParent());
        return template.replace("\n", "\n" + indent);
    }

}
