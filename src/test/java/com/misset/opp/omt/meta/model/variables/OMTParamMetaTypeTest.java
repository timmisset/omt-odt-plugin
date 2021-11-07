package com.misset.opp.omt.meta.model.variables;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.OMTUnkownKeysInspection;
import com.misset.opp.omt.inspection.OMTValueInspection;
import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static com.misset.opp.omt.meta.model.variables.OMTParamMetaType.SYNTAX_ERROR;

class OMTParamMetaTypeTest extends InspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return List.of(OMTUnkownKeysInspection.class, OMTValueInspection.class);
    }

    @Test
    void inspectParameterShorthand() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       params:\n" +
                "       -   $param\n" +
                "";
        configureByText(content);
        assertNoErrors();
    }

    @Test
    void inspectParameterShorthandIncomplete() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       params:\n" +
                "       -   $param (ty\n" +
                "";
        configureByText(content);
        assertHasError(SYNTAX_ERROR);
    }

    @Test
    void inspectParameterShorthandNoTypeParenthesis() {
        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       params:\n" +
                "       -   $param type\n" +
                "";
        configureByText(content);
        assertHasError(SYNTAX_ERROR);
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
        assertNoErrors();
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
        assertHasError("Key 'typo' is not expected here");
    }
}