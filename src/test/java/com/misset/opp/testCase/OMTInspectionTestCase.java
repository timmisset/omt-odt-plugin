package com.misset.opp.testCase;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class OMTInspectionTestCase extends OMTTestCase {

    protected abstract Collection<Class<? extends LocalInspectionTool>> getEnabledInspections();

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        myFixture.enableInspections(getEnabledInspections());
    }

    protected void assertNoErrors() {
        assertNoHighlighting(HighlightSeverity.ERROR);
    }

    protected void assertNoWarnings() {
        assertNoHighlighting(HighlightSeverity.WARNING);
    }

    protected void assertNoWarning(String warning) {
        assertNoHighlighting(HighlightSeverity.WARNING, warning);
    }

    protected void assertNoWeakWarning(String warning) {
        assertNoHighlighting(HighlightSeverity.WEAK_WARNING, warning);
    }

    protected void assertNoError(String error) {
        assertNoHighlighting(HighlightSeverity.ERROR, error);
    }

    private void assertNoHighlighting(HighlightSeverity severity, String message) {
        assertTrue("Highlighting not wanted but present: " + message, getHighlighting(severity).stream().noneMatch(
                highlightInfo -> highlightInfo.getDescription().startsWith(message)
        ));
    }

    private void assertNoHighlighting(HighlightSeverity severity) {
        final List<HighlightInfo> highlighting = getHighlighting(severity);
        if (!highlighting.isEmpty()) {
            ReadAction.run(() -> fail(String.format("All %s highlighting%n%s", severity.myName, highlighting)));
        }
    }

    protected void assertHasError(String message) {
        assertHasHighlightingMessage(HighlightSeverity.ERROR, message);
    }

    protected void assertHasWarning(String message) {
        assertHasHighlightingMessage(HighlightSeverity.WARNING, message);
    }

    protected void assertHasWeakWarning(String message) {
        assertHasHighlightingMessage(HighlightSeverity.WEAK_WARNING, message);
    }

    protected void assertHasInformation(String message) {
        assertHasHighlightingMessage(HighlightSeverity.INFORMATION, message);
    }

    private void assertHasHighlightingMessage(HighlightSeverity severity, String message) {
        final List<HighlightInfo> highlighting = getHighlighting(severity);
        assertTrue(
                allHighlightingAsMessage(),
                highlighting.stream().anyMatch(
                        highlightInfo -> highlightInfo.getDescription() != null && highlightInfo.getDescription().startsWith(message)
                ));
    }

    private List<HighlightInfo> getHighlighting(HighlightSeverity severity) {
        return myFixture.doHighlighting().stream().filter(
                highlightInfo -> highlightInfo.getSeverity() == severity
        ).collect(Collectors.toList());
    }

    private String allHighlightingAsMessage() {
        return myFixture.doHighlighting().stream()
                .map(highlightInfo -> String.format(
                        "%s: %s", highlightInfo.getSeverity().myName, highlightInfo.getDescription()
                )).collect(Collectors.joining("\n"));
    }

    protected @NotNull List<IntentionAction> getAllQuickFixes() {
        final String fileName = getFile().getName();
        return
                Stream.of(
                                myFixture.getAllQuickFixes(fileName),
                                myFixture.getAvailableIntentions(fileName))
                        .flatMap(Collection::stream)
                        .distinct()
                        .collect(Collectors.toList());
    }

    protected IntentionAction getQuickFixIntention(String description) {
        return getAllQuickFixes()
                .stream()
                .filter(intentionAction -> intentionAction.getText().equals(description))
                .findFirst()
                .orElse(null);
    }

    protected List<IntentionAction> getQuickFixIntentions(String description) {
        return getAllQuickFixes()
                .stream()
                .filter(intentionAction -> intentionAction.getText().equals(description))
                .collect(Collectors.toList());
    }

    protected void invokeQuickFixIntention(String description) {
        final IntentionAction quickFixIntention = getQuickFixIntention(description);
        if (quickFixIntention == null) {
            fail("Could not find quickfix with description " + description);
        }
        invokeQuickFixIntention(quickFixIntention);
    }

    protected void invokeQuickFixIntention(IntentionAction intentionAction) {
        WriteCommandAction.runWriteCommandAction(
                getProject(), () -> intentionAction.invoke(getProject(), getEditor(), getFile())
        );
    }
}
