package com.misset.opp.omt.meta;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTActionsMetaTypeTest extends InspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTUnkownKeysInspection.class);
    }
    @Test
    void testCompletionShowsImportableMembers() {
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

}
