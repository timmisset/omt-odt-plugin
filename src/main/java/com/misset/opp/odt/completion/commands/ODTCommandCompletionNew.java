package com.misset.opp.odt.completion.commands;

import com.intellij.codeInsight.completion.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import com.misset.opp.odt.builtin.commands.NewCommand;
import com.misset.opp.odt.psi.ODTCommandCall;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

import static com.misset.opp.odt.completion.CompletionPatterns.getInsideBuiltinCommandSignaturePattern;

public class ODTCommandCompletionNew extends CompletionContributor {
    public ODTCommandCompletionNew() {
        extend(CompletionType.BASIC, getInsideBuiltinCommandSignaturePattern(NewCommand.INSTANCE), new CompletionProvider<>() {
            @Override
            protected void addCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull ProcessingContext context,
                                          @NotNull CompletionResultSet result) {
                // get the CommandCall:
                PsiElement element = parameters.getPosition();
                ODTCommandCall newGraphCommand = PsiTreeUtil.getParentOfType(element, ODTCommandCall.class);
                if (newGraphCommand != null) {
                    addNewCommandCompletions(parameters, result, element, newGraphCommand);
                }
            }
        });
    }

    private void addNewCommandCompletions(@NotNull CompletionParameters parameters,
                                          @NotNull CompletionResultSet result,
                                          PsiElement element,
                                          ODTCommandCall newGraphCommand) {
        int argumentIndexOf = newGraphCommand.getArgumentIndexOf(element);
        if (argumentIndexOf == 0) {
            ODTFile file = (ODTFile) parameters.getOriginalFile();
            Map<String, String> availableNamespaces = file.getAvailableNamespaces();

            // show all classes instances:
            OppModel.getInstance().listClasses().stream().map(
                            resource -> TTLResourceUtil
                                    .getRootLookupElement(resource, "Class", availableNamespaces))
                    .filter(Objects::nonNull)
                    .forEach(result::addElement);

            result.stopHere();
        }
    }
}
