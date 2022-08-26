package com.misset.opp.odt.completion.commands;

import com.intellij.codeInsight.completion.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.builtin.commands.NewGraphCommand;
import com.misset.opp.odt.psi.ODTCommandCall;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.misset.opp.odt.completion.CompletionPatterns.getInsideBuiltinCommandSignaturePattern;

public class ODTCommandCompletionNewGraph extends CompletionContributor {


    public ODTCommandCompletionNewGraph() {
        extend(CompletionType.BASIC, getInsideBuiltinCommandSignaturePattern(NewGraphCommand.INSTANCE), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters, @NotNull ProcessingContext context, @NotNull CompletionResultSet result) {
                // get the CommandCall:
                PsiElement element = parameters.getPosition();
                ODTCommandCall newGraphCommand = PsiTreeUtil.getParentOfType(element, ODTCommandCall.class);
                if (newGraphCommand != null) {
                    int argumentIndexOf = newGraphCommand.getArgumentIndexOf(element);
                    if (argumentIndexOf == 0) {
                        addShapeCompletions(parameters, result);
                        result.stopHere();
                    }
                }
            }
        });
    }

    public static void addShapeCompletions(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        ODTFile file = (ODTFile) parameters.getOriginalFile();
        Map<String, String> availableNamespaces = file.getAvailableNamespaces();

        // show all graphshape instances:
        OppModelConstants.GRAPH_SHAPE.listInstances().mapWith(
                        resource -> TTLResourceUtil
                                .getRootLookupElement(resource, "Graphshape", availableNamespaces))
                .forEachRemaining(result::addElement);
    }
}
