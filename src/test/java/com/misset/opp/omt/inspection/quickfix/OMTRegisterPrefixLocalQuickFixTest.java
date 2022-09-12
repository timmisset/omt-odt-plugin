package com.misset.opp.omt.inspection.quickfix;

import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTCurieElement;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class OMTRegisterPrefixLocalQuickFixTest extends OMTTestCase {
    @Test
    void registerPrefixCreateNewBlock() {
        final ProblemDescriptor problemDescriptor = mock(ProblemDescriptor.class);
        configureByText("" +
                "queries: |\n" +
                "   DEFINE QUERY <caret>query => /ont:ClassA");
        final ODTCurieElement curieElementStep = ReadAction.compute(() ->
                PsiTreeUtil.findChildOfType(myFixture.getElementAtCaret().getParent(), ODTCurieElement.class));
        doReturn(curieElementStep).when(problemDescriptor).getPsiElement();

        final OMTRegisterPrefixLocalQuickFix odtRegisterPrefixLocalQuickFix = new OMTRegisterPrefixLocalQuickFix(
                "ont",
                "http://ontology#");

        WriteCommandAction.runWriteCommandAction(getProject(), () -> odtRegisterPrefixLocalQuickFix.applyFix(getProject(), problemDescriptor));

        // due to the caret position, the active file is the inject file fragment
        ReadAction.run(() -> {
            ODTFile file = (ODTFile) getFile();
            final OMTFile omtFile = (OMTFile) InjectedLanguageManager.getInstance(getProject())
                    .getInjectionHost(file)
                    .getContainingFile();

            // confirm that the file now has a prefixes block and a key "ont"
            Optional.ofNullable(PsiTreeUtil.findChildOfType(omtFile, YAMLMapping.class))
                    .map(mapping -> mapping.getKeyValueByKey("prefixes"))
                    .map(YAMLKeyValue::getValue)
                    .map(value -> (YAMLMapping)value)
                    .map(mapping -> mapping.getKeyValueByKey("ont"))
                    .ifPresentOrElse(prefix -> Assertions.assertEquals("<http://ontology#>", prefix.getValueText()),
                            Assertions::fail);
        });


    }

    @Test
    void registerPrefixRegisterInExistingBlock() {
        final ProblemDescriptor problemDescriptor = mock(ProblemDescriptor.class);
        configureByText(insideQueryWithPrefixes("/<caret>prefix:ClassA"));
        final ODTCurieElement curieElementStep = (ODTCurieElement) ReadAction.compute(() ->
                myFixture.getElementAtCaret().getParent());
        doReturn(curieElementStep).when(problemDescriptor).getPsiElement();

        final OMTRegisterPrefixLocalQuickFix odtRegisterPrefixLocalQuickFix = new OMTRegisterPrefixLocalQuickFix(
                "prefix",
                "http://namespace#");

        WriteCommandAction.runWriteCommandAction(getProject(), () -> odtRegisterPrefixLocalQuickFix.applyFix(getProject(), problemDescriptor));

        // due to the caret position, the active file is the inject file fragment
        ReadAction.run(() -> {
            ODTFile file = (ODTFile) getFile();
            final OMTFile omtFile = (OMTFile) InjectedLanguageManager.getInstance(getProject())
                    .getInjectionHost(file)
                    .getContainingFile();

            // confirm that the file now has a prefixes block and a key "ont"
            Optional.ofNullable(PsiTreeUtil.findChildOfType(omtFile, YAMLMapping.class))
                    .map(mapping -> mapping.getKeyValueByKey("prefixes"))
                    .map(YAMLKeyValue::getValue)
                    .map(value -> (YAMLMapping)value)
                    .map(mapping -> mapping.getKeyValueByKey("prefix"))
                    .ifPresentOrElse(prefix ->
                                    Assertions.assertEquals("<http://namespace#>", prefix.getValueText()),
                            Assertions::fail);
        });


    }
}
