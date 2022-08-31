package com.misset.opp.omt.completion;

import com.google.common.base.Strings;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.Key;
import com.intellij.patterns.PsiElementPattern;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.util.ProcessingContext;
import com.intellij.util.SharedProcessingContext;
import com.misset.opp.odt.completion.commands.ODTCommandCompletionNewGraph;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import com.misset.opp.omt.injection.InjectableContentType;
import com.misset.opp.omt.injection.InjectionHost;
import com.misset.opp.omt.injection.OMTODTInjectionUtil;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

import static com.intellij.patterns.PlatformPatterns.psiElement;

/**
 * Certain injection segments have specific completions (templates).
 * Since this is driven by the OMT structure where the ODT fragment resides, it is considered
 * an OMT/ODT completion and not purely an ODT completion.
 */
public class OMTODTInjectableSectionCompletion extends CompletionContributor {
    /**
     * Type-filter to filter to completions
     */
    public static final Key<Predicate<Set<OntResource>>> TYPE_FILTER = new Key<>("TYPE_FILTER");

    /**
     * Type-filter to filter callables by type (Operators/Commands);
     */
    public static final Key<Predicate<Callable>> CALLABLE_FILTER = new Key<>("CALLABLE_FILTER");
    protected static final String QUERY_SIMPLE_TEMPLATE = "DEFINE QUERY simpleQuery => 'Hello world';";
    public static final AtomicReference<SharedProcessingContext> sharedContext = new AtomicReference<>();
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

    public OMTODTInjectableSectionCompletion() {

        extend(CompletionType.BASIC, psiElement(), new CompletionProvider<>() {

            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {

                PsiLanguageInjectionHost injectionHost = OMTODTInjectionUtil.getInjectionHost(parameters.getPosition());
                PsiFile originalFile = parameters.getOriginalFile();
                if (injectionHost instanceof InjectionHost && originalFile instanceof ODTFile) {
                    // set a new sharedContext
                    sharedContext.set(context.getSharedContext());
                    addInjectableSectionCompletions(parameters, result, ((InjectionHost) injectionHost).getInjectableContentType(), (ODTFile) originalFile);
                }
            }
        });
    }

    private void addInjectableSectionCompletions(@NotNull CompletionParameters parameters,
                                                 @NotNull CompletionResultSet result,
                                                 InjectableContentType injectableContentType,
                                                 ODTFile originalFile) {
        if (injectableContentType.isQueryStatement()) {
            sharedContext.get().put(CALLABLE_FILTER, callable -> !callable.isCommand());
        }
        PsiElement element = parameters.getPosition();
        switch (injectableContentType) {
            case QUERY_BLOCK:
                if (insertQueryTemplate(element, originalFile, result)) {
                    result.stopHere();
                }
                break;
            case COMMAND_BLOCK:
                if (insertCommandTemplate(element, originalFile, result)) {
                    result.stopHere();
                }
                break;
            case GRAPH_SHAPE_QUERY:
                ODTCommandCompletionNewGraph.addShapeCompletions(parameters, result);
                result.stopHere();
                break;
            case BOOLEAN_QUERY:
                result.addElement(PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create("true"), 100
                ));
                result.addElement(PrioritizedLookupElement.withPriority(
                        LookupElementBuilder.create("false"), 100
                ));
                sharedContext.get().put(TYPE_FILTER, resources -> OppModel.getInstance().areCompatible(Collections.singleton(OppModelConstants.getXsdBooleanInstance()), resources));
                break;
            case NONE:
            default:
        }
    }

    private boolean insertQueryTemplate(PsiElement element,
                                        ODTFile containingFile,
                                        CompletionResultSet resultSet) {
        if (OUTSIDE_STATEMENT.accepts(element)) {
            resultSet.addElement(LookupElementBuilder.create(withIndentation(QUERY_SIMPLE_TEMPLATE, containingFile))
                    .withPresentableText("DEFINE QUERY simpleQuery")
                    .withTypeText(CODE_SNIPPET));
            resultSet.addElement(LookupElementBuilder.create(withIndentation(QUERY_PARAMETER_TEMPLATE, containingFile))
                    .withPresentableText("DEFINE QUERY queryWithParameter($param)")
                    .withTypeText(CODE_SNIPPET));
            resultSet.addElement(LookupElementBuilder.create(withIndentation(QUERY_BASE_TEMPLATE, containingFile))
                    .withPresentableText("DEFINE QUERY queryWithBase")
                    .withTypeText(CODE_SNIPPET));
            return true;
        }
        return false;
    }

    private boolean insertCommandTemplate(PsiElement element,
                                          ODTFile containingFile,
                                          CompletionResultSet resultSet) {
        if (OUTSIDE_STATEMENT.accepts(element)) {
            resultSet.addElement(LookupElementBuilder.create(withIndentation(COMMAND_SIMPLE_TEMPLATE, containingFile))
                    .withPresentableText("DEFINE COMMAND simpleCommand")
                    .withTypeText(CODE_SNIPPET));
            resultSet.addElement(LookupElementBuilder.create(withIndentation(COMMAND_PARAMETER_TEMPLATE, containingFile))
                    .withPresentableText("DEFINE COMMAND commandWithParameter($param)")
                    .withTypeText(CODE_SNIPPET));
            return true;
        }
        return false;
    }

    private String withIndentation(String template, ODTFile containingFile) {
        String indent = Strings.repeat(" ", OMTODTInjectionUtil.getMinimalLineOffset(containingFile));
        return template.replace("\n", "\n" + indent);
    }

}
