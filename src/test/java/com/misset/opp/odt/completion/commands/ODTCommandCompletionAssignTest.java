package com.misset.opp.odt.completion.commands;

import com.misset.opp.odt.ODTTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTCommandCompletionAssignTest extends ODTTestCase {

    @Test
    void testShowsPredicates() {
        String content = withPrefixes(
                "@ASSIGN(/ont:ClassA_INSTANCE, <caret>)");
        configureByText(content, true);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "/ont:booleanPredicate", "/ont:classPredicate");
        assertDoesntContain(lookupStrings, "/rdf:type");
    }

    @Test
    void testShowsPredicatesSkipsExisting() {
        String content = withPrefixes(
                "@ASSIGN(/ont:ClassA_INSTANCE, /ont:booleanPredicate, true, <caret>)");
        configureByText(content, true);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "/ont:classPredicate");
        assertDoesntContain(lookupStrings, "/ont:booleanPredicate", "/rdf:type");
    }

}
