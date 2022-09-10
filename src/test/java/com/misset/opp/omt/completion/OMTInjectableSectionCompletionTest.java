package com.misset.opp.omt.completion;

import com.misset.opp.omt.testcase.OMTTestCase;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.misset.opp.omt.completion.OMTInjectableSectionCompletion.*;

class OMTInjectableSectionCompletionTest extends OMTTestCase {

    @Test
    void testHasQueryTemplateCompletions() {
        String content = "queries: |\n" +
                "   <caret>";
        configureByText(content);
        List<String> lookupStrings = completion.getLookupStrings();
        Assertions.assertTrue(lookupStrings.stream().anyMatch(QUERY_SIMPLE_TEMPLATE::equals));
        Assertions.assertTrue(lookupStrings.stream().anyMatch(QUERY_PARAMETER_TEMPLATE::equals));
        Assertions.assertTrue(lookupStrings.stream().anyMatch(QUERY_BASE_TEMPLATE::equals));
    }

    @Test
    void testHasQueryTemplateAtNextPositionCompletions() {
        String content = "queries: |\n" +
                "   DEFINE QUERY query => '';\n" +
                "   <caret>";
        configureByText(content);
        List<String> lookupStrings = completion.getLookupStrings();
        Assertions.assertTrue(lookupStrings.stream().anyMatch(QUERY_SIMPLE_TEMPLATE::equals));
        Assertions.assertTrue(lookupStrings.stream().anyMatch(QUERY_PARAMETER_TEMPLATE::equals));
        Assertions.assertTrue(lookupStrings.stream().anyMatch(QUERY_BASE_TEMPLATE::equals));
    }

    @Test
    void testHasNoQueryTemplateCompletionsInsideStatement() {
        String content = "queries: |\n" +
                "   DEFINE QUERY query => <caret>";
        configureByText(content, true);
        List<String> lookupStrings = completion.getLookupStrings();
        Assertions.assertTrue(lookupStrings.stream().noneMatch(QUERY_SIMPLE_TEMPLATE::equals));
        Assertions.assertTrue(lookupStrings.stream().noneMatch(QUERY_PARAMETER_TEMPLATE::equals));
        Assertions.assertTrue(lookupStrings.stream().noneMatch(QUERY_BASE_TEMPLATE::equals));
    }

    @Test
    void testHasNoQueryTemplateCompletionsInsideCompleteStatement() {
        String content = "queries: |\n" +
                "   DEFINE QUERY query => <caret>;";
        configureByText(content, true);
        List<String> lookupStrings = completion.getLookupStrings();
        Assertions.assertTrue(lookupStrings.stream().noneMatch(QUERY_SIMPLE_TEMPLATE::equals));
        Assertions.assertTrue(lookupStrings.stream().noneMatch(QUERY_PARAMETER_TEMPLATE::equals));
        Assertions.assertTrue(lookupStrings.stream().noneMatch(QUERY_BASE_TEMPLATE::equals));
    }

    @Test
    void testHasCommandTemplateCompletions() {
        String content = "commands: |\n" +
                "   <caret>";
        configureByText(content);
        List<String> lookupStrings = completion.getLookupStrings();
        Assertions.assertTrue(lookupStrings.stream().anyMatch(COMMAND_SIMPLE_TEMPLATE::equals));
        Assertions.assertTrue(lookupStrings.stream().anyMatch(COMMAND_PARAMETER_TEMPLATE::equals));
    }

    @Test
    void testHasNoCommandTemplateCompletionsInsideStatement() {
        String content = "commands: |\n" +
                "   DEFINE COMMAND command => { <caret> }";
        configureByText(content, true);
        List<String> lookupStrings = completion.getLookupStrings();
        Assertions.assertTrue(lookupStrings.stream().noneMatch(COMMAND_SIMPLE_TEMPLATE::equals));
        Assertions.assertTrue(lookupStrings.stream().noneMatch(COMMAND_PARAMETER_TEMPLATE::equals));
    }

    @Test
    void testHasGraphShapeCompletions() {
        OppModelConstants.getGraphShape().createIndividual("http://data/graphshape");
        String content = "model:\n" +
                "   GraphShapeHandler: !GraphShapeHandlers\n" +
                "       id: test\n" +
                "       shape: <caret>\n" +
                "";
        configureByText(content, true);
        assertContainsElements(completion.getLookupStrings(), "/<http://data/graphshape>");
    }

    @Test
    void testHasBooleanCompletions() {
        String content = insideActivityWithPrefixes(
                "" +
                        "variables:\n" +
                        "- $boolean = true\n" +
                        "rules:\n" +
                        "   myRule:\n" +
                        "       query: true\n" +
                        "       strict: <caret>\n"
        );
        configureByText(content);
        assertContainsElements(completion.getLookupStrings(), "true", "false", "$boolean");
    }
}
