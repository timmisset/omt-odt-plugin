package com.misset.opp.odt.completion;

import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTTraverseCompletionTest extends ODTTestCase {
    @Test
    void testQueryStepTraversion() {
        configureByText(withPrefixes("/ont:ClassA / <caret>"), true);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "^rdf:type", "ont:booleanPredicate", "ont:classPredicate");
    }

    @Test
    void testQueryStepTraversionReverse() {
        String content = withPrefixes(
                "VAR $variableA;\n" +
                        "VAR $anotherVariable = '' / <caret>");
        configureByText(content, true);
        assertContainsElements(completion.getLookupStrings(), "rdf:type", "^ont:stringPredicate");
    }

    @Test
    void testAtFirstStepInsideOfDefinedQuery() {
        String content = withPrefixes("DEFINE QUERY query => <caret>");
        configureByText(content, true);
        assertContainsElements(completion.getLookupStrings(), "rdf:type");
    }

    @Test
    void testNotAtFirstStepOutsideOfDefinedQuery() {
        String content = withPrefixes(
                "VAR $variableA;\n" +
                        "VAR $anotherVariable = <caret>");
        configureByText(content, true);
        assertDoesntContain(completion.getLookupStrings(), "rdf:type");
    }

    @Test
    void testQueryStepFiltersForType() {
        configureByText(withPrefixes(
                "/**\n" +
                        "* @param $booleanValue (boolean)\n" +
                        "*/\n" +
                        "DEFINE QUERY query($booleanValue) => $booleanValue;\n" +
                        "DEFINE QUERY anotherQuery => query(/ont:ClassA / ^rdf:type / <caret>);\n"), true);
        List<String> lookupStrings = completion.getLookupStrings();
        Assertions.assertTrue(lookupStrings.contains("ont:booleanPredicate"));
        Assertions.assertFalse(lookupStrings.contains("ont:classPredicate"));
    }

    @Test
    void testQueryStepQueryArray() {
        configureByText(withPrefixes("DEFINE QUERY query => /ont:ClassA / ^rdf:type / ont:booleanPredicate | <caret>"), true);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "ont:booleanPredicate", "ont:classPredicate");
    }

    @Test
    void testQueryStepWrappedQueryArray() {
        configureByText(withPrefixes("DEFINE QUERY query => /ont:ClassA / ^rdf:type / (ont:booleanPredicate | <caret>)"), true);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "ont:booleanPredicate", "ont:classPredicate");
    }
}
