package com.misset.opp.odt.psi.references;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.odt.psi.ODTCommandCall;
import com.misset.opp.odt.psi.ODTDefineCommandStatement;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTCommandCallReferenceTest extends OMTTestCase {

    @Test
    void testODTReferenceCanResolve() {
        String content = insideActivityWithPrefixes(
                "commands: |\n" +
                        "   DEFINE COMMAND commandA => { @LOG('test'); }\n" +
                        "\n" +
                        "onStart: |\n" +
                        "   @<caret>commandA();\n" +
                        ""
        );
        configureByText(content);
        ReadAction.run(() -> {
            final ODTCommandCall elementByText = myFixture.findElementByText("@commandA", ODTCommandCall.class);
            // is resolved to the defined variable
            Assertions.assertTrue(elementByText.getReference()
                    .resolve() instanceof ODTDefineCommandStatement);
        });
    }
}
