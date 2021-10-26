package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTCommandCall;
import com.misset.opp.odt.psi.ODTDefineName;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTCommandCallReferenceTest extends OMTTestCase {

    @Test
    void testODTReferenceCanResolveToDefineCommandInsideScript() {
        String content = insideActivityWithPrefixes(
                "onStart: |\n" +
                        "   DEFINE COMMAND commandA => { @LOG('test'); }\n" +
                        "   @<caret>commandA();\n" +
                        ""
        );
        configureByText(content);
        ReadAction.run(() -> {
            final ODTCommandCall elementByText = myFixture.findElementByText("@commandA", ODTCommandCall.class);
            // is resolved to the defined variable
            Assertions.assertTrue(elementByText.getReference().resolve() instanceof ODTDefineName);
        });
    }

    @Test
    void testODTReferenceCanResolveToDefineCommandInsideActivity() {
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
            Assertions.assertTrue(elementByText.getReference().resolve() instanceof ODTDefineName);
        });
    }

    @Test
    void testODTReferenceCanResolveToModelItem() {
        String content = "model:\n" +
                "   MyActivity: !Activity\n" +
                "       title: MijnActiviteit\n" +
                "   MyOtherActivity: !Activity\n" +
                "       onStart: |\n" +
                "           @<caret>MyActivity();\n" +
                "";
        configureByText(content);
        ReadAction.run(() -> {
            final ODTCommandCall elementByText = myFixture.findElementByText("@MyActivity", ODTCommandCall.class);
            // is resolved to the defined variable
            final PsiElement resolve = elementByText.getReference().resolve();
            Assertions.assertTrue(resolve instanceof YAMLKeyValue);
            Assertions.assertEquals("MyActivity", ((YAMLKeyValue) resolve).getKeyText());
        });
    }
}
