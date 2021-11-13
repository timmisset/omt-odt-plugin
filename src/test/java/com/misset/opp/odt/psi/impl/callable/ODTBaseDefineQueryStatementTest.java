package com.misset.opp.odt.psi.impl.callable;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ODTBaseDefineQueryStatementTest extends InspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        // only annotations
        return Collections.emptyList();
    }

    @Test
    void testMissingSemicolon() {
        configureByText("queries: \n" +
                "   DEFINE QUERY query => 'hello'");
        assertHasError("Missing semicolon");
    }
}
