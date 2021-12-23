package com.misset.opp.omt.meta;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Set;

class OMTActionsMapMetaTypeTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Set.of(OMTUnkownKeysInspection.class, OMTValueInspection.class);
    }

    @Test
    void testNoErrorsOnActionsMap() {
        configureByText(insideActivityWithPrefixes(
                "actions: \n" +
                        "   myAction:\n" +
                        "       onSelect: |\n" +
                        "           @LOG('do something');\n" +
                        "       disabled: true\n" +
                        ""
        ));
        assertNoErrors();
    }

    @Test
    void testNoIdForMappedAction() {
        configureByText(insideActivityWithPrefixes(
                "actions: \n" +
                        "   myAction:\n" +
                        "       id: id\n"
        ));
        assertHasWarning("Unnecessary id field when action is mapped to a key");
    }

}
