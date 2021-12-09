package com.misset.opp.omt.stubs.index;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.stubs.StubIndex;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;

class OMTImportStubIndexTest extends OMTTestCase {

    @Test
    void testCreatesIndexForImports() {
        configureByText("import:\n" +
                "   ./a-file.omt:\n" +
                "   - memberA\n" +
                "   - memberB\n");
        ReadAction.run(() -> {
            Collection<String> allKeys = StubIndex.getInstance().getAllKeys(OMTImportStubIndex.KEY, getProject());
            assertContainsElements(allKeys, "memberA", "memberB");
        });

    }

}