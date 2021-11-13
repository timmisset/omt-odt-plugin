package com.misset.opp.odt.inspection.quikfix;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.resolvable.queryStep.traverse.ODTResolvableCurieElementStep;
import com.misset.opp.testCase.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class ODTRegisterPrefixLocalQuickFixTest extends ODTTestCase {

    @Test
    void registerPrefix() {
        final ProblemDescriptor problemDescriptor = mock(ProblemDescriptor.class);
        final ODTFile file = configureByText("/ont:ClassA");
        final ODTResolvableCurieElementStep curieElementStep = ReadAction.compute(() -> PsiTreeUtil.findChildOfType(file,
                ODTResolvableCurieElementStep.class));
        doReturn(curieElementStep).when(problemDescriptor).getPsiElement();

        final ODTRegisterPrefixLocalQuickFix odtRegisterPrefixLocalQuickFix = new ODTRegisterPrefixLocalQuickFix(
                "ont",
                "http://ontology#");

        WriteCommandAction.runWriteCommandAction(getProject(), () -> odtRegisterPrefixLocalQuickFix.applyFix(getProject(), problemDescriptor));

        final String content = ReadAction.compute(() -> getFile().getText());
        // todo: indentation is not yet correct, acceptable for now, fix when formatter is implemented
        Assertions.assertEquals("PREFIX ont: <http://ontology#>\n" +
                "        /ont:ClassA", content);
    }

}
