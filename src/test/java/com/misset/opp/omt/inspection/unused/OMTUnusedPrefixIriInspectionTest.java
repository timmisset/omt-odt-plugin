package com.misset.opp.omt.inspection.unused;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTUnusedPrefixIriInspectionTest extends OMTInspectionTestCase {
    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTUnusedPrefixIriInspection.class);
    }

    @Test
    void testOMTPrefixUnused() {
        String content = "prefixes:\n" +
                "   ont: <http://ontology/>";
        configureByText(content);
        assertHasWarning("ont is never used");
    }

    @Test
    void testOMTPrefixUsed() {
        String content = "prefixes:\n" +
                "   ont: <http://ontology/>\n" +
                "\n" +
                "queries: |\n" +
                "   DEFINE QUERY query => ont:ClassA;\n" +
                "";
        configureByText(content);
        assertNoWarning("ont is never used");
    }

    @Test
    void testOMTPrefixUnusedRemoveViaLocalQuickFix() {
        String content = "prefixes:\n" +
                "   <caret>ont: <http://ontology/>";
        configureByText(content);
        IntentionAction remove_prefix = getQuickFixIntention("Remove prefix");
        WriteCommandAction.runWriteCommandAction(getProject(), () -> remove_prefix.invoke(getProject(), getEditor(), getFile()));
        String contentAfterRemoval = ReadAction.compute(getFile()::getText);
        Assertions.assertEquals("prefixes:\n" +
                "   ", contentAfterRemoval);
    }

    @Test
    void testOMTPrefixUnusedRemoveWithRemainingItemsViaLocalQuickFix() {
        String content = "prefixes:\n" +
                "   <caret>ont: <http://ontology/>\n" +
                "   another: <http://ontology/>";
        configureByText(content);
        IntentionAction remove_prefix = getQuickFixIntention("Remove prefix");
        WriteCommandAction.runWriteCommandAction(getProject(), () -> remove_prefix.invoke(getProject(), getEditor(), getFile()));
        String contentAfterRemoval = ReadAction.compute(getFile()::getText);
        Assertions.assertEquals("prefixes:\n" +
                "  another: <http://ontology/>", contentAfterRemoval);
    }
}
