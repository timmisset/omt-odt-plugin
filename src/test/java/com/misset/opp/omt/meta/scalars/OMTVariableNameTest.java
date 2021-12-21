package com.misset.opp.omt.meta.scalars;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static com.misset.opp.omt.meta.scalars.OMTVariableNameMetaType.SYNTAX_ERROR;

class OMTVariableNameTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTValueInspection.class);
    }

    @Test
    void validateScalarValueErrorWhenNoName() {

        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       params:\n" +
                "       -   name: $\n" +
                "";
        configureByText(content);
        assertHasError(SYNTAX_ERROR);
    }

    @Test
    void validateScalarValueErrorWhenNoPrefix() {

        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       params:\n" +
                "       -   name: param\n" +
                "";
        configureByText(content);
        assertHasError(SYNTAX_ERROR);
    }

    @Test
    void validateScalarValueNoError() {

        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       params:\n" +
                "       -   name: $param\n" +
                "";
        configureByText(content);
        assertNoErrors();
    }
}
