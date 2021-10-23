package com.misset.opp.testCase;

import com.intellij.codeInsight.daemon.impl.HighlightInfo;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.application.ReadAction;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class InspectionTestCase extends OMTTestCase {

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
                        highlightInfo -> highlightInfo.getDescription().startsWith(message)
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

}
