package com.misset.opp.odt.completion;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

class ODTTypeClassNameCompletionTest extends ODTTestCase {

    @ParameterizedTest
    @ValueSource(strings = {
            "/<caret>",
            "DEFINE QUERY query => /<caret>",
            "VAR $variable = /<caret>"
    })
    void testHasClassesAtRoot(String content) {
        configureByText(withPrefixes(content), true);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "/ont:ClassA");
        assertDoesntContain(lookupStrings, "/" + OntologyModelConstants.getXsdString().getURI(), "/" + OntologyModelConstants.getXsdStringInstance().getURI());
    }

}
