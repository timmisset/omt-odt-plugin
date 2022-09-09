package com.misset.opp.odt.inspection.redundancy;

import com.misset.opp.odt.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.misset.opp.odt.inspection.redundancy.ODTStyleInspectionUnnecessaryIdentifierOperator.REMOVE_OPERATOR;
import static com.misset.opp.odt.inspection.redundancy.ODTStyleInspectionUnnecessaryIdentifierOperator.UNNECESSARY_IDENTIFIER_OPERATOR;

class ODTStyleInspectionUnnecessaryIdentifierOperatorTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTStyleInspectionUnnecessaryIdentifierOperator.class));
    }

    @Test
    void testUnnecessaryIdentifierOperatorAsFirstStep() {
        configureByText("DEFINE QUERY query => . / ont:property;");
        inspection.assertHasWarning(UNNECESSARY_IDENTIFIER_OPERATOR);
    }

    @Test
    void testUnnecessaryIdentifierOperatorInFilter() {
        configureByText("DEFINE QUERY query => /ont:ClassA[. / rdf:type == /ont:ClassA];");
        inspection.assertHasWarning(UNNECESSARY_IDENTIFIER_OPERATOR);
    }

    @Test
    void testUnnecessaryIdentifierOperatorNoWarningInSignatureArgument() {
        configureByText("DEFINE QUERY query => CALL(.);");
        inspection.assertNoWarning(UNNECESSARY_IDENTIFIER_OPERATOR);
    }

    @Test
    void testUnnecessaryIdentifierOperatorNoWarningWhenHasFilter() {
        configureByText("DEFINE QUERY query => . [rdf:type == /ont:ClassA];");
        inspection.assertNoWarning(UNNECESSARY_IDENTIFIER_OPERATOR);
    }

    @Test
    void testUnnecessaryIdentifierOperatorNoWarningWhenOnlyStep() {
        configureByText("DEFINE QUERY query => true / CHOOSE WHEN . == true => true OTHERWISE => . END;");
        inspection.assertNoWarning(UNNECESSARY_IDENTIFIER_OPERATOR);
    }

    @Test
    void testRemoveStep() {
        configureByText("DEFINE QUERY query => . / ont:property;");
        inspection.invokeQuickFixIntention(REMOVE_OPERATOR);
        Assertions.assertEquals("DEFINE QUERY query => ont:property;", getFile().getText());
    }

    @Test
    void testUnnecessaryIdentifierOperatorDoesntRemoveFilter() {
        configureByText("DEFINE QUERY query => /ont:ClassA / . [rdf:type == /ont:ClassA];");
        inspection.assertHasWarning(UNNECESSARY_IDENTIFIER_OPERATOR);
        inspection.invokeQuickFixIntention(REMOVE_OPERATOR);
        Assertions.assertEquals("DEFINE QUERY query => /ont:ClassA / [rdf:type == /ont:ClassA];", getFile().getText());
    }
}
