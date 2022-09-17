package com.misset.opp.odt.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.util.ProcessingContext;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.util.OntologyResourceUtil;
import com.misset.opp.odt.psi.ODTFile;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class ODTAnnotationTypeClassNameCompletion extends CompletionContributor {
    public ODTAnnotationTypeClassNameCompletion() {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement().inside(PsiDocTag.class), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {
                // get the CommandCall:
                PsiElement position = parameters.getPosition();
                if (position.getParent() instanceof PsiDocTag && position.getText().startsWith("(") && position.getText().endsWith(")")) {
                    String name = ((PsiDocTag) position.getParent()).getName();
                    if (Set.of("param", "base").contains(name)) {
                        addClassCompletions(parameters, result);
                    }
                }
            }
        });
    }

    private void addClassCompletions(@NotNull CompletionParameters parameters,
                                     @NotNull CompletionResultSet result) {
        ODTFile file = (ODTFile) parameters.getOriginalFile();
        Map<String, String> availableNamespaces = file.getAvailableNamespaces();

        // show all classes instances:
        OntologyModel.getInstance().listClasses().stream()
                .map(resource -> OntologyResourceUtil.getTypeLookupElement(resource, availableNamespaces))
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
