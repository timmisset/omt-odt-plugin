package com.misset.opp.odt.completion.commands;

import com.misset.opp.testCase.OMTCompletionTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTCommandCompletionAssignTest extends OMTCompletionTestCase {

    @Test
    void testShowsPredicates() {
        String content = insideProcedureRunWithPrefixes(
                "@ASSIGN(/ont:ClassA_INSTANCE, <caret>)");
        configureByText(content, true);
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "/ont:booleanPredicate", "/ont:classPredicate");
        assertDoesntContain(lookupStrings, "/rdf:type");
    }

    @Test
    void testShowsPredicatesSkipsExisting() {
        String content = insideProcedureRunWithPrefixes(
                "@ASSIGN(/ont:ClassA_INSTANCE, /ont:booleanPredicate, true, <caret>)");
        configureByText(content, true);
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "/ont:classPredicate");
        assertDoesntContain(lookupStrings, "/ont:booleanPredicate", "/rdf:type");
    }

}