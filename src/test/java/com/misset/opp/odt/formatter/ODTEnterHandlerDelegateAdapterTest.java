package com.misset.opp.odt.formatter;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.odt.testcase.ODTFormattingTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTEnterHandlerDelegateAdapterTest extends ODTFormattingTestCase {

    @Test
    void testInsertJavadocsBlock() {
        configureByText("/**<caret>\n" +
                "DEFINE QUERY query => 'hi';");
        enter();
        Assertions.assertEquals("/**\n" +
                " * \n" +
                " */\n" +
                "DEFINE QUERY query => 'hi';", getDocumentText());
        ReadAction.run(() -> {
            Assertions.assertEquals(7, myFixture.getCaretOffset());
        });
    }

}
