package com.misset.opp.odt.inspection.calls.commands;

import com.misset.opp.odt.ODTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.misset.opp.odt.inspection.calls.commands.ODTCommandInspectionAssign.*;

class ODTCommandInspectionAssignTest extends ODTTestCase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTCommandInspectionAssign.class));
    }

    @Test
    void testHasNoIssues() {
        String content = withPrefixes("" +
                "@ASSIGN(/ont:ClassA_INSTANCE, /ont:booleanPredicate, true);");
        configureByText(content);
        inspection.assertNoErrors();
        inspection.assertNoWarnings();
    }

    @Test
    void testHasWrongValueType() {
        String content = withPrefixes("" +
                "@ASSIGN(/ont:ClassA_INSTANCE, /ont:booleanPredicate, 'true');");
        configureByText(content);
        inspection.assertHasWarning("Incompatible types:", "'true'");
    }

    @Test
    void testRootIndicatorExpected() {
        String content = withPrefixes("" +
                "@ASSIGN(/ont:ClassA_INSTANCE, ont:booleanPredicate, true);");
        configureByText(content);
        inspection.assertHasError(ROOT_INDICATOR_EXPECTED);
    }

    @Test
    void testCannotAssignReversePath() {
        String content = withPrefixes("" +
                "@ASSIGN(/ont:ClassA_INSTANCE, /^ont:booleanPredicate, true);");
        configureByText(content);
        inspection.assertHasError(CANNOT_ASSIGN_USING_A_REVERSE_PATH);
    }

    @Test
    void testUnkownPredicate() {
        String content = withPrefixes("" +
                "@ASSIGN(/ont:ClassA_INSTANCE, /ont:unknownPredicate, true);");
        configureByText(content);
        inspection.assertHasError(UNKNOWN_PREDICATE);
    }

    @Test
    void testSinglePredicateExpected() {
        String content = withPrefixes("" +
                "@ASSIGN(/ont:ClassA_INSTANCE, /ont:booleanPredicate | /ont:stringPredicate, true);");
        configureByText(content);
        inspection.assertHasError(EXPECTED_A_SINGLE_PREDICATE);
    }

    @Test
    void testNotAPredicateForTheSubject() {
        String content = withPrefixes("" +
                "@ASSIGN(/ont:ClassA_INSTANCE, /ont:stringPredicate, true);");
        configureByText(content);
        inspection.assertHasWarning("Could not traverse FORWARD");
    }

    @Test
    void testValidStatement() {
        String content = withPrefixes("" +
                "@ASSIGN(/ont:ClassA_INSTANCE, /ont:booleanPredicate, true);");
        configureByText(content);
        inspection.assertNoWarning("Could not traverse FORWARD");
        inspection.assertNoErrors();
    }
}
