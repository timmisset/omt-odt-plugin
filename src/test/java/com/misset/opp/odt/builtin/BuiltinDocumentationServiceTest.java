package com.misset.opp.odt.builtin;

import com.misset.opp.odt.builtin.operators.LogOperator;
import com.misset.opp.settings.SettingsState;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class BuiltinDocumentationServiceTest extends OMTTestCase {

    @Test
    void testGetDocumentationIncludesInformationFromTheAPI() {
        LogOperator logOperator = spy(LogOperator.INSTANCE);
        doReturn("OPERATOR").when(logOperator).getName();

        SettingsState.getInstance(getProject()).odtAPIPath = Path.of("src/test/resources/ODT-API.md").toString();

        BuiltinDocumentationService documentationService = BuiltinDocumentationService.getInstance(getProject());
        String documentation = documentationService.getDocumentation(logOperator);

        Assertions.assertTrue(documentation.contains("This is some information about the operator 'Operator'"));
    }

    @Test
    void testGetDocumentationIncludesFlags() {
        LogOperator logOperator = spy(LogOperator.INSTANCE);
        doReturn("OPERATOR").when(logOperator).getName();
        doReturn(List.of("!FlagA", "!FlagB")).when(logOperator).getFlags();

        SettingsState.getInstance(getProject()).odtAPIPath = Path.of("src/test/resources/ODT-API.md").toString();

        BuiltinDocumentationService documentationService = BuiltinDocumentationService.getInstance(getProject());
        String documentation = documentationService.getDocumentation(logOperator);

        Assertions.assertTrue(documentation.contains("This is some information about the operator 'Operator'"));
        Assertions.assertTrue(documentation.contains("!FlagA"));
        Assertions.assertTrue(documentation.contains("!FlagB"));
    }
}
