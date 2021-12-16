package com.misset.opp.odt.completion.commands;

import com.intellij.codeInsight.completion.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.callable.builtin.commands.AssignCommand;
import com.misset.opp.odt.psi.ODTCommandCall;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.misset.opp.odt.completion.CompletionPatterns.getInsideBuiltinCommandSignaturePattern;

public class ODTCommandCompletionAssign extends CompletionContributor {


    public ODTCommandCompletionAssign() {
        extend(CompletionType.BASIC, getInsideBuiltinCommandSignaturePattern(AssignCommand.INSTANCE), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                // get the CommandCall:
                PsiElement element = parameters.getPosition();
                ODTCommandCall assignCommand = PsiTreeUtil.getParentOfType(element, ODTCommandCall.class);
                if (assignCommand == null) {
                    return;
                }

                int argumentIndexOf = assignCommand.getArgumentIndexOf(element);
                if (argumentIndexOf % 2 != 0 && argumentIndexOf > 0) {
                    // predicate position, show all predicates for this command:
                    Set<OntResource> subject = assignCommand.resolveSignatureArgument(0);
                    if (subject.isEmpty()) {
                        return;
                    }

                    Set<Resource> existingPredicates = existingPredicates(assignCommand)
                            .stream()
                            .map(RDFNode::asResource)
                            .collect(Collectors.toSet());
                    Set<Property> predicates = OppModel.INSTANCE.listPredicates(subject)
                            .stream()
                            .filter(property -> !OppModel.INSTANCE.classModelProperties.contains(property))
                            .filter(property -> !existingPredicates.contains(property))
                            .collect(Collectors.toSet());

                    ODTFile file = (ODTFile) parameters.getOriginalFile();
                    Map<String, String> availableNamespaces = file.getAvailableNamespaces();

                    predicates.stream()
                            .map(property -> TTLResourceUtil.getRootLookupElement(
                                    property, "Predicate", availableNamespaces))
                            .filter(Objects::nonNull)
                            .forEach(result::addElement);
                    result.stopHere();
                }
            }

            private Set<OntResource> existingPredicates(ODTCommandCall call) {
                Set<OntResource> predicates = new HashSet<>();
                for (int i = 1; i < call.numberOfArguments(); i = i + 2) {
                    predicates.addAll(call.resolveSignatureArgument(i));
                }
                return predicates;
            }
        });
    }
}
