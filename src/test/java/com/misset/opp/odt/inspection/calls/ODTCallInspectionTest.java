package com.misset.opp.odt.inspection.calls;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.resolvable.call.ODTCall;
import com.misset.opp.odt.testcase.ODTFileTestImpl;
import com.misset.opp.odt.testcase.ODTTestCase;
import com.misset.opp.resolvable.Callable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.misset.opp.odt.inspection.calls.ODTCallInspection.ROOT_INDICATOR_EXPECTED;
import static org.mockito.Mockito.*;

class ODTCallInspectionTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTCallInspection.class));
    }

    @Test
    void testValidatesCallable() {
        String content = "@Command();";
        ODTFileTestImpl odtFileTest = configureByText(content);

        Callable callable = mock(Callable.class);
        doReturn("@Command").when(callable).getCallId();
        doAnswer(invocation -> {
            PsiElement element = invocation.getArgument(0);
            ProblemsHolder problemsHolder = invocation.getArgument(1);
            problemsHolder.registerProblem(element, "Some error", ProblemHighlightType.ERROR);
            return null;
        }).when(callable).validate(any(ODTCall.class), any(ProblemsHolder.class));
        odtFileTest.addCallable(callable);

        inspection.assertHasError("Some error");
    }

    @Test
    void testErrorWhenNoRootPathInCommandCall() {
        String content = "@CALL(ont:ClassA)";
        configureByText(content);
        inspection.assertHasError(ROOT_INDICATOR_EXPECTED);
    }

    @Test
    void testNoErrorWhenNoRootPathInOperatorCall() {
        String content = "CALL(ont:ClassA)";
        configureByText(content);
        inspection.assertNoError(ROOT_INDICATOR_EXPECTED);
    }
}
