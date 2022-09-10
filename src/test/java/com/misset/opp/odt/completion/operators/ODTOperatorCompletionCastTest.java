package com.misset.opp.odt.completion.operators;

import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTOperatorCompletionCastTest extends ODTTestCase {

    @Test
    void testODTOperatorCompletionCastHasTypes() {
        String content = withPrefixes("CAST(<caret>)");
        configureByText(content);

        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "/xsd:integer", "/xsd:string", "JSON", "IRI");
    }

}
