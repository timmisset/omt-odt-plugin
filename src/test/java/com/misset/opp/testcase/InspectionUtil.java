package com.misset.opp.testcase;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.testFramework.fixtures.JavaCodeInsightTestFixture;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class InspectionUtil {

    private final JavaCodeInsightTestFixture fixture;

    public InspectionUtil(JavaCodeInsightTestFixture fixture) {
        this.fixture = fixture;
    }

    public void assertNoErrors() {
        assertNoHighlighting(HighlightSeverity.ERROR);
    }

    public void assertNoWarnings() {
        assertNoHighlighting(HighlightSeverity.WARNING);
    }

    public void assertNoWarning(String warning) {
        assertNoHighlighting(HighlightSeverity.WARNING, warning);
    }

    public void assertNoWeakWarning(String warning) {
        assertNoHighlighting(HighlightSeverity.WEAK_WARNING, warning);
    }

    public void assertNoError(String error) {
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

    public void assertHasError(String message) {
        assertHasHighlightingMessage(HighlightSeverity.ERROR, message);
    }

    public void assertHasWarning(String message) {
        assertHasHighlightingMessage(HighlightSeverity.WARNING, message);
    }

    public void assertHasWarning(String message, String highlightingText) {
        assertHasHighlightingMessage(HighlightSeverity.WARNING, message, highlightingText);
    }

    public void assertHasWeakWarning(String message) {
        assertHasHighlightingMessage(HighlightSeverity.WEAK_WARNING, message);
    }

    public void assertHasInformation(String message) {
        assertHasHighlightingMessage(HighlightSeverity.INFORMATION, message);
    }

    private void assertHasHighlightingMessage(HighlightSeverity severity, String message) {
        assertHasHighlightingMessage(severity, message, null);
    }

    private void assertHasHighlightingMessage(HighlightSeverity severity, String message, String highlightText) {
        final List<HighlightInfo> highlighting = getHighlighting(severity);
        assertTrue(
                allHighlightingAsMessage(),
                highlighting.stream().anyMatch(
                        highlightInfo -> hasHighlightingDescription(highlightInfo, message, highlightText)
                ));
    }

    private boolean hasHighlightingDescription(HighlightInfo highlightInfo, String description, String highlightText) {
        if (highlightText != null && !highlightInfo.getText().equals(highlightText)) {
            return false;
        }
        return highlightInfo.getDescription() != null &&
                highlightInfo.getDescription().startsWith(description);
    }

    private List<HighlightInfo> getHighlighting(HighlightSeverity severity) {
        return getHighlighting().stream().filter(
                highlightInfo -> highlightInfo.getSeverity() == severity
        ).collect(Collectors.toList());
    }

    private String allHighlightingAsMessage() {
        return getHighlighting().stream()
                .map(highlightInfo -> String.format(
                        "%s: %s", highlightInfo.getSeverity().myName, highlightInfo.getDescription()
                )).collect(Collectors.joining("\n"));
    }

    private @NotNull List<HighlightInfo> getHighlighting() {
        try {
            return fixture.doHighlighting();
        } catch (IllegalStateException e) {
            throw new RuntimeException("Don't wrap highlight assertions in ReadAction.read locks");
        }
    }

    public @NotNull List<IntentionAction> getAllQuickFixes() {
        final String fileName = fixture.getFile().getName();
        return
                Stream.of(
                                fixture.getAllQuickFixes(fileName),
                                fixture.getAvailableIntentions(fileName))
                        .flatMap(Collection::stream)
                        .distinct()
                        .collect(Collectors.toList());
    }

    public IntentionAction getQuickFixIntention(String description) {
        return getAllQuickFixes()
                .stream()
                .filter(intentionAction -> intentionAction.getText().equals(description))
                .findFirst()
                .orElse(null);
    }

    public List<IntentionAction> getQuickFixIntentions(String description) {
        return getAllQuickFixes()
                .stream()
                .filter(intentionAction -> intentionAction.getText().equals(description))
                .collect(Collectors.toList());
    }

    public void invokeQuickFixIntention(String description) {
        final IntentionAction quickFixIntention = getQuickFixIntention(description);
        if (quickFixIntention == null) {
            fail("Could not find quickfix with description " + description);
        }
        invokeQuickFixIntention(quickFixIntention);
    }

    public void invokeQuickFixIntention(IntentionAction intentionAction) {
        WriteCommandAction.runWriteCommandAction(
                fixture.getProject(), () -> intentionAction.invoke(fixture.getProject(), fixture.getEditor(), fixture.getFile())
        );
    }

}
