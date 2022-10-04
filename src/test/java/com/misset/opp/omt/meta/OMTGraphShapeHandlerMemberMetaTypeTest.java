package com.misset.opp.omt.meta;

import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class OMTGraphShapeHandlerMemberMetaTypeTest extends OMTTestCase {

    @Test
    void testShowsErrorWhenUnknownHandler() {
        myFixture.enableInspections(Collections.singleton(OMTValueInspection.class));
        String content = "handlers:\n" +
                "- unknownHandler";
        configureByText("my.module.omt", content);

        inspection.assertHasError("unknownHandler is not a known handler");
    }

    @Test
    void testShowsNoErrorWhenImportedHandler() {
        myFixture.enableInspections(Collections.singleton(OMTValueInspection.class));
        addFileToProject("myFile.omt",
                "model:\n" +
                        "   knownHandler: !GraphShapeHandlers\n" +
                        "       shape: myshape\n");
        String content = "" +
                "import:\n" +
                "   myFile.omt:\n" +
                "   - knownHandler\n" +
                "handlers:\n" +
                "- knownHandler";
        configureByText("my.module.omt", content);

        inspection.assertNoError("knownHandler is not a known handler");
    }

    @Test
    void testShowsNoErrorWhenDeclaredHandler() {
        myFixture.enableInspections(Collections.singleton(OMTValueInspection.class));
        String content = "" +
                "model:\n" +
                "   knownHandler: !GraphShapeHandlers\n" +
                "       shape: myshape\n" +
                "handlers:\n" +
                "- knownHandler";
        configureByText("my.module.omt", content);

        inspection.assertNoError("knownHandler is not a known handler");
    }

}
