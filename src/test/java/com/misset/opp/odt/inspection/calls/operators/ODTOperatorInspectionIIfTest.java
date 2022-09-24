package com.misset.opp.odt.inspection.calls.operators;

import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collections;
import java.util.stream.Stream;

import static com.misset.opp.odt.inspection.calls.operators.ODTOperatorInspectionIIf.SIMPLIFY;
import static com.misset.opp.odt.inspection.calls.operators.ODTOperatorInspectionIIf.UNNECESSARY_IIF;

class ODTOperatorInspectionIIfTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTOperatorInspectionIIf.class));
    }

    public static Stream<Arguments> getSimplyStatements() {
        return Stream.of(
                Arguments.of("IIF($condition, true)", "$condition"),
                Arguments.of("IIF($condition, false)", "NOT $condition"),
                Arguments.of("IIF($condition, $variable == $anotherVariable)", "$condition AND $variable == $anotherVariable"),
                Arguments.of("IIF($condition, true, false)", "$condition"),
                Arguments.of("IIF($condition, false, true)", "NOT $condition"),
                Arguments.of("IIF($condition, true, $variable == $anotherVariable)", "$condition OR $variable == $anotherVariable")
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "IIF($condition, true, false)",
            "IIF($condition, true, 'a' == 'b')",
            "IIF($condition, $variable == 'a')"
    })
    void testHasUnnecessaryIIFWarning(String content) {
        configureByText(content);
        inspection.assertHasWarning(UNNECESSARY_IIF);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "IIF($condition, 'a', 'b')",
            "IIF($condition, true, 'b')",
            "IIF($condition, 'a', true)"

    })
    void testHasNoUnnecessaryIIFWarning(String content) {
        configureByText(content);
        inspection.assertNoWarning(UNNECESSARY_IIF);
    }

    @ParameterizedTest
    @MethodSource(value = "getSimplyStatements")
    void testInvokeSimplify(String content, String result) {
        configureByText(content);
        inspection.invokeQuickFixIntention(SIMPLIFY);
        Assertions.assertEquals(result, getFile().getText());
    }
}
