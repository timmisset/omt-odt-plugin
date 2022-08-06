package com.misset.opp.odt.completion;

import com.misset.opp.testCase.OMTCompletionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTTraverseCompletionTest extends OMTCompletionTestCase {
    @Test
    void testQueryStepTraversion() {
        configureByText(insideQueryWithPrefixes("/ont:ClassA / <caret>"), true);
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "^rdf:type", "ont:booleanPredicate", "ont:classPredicate");
    }

    @Test
    void testQueryStepTraversionReverse() {
        String content = insideProcedureRunWithPrefixes(
                "VAR $variableA;\n" +
                        "VAR $anotherVariable = '' / <caret>");
        configureByText(content, true);
        assertContainsElements(getLookupStrings(), "rdf:type", "^ont:stringPredicate");
    }

    @Test
    void testAtFirstStepInsideOfDefinedQuery() {
        String content = insideQueryWithPrefixes("<caret>");
        configureByText(content, true);
        assertContainsElements(getLookupStrings(), "rdf:type");
    }

    @Test
    void testNotAtFirstStepOutsideOfDefinedQuery() {
        String content = insideProcedureRunWithPrefixes(
                "VAR $variableA;\n" +
                        "VAR $anotherVariable = <caret>");
        configureByText(content, true);
        assertDoesntContain(getLookupStrings(), "rdf:type");
    }

    @Test
    void testQueryStepFiltersForType() {
        configureByText(withPrefixes("queries:\n" +
                "   /**\n" +
                "    * @param $booleanValue (boolean)\n" +
                "    */\n" +
                "   DEFINE QUERY query($booleanValue) => $booleanValue;\n" +
                "   DEFINE QUERY anotherQuery => query(/ont:ClassA / ^rdf:type / <caret>);\n"), true);
        List<String> lookupStrings = getLookupStrings();
        Assertions.assertTrue(lookupStrings.contains("ont:booleanPredicate"));
        Assertions.assertFalse(lookupStrings.contains("ont:classPredicate"));
    }

    @Test
    void testQueryStepQueryArray() {
        configureByText(insideQueryWithPrefixes("/ont:ClassA / ^rdf:type / ont:booleanPredicate | <caret>"), true);
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "ont:booleanPredicate", "ont:classPredicate");
    }

    @Test
    void testQueryStepWrappedQueryArray() {
        configureByText(insideQueryWithPrefixes("/ont:ClassA / ^rdf:type / (ont:booleanPredicate | <caret>)"), true);
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "ont:booleanPredicate", "ont:classPredicate");
    }
}
