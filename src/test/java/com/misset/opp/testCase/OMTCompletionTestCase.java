
package com.misset.opp.testCase;

import com.intellij.codeInsight.lookup.LookupElement;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OMTCompletionTestCase extends OMTOntologyTestCase {

    protected LookupElement[] getLookupElements() {
        return myFixture.completeBasic();
    }

    protected List<String> getLookupStrings() {
        return Arrays.stream(getLookupElements()).map(
                LookupElement::getLookupString
        ).collect(Collectors.toList());
    }

}
