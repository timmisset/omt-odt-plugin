package com.misset.opp.odt;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTMultiHostInjectorTest extends OMTTestCase {

    @Test
    void testInjectODTInOnStart() {
        configureByText(insideActivityWithPrefixes(
                "onStart: |\n" +
                        "   @<caret>LOG('test');"
        ));
        ReadAction.run(() -> Assertions.assertNotNull(getCallByName("@LOG")));

    }
}
