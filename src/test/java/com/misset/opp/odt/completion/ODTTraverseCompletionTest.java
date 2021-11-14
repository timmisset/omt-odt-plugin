package com.misset.opp.odt.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.misset.opp.testCase.OMTOntologyTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTTraverseCompletionTest extends OMTOntologyTestCase {
    @Test
    void testQueryStepForwardTraversion() {
        configureByText(insideQueryWithPrefixes("/ont:ClassA / <caret>"), true);
        final LookupElement[] lookupElements = myFixture.completeBasic();
        Assertions.assertNotEquals(0, lookupElements.length);
    }
}
