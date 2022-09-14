package com.misset.opp.odt.completion;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ODTTypeClassNameCompletion extends CompletionContributor {

    private static final String RDF_TYPE_INSERT = " / ^rdf:type / ";

    public ODTTypeClassNameCompletion() {
        extend(CompletionType.BASIC, CompletionPatterns.FIRST_QUERY_STEP_ABSOLUTE, new CompletionProvider<>() {
            private final InsertHandler<LookupElement> withInsertHandler = (insertionContext, lookupElement) -> {
                insertionContext.commitDocument();
                Editor editor = insertionContext.getEditor();
                int offset = editor.getCaretModel().getOffset();
                PsiElement elementAt = insertionContext.getFile().findElementAt(offset);
                if (elementAt == null || PsiTreeUtil.nextVisibleLeaf(elementAt) == null) {
                    // insert reverse predicate:
                    insertionContext.getDocument().insertString(offset, RDF_TYPE_INSERT);
                    insertionContext.commitDocument();
                    editor.getCaretModel().moveCaretRelatively(RDF_TYPE_INSERT.length(), 0, false, false, false);
                    AutoPopupController.getInstance(insertionContext.getProject()).scheduleAutoPopup(editor, CompletionType.BASIC, f -> true);
                }
            };

            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {
                ODTFile file = (ODTFile) parameters.getOriginalFile();
                Map<String, String> availableNamespaces = file.getAvailableNamespaces();

                OppModel oppModel = OppModel.getInstance();
                List<LookupElementBuilder> elements = new ArrayList<>();

                addElements(getFilteredCollection(oppModel.listClasses(), this::filterClasses), resource -> "Class", availableNamespaces, elements);

                CompletionResultSet resultSet = result.withPrefixMatcher("/" + result.getPrefixMatcher().getPrefix());

                elements.stream()
                        .map(element -> element.withInsertHandler(withInsertHandler))
                        .map(element -> PrioritizedLookupElement.withPriority(element, element.getLookupString().startsWith("<") ? 0 : 1))
                        .forEach(resultSet::addElement);

                result.stopHere();
            }

            private <T extends OntResource> Collection<T> getFilteredCollection(Collection<T> collection,
                                                                                Predicate<T> filter) {
                return collection.stream().filter(filter).collect(Collectors.toSet());
            }

            private boolean filterClasses(OntClass ontClass) {
                return !ontClass.getNameSpace().equals(OppModelConstants.getXsdBoolean().getNameSpace());
            }
        });
    }

    private void addElements(
            Collection<? extends OntResource> resources,
            Function<OntResource, String> typeText,
            Map<String, String> availableNamespaces,
            Collection<LookupElementBuilder> elements) {
        resources.stream().map(
                        resource -> TTLResourceUtil
                                .getRootLookupElement(resource, typeText.apply(resource), availableNamespaces))
                .filter(Objects::nonNull)
                .forEach(elements::add);
    }
}
