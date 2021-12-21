package com.misset.opp.odt.inspection.calls.commands;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import com.misset.opp.testCase.OMTOntologyTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static com.misset.opp.odt.inspection.calls.commands.ODTCommandInspectionAssign.*;

class ODTCommandInspectionAssignTest extends OMTInspectionTestCase {
    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();
        OMTOntologyTestCase.initOntologyModel();
    }

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTCommandInspectionAssign.class);
    }

    @Test
    void testRootIndicatorExpected() {
        String content = insideProcedureRunWithPrefixes("" +
                "@ASSIGN(/ont:ClassA_INSTANCE, ont:booleanPredicate, true);");
        configureByText(content);
        assertHasError(ROOT_INDICATOR_EXPECTED);
    }

    @Test
    void testCannotAssignReversePath() {
        String content = insideProcedureRunWithPrefixes("" +
                "@ASSIGN(/ont:ClassA_INSTANCE, /^ont:booleanPredicate, true);");
        configureByText(content);
        assertHasError(CANNOT_ASSIGN_USING_A_REVERSE_PATH);
    }

    @Test
    void testUnkownPredicate() {
        String content = insideProcedureRunWithPrefixes("" +
                "@ASSIGN(/ont:ClassA_INSTANCE, /ont:unknownPredicate, true);");
        configureByText(content);
        assertHasError(UNKNOWN_PREDICATE);
    }

    @Test
    void testSinglePredicateExpected() {
        String content = insideProcedureRunWithPrefixes("" +
                "@ASSIGN(/ont:ClassA_INSTANCE, /ont:booleanPredicate | /ont:stringPredicate, true);");
        configureByText(content);
        assertHasError(EXPECTED_A_SINGLE_PREDICATE);
    }

    @Test
    void testNotAPredicateForTheSubject() {
        String content = insideProcedureRunWithPrefixes("" +
                "@ASSIGN(/ont:ClassA_INSTANCE, /ont:stringPredicate, true);");
        configureByText(content);
        assertHasWarning("Could not traverse FORWARD");
    }

    @Test
    void testValidStatement() {
        String content = insideProcedureRunWithPrefixes("" +
                "@ASSIGN(/ont:ClassA_INSTANCE, /ont:booleanPredicate, true);");
        configureByText(content);
        assertNoWarning("Could not traverse FORWARD");
        assertNoErrors();
    }
}
