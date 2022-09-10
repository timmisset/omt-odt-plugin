package com.misset.opp.omt.meta.model.variables;

import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.misset.opp.omt.meta.model.variables.OMTParamMetaType.SYNTAX_ERROR;

class OMTParamMetaTypeTest extends OMTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Set.of(OMTUnkownKeysInspection.class, OMTValueInspection.class));
    }

    @Test
    void inspectParameterShorthand() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       params:\n" +
                "       -   $param\n" +
                "";
        configureByText(content);
        inspection.assertNoErrors();
    }

    @Test
    void inspectParameterShorthandIncomplete() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       params:\n" +
                "       -   $param (ty\n" +
                "";
        configureByText(content);
        inspection.assertHasError(SYNTAX_ERROR);
    }

    @Test
    void inspectParameterShorthandNoTypeParentheses() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       params:\n" +
                "       -   $param type\n" +
                "";
        configureByText(content);
        inspection.assertHasError(SYNTAX_ERROR);
    }

    @Test
    void inspectUnknownKeyInspectionNoErrors() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       params:\n" +
                "       -   name: $param\n" +
                "           type: SomeType\n" +
                "";
        configureByText(content);
        inspection.assertNoError("Key"); // no errors that indicate a missing key
    }

    @Test
    void inspectUnknownKeyInspectionUnknownKey() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       params:\n" +
                "       -   name: $param\n" +
                "           typo: SomeType\n" +
                "";
        configureByText(content);
        inspection.assertHasError("Key 'typo' is not expected here");
    }
}
