package com.misset.opp.omt.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.patterns.PatternCondition;
import com.intellij.psi.PsiElement;
import com.intellij.util.ProcessingContext;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.variables.OMTParamMetaType;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class OMTParameterShorthandTypeCompletion extends CompletionContributor {

    /*
        ENCAPSULATED_TOKEN will check if the caret is located between the parenthesis
     */
    private static final Pattern ENCAPSULATED_TOKEN = Pattern.compile("\\([^\\)]*IntellijIdeaRulezzz[^\\)]*\\)");
    /*
        PREFIX_MATCHER will match the prefix: part, including colon, of an existing value
        It takes the position of the caret into consideration
     */
    private static final Pattern PREFIX_MATCHER = Pattern.compile("\\(([^<]*)IntellijIdeaRulezzz");

    public OMTParameterShorthandTypeCompletion() {
        extend(CompletionType.BASIC, psiElement().with(new PatternCondition<>("Command completion") {
            @Override
            public boolean accepts(@NotNull PsiElement element, ProcessingContext context) {
                YamlMetaType resolvedMetaType = OMTMetaTypeProvider.getInstance(element.getProject()).getResolvedMetaType(element);
                return resolvedMetaType instanceof OMTParamMetaType;
            }
        }), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {
                // check if the token is between the parenthesis of the text:
                // $someVariable <caret>(ont:Type)  ==> false
                // $someVariable (<caret>ont:Type)  ==> true
                if (ENCAPSULATED_TOKEN.matcher(parameters.getPosition().getText()).find()) {
                    Matcher matcher = PREFIX_MATCHER.matcher(parameters.getPosition().getText());
                    String prefixMatcher = matcher.find() ? matcher.group(1) : "";
                    addClassCompletions(parameters, result.withPrefixMatcher(prefixMatcher));
                }
            }
        });
    }

    private void addClassCompletions(@NotNull CompletionParameters parameters,
                                     @NotNull CompletionResultSet result) {
        OMTFile file = (OMTFile) parameters.getOriginalFile();
        Map<String, String> availableNamespaces = file.getAvailableNamespaces(parameters.getPosition());

        // show all classes instances:
        OppModel.getInstance().listClasses().stream()
                .map(resource -> TTLResourceUtil.getTypeLookupElement(resource, availableNamespaces))
                .filter(Objects::nonNull)
                // sort so that items with prefixes are shown first
                .map(lookupElementBuilder -> PrioritizedLookupElement.withPriority(
                        lookupElementBuilder,
                        lookupElementBuilder.getLookupString().startsWith("<") ? 0 : 1
                ))
                .forEach(result::addElement);

        result.stopHere();
    }
}
