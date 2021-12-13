package com.misset.opp.odt.completion.commands;

import com.intellij.codeInsight.completion.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.callable.builtin.commands.NewGraphCommand;
import com.misset.opp.odt.completion.ODTTraverseCompletion;
import com.misset.opp.odt.psi.ODTCommandCall;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.ttl.OppModel;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.misset.opp.odt.completion.CompletionPatterns.getInsideBuiltinCommandSignaturePattern;
import static com.misset.opp.odt.completion.ODTTraverseCompletion.TraverseDirection.FORWARD;

public class ODTCommandCompletionNewGraph extends CompletionContributor {


    public ODTCommandCompletionNewGraph() {
        extend(CompletionType.BASIC, getInsideBuiltinCommandSignaturePattern(NewGraphCommand.INSTANCE), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                // get the CommandCall:
                PsiElement element = parameters.getPosition();
                ODTCommandCall newGraphCommand = PsiTreeUtil.getParentOfType(element, ODTCommandCall.class);
                if (newGraphCommand == null) {
                    return;
                }

                int argumentIndexOf = newGraphCommand.getArgumentIndexOf(element);
                if (argumentIndexOf == 0) {
                    ODTFile file = (ODTFile) parameters.getOriginalFile();
                    Map<String, String> availableNamespaces = file.getAvailableNamespaces();

                    // show all graphshape instances:
                    OppModel.INSTANCE.GRAPH_SHAPE.listInstances().mapWith(
                                    resource -> ODTTraverseCompletion
                                            .getPredicateLookupElement(resource, FORWARD, availableNamespaces, true))
                            .forEachRemaining(result::addElement);

                    result.stopHere();
                }
            }
        });
    }
}
