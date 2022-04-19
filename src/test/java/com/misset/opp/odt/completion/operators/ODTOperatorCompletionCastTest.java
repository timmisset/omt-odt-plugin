package com.misset.opp.odt.completion.operators;

import com.misset.opp.testCase.OMTCompletionTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTOperatorCompletionCastTest extends OMTCompletionTestCase {

    @Test
    void testODTOperatorCompletionCastHasTypes() {
        String content = insideQueryWithPrefixes("CAST(<caret>)");
        configureByText(content);

        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "/xsd:integer", "/xsd:string", "JSON", "IRI");
    }

}
