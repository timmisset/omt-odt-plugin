package com.misset.opp.omt.injection;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OMTODTMultiHostInjectorTest extends OMTTestCase {

    @Test
    void testInjectODTInOnStart() {
        configureByText(insideActivityWithPrefixes(
                "onStart: |\n" +
                        "   @<caret>LOG('test');"
        ));
        ReadAction.run(() -> Assertions.assertNotNull(getCallByName("LOG")));
    }

}
