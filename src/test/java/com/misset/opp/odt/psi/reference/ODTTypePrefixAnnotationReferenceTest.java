package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTTypePrefixAnnotationReferenceTest extends OMTTestCase {

    @Test
    void testHasReferenceToPrefix() {
        String content = insideActivityWithPrefixes(
                "queries: |\n" +
                        "   /**\n" +
                        "    * @param $param (on<caret>t:SomeClass)\n" +
                        "    */\n" +
                        "   DEFINE QUERY query($param) => $param;\n"
        );
        configureByText(content);
        ReadAction.run(() -> Assertions.assertTrue(myFixture.getElementAtCaret() instanceof YAMLKeyValue));
    }

}
